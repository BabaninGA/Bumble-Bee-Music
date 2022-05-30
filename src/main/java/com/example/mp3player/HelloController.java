package com.example.mp3player;



import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.nio.channels.FileChannel;
import java.nio.file.Files;


import org.apache.commons.io.FileUtils;


public class HelloController {

    private Timer timer;
    private TimerTask task;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private boolean running;
    private boolean isMuted = false;
    private boolean isPlaying = true;
    private double volPerc;
    private int prevVol;
    private double totalTime;
    private double currentTime;

    private ImageView iconMute;
    private ImageView iconVolume;
    private ImageView iconPlay;
    private ImageView iconPause;
    private ImageView iconReset;
    private ImageView iconNext;
    private ImageView iconPrevious;
    private ImageView iconShuffle;
    private ImageView iconPlus;
    private ImageView iconMinus;

    @FXML
    private Label shuffleMedia;
    @FXML
    private Button openMedia;
    @FXML
    private Button createPlaylist;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider songSlider;
    @FXML
    private Label songLabel;
    @FXML
    private ScrollBar scrollBar;
    @FXML
    private AnchorPane allButtons;
    @FXML
    private AnchorPane bottomMenu;
    @FXML
    private AnchorPane mainWindow;
    @FXML
    private AnchorPane sideMenu;
    @FXML
    private Label labelCurrentTime;
    @FXML
    private Label labelTotalTime;
    @FXML
    private Label labelRemainingTime;
    @FXML
    private Label labelVolume;
    @FXML
    private Label volumeOff;
    @FXML
    private Label labelButtonPPR;
    @FXML
    private Label backMedia;
    @FXML
    private Label forwardMedia;
    @FXML
    private HBox hboxTime;
    @FXML
    private HBox hboxVolume;
    @FXML
    private Label nextSongButton;
    @FXML
    private Label previousSongButton;
    @FXML
    private ListView<?> playlistList;
    @FXML
    private ListView<?> songList;
    @FXML
    private Label songName;
    @FXML
    private Label songAuthor;


    @FXML
    private void addMedia(ActionEvent event) throws IOException {
        System.out.println("Добавить файл");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select mp3", "*.mp3");
        fileChooser.getExtensionFilters().add(filter);
        String filePath = fileChooser.showOpenDialog(null).toURI().toString();
        File f = new File(filePath);
        System.out.println(filePath);
        String name = f.getName();
        name = name.replaceAll("%20", " ");
        name = name.replaceAll(".mp3", "");
        System.out.println(name);

        String track = filePath.replaceAll("file:/", "");

        File playlist_file = new File("a_playlist_file");
        BufferedWriter writer = new BufferedWriter(new FileWriter(playlist_file));
        writer.write(track);

        writer.close();

//        SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(playlist_file);
//
//        Playlist genericPlaylist = specificPlaylist.toPlaylist();



        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            setIcons();
            mediaPlayer.play();
            labelVolume.setText("10%");
            volumeSlider.setValue(10.0);
            mediaPlayer.setVolume(10.0 * 0.01);
            beginTimer();
            songLabel.setText(name);

            bottomMenu.setVisible(true);
            scrollBar.setVisible(true);

            hboxTime.getChildren().remove(labelRemainingTime);
            hboxVolume.getChildren().remove(volumeSlider);
            hboxVolume.getChildren().remove(labelVolume);


            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                    songSlider.setValue(newValue.toSeconds());
                }
            });
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                    bindCurrentTimeLabel();
                    if (!songSlider.isValueChanging()) {
                        songSlider.setValue(newTime.toSeconds());
                    }
                    labelCurrentTime.getText();
                    labelTotalTime.getText();
                }
            });
            mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                    songSlider.setMax(newDuration.toSeconds());
                    labelTotalTime.setText(getTime(newDuration));

                }
            });
            songSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(songSlider.getValue()));

                }
            });
            songSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(songSlider.getValue()));
                }
            });
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total = media.getDuration();
                    songSlider.setMax(total.toSeconds());
                }
            });

            forwardMedia.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("+ 10 секунд");
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(+10)));
                }
            });

            backMedia.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("- 10 секунд");
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
                }
            });

            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    volPerc = volumeSlider.getValue();
                    int result = Math.toIntExact(Math.round(volPerc));
                    String res = String.valueOf(result);
                    labelVolume.setText(res + "%");
                    if (volPerc == 0) {
                        isMuted = true;
                        volumeOff.setGraphic(iconMute);
                    } else {
                        isMuted = false;
                        volumeOff.setGraphic(iconVolume);
                    }
                }
            });

            hboxVolume.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (hboxVolume.lookup("#volumeSlider") == null) {
                        hboxVolume.getChildren().add(volumeSlider);
                        hboxVolume.getChildren().add(labelVolume);
                        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                            }
                        });
                        volumeOff.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if (isMuted) {
                                    volumeOn();
                                } else {
                                    volumeOff();
                                }
                            }
                        });
                    }
                }
            });
            hboxVolume.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    hboxVolume.getChildren().remove(volumeSlider);
                    hboxVolume.getChildren().remove(labelVolume);
                }
            });

            //hboxTime.setOnMouseEntered(new EventHandler<MouseEvent>() {
                //@Override
                //public void handle(MouseEvent mouseEvent) {
                    //hboxTime.getChildren().add(labelRemainingTime);
                    //totalTime = mediaPlayer.getCurrentTime().toMinutes();
                    //currentTime = mediaPlayer.getTotalDuration().toMinutes();
                    //double result = -(totalTime - currentTime);
                    //String res = String.valueOf(result);
                    //labelRemainingTime.setText(res);
               // }
            //});
            //hboxTime.setOnMouseExited(new EventHandler<MouseEvent>() {
                //@Override
                //public void handle(MouseEvent mouseEvent) {
                    //hboxTime.getChildren().remove(labelRemainingTime);
                //}
            //});

            labelButtonPPR.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isPlaying) {
                        pauseMedia();
                    } else {
                        playMedia();
                    }
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = mediaPlayer.getTotalDuration().toSeconds();
                    //if (current / end == 1) {
                    //   resetMedia();
                    //   labelButtonPPR.setGraphic(iconPause);
                    // }
                }
            });
        }
    }

    private void volumeOff() {
        volumeOff.setGraphic(iconMute);
        volPerc = volumeSlider.getValue();
        int result = Math.toIntExact(Math.round(volPerc));
        prevVol = result;
        volumeSlider.setValue(0);
        System.out.println("Выключение звука");
        isMuted = true;
    }
    private void volumeOn() {
        volumeSlider.setValue(prevVol);
        volumeOff.setGraphic(iconVolume);
        System.out.println("Включение звука");
        isMuted = false;
    }

    @FXML
    private void createPlaylist() {
        System.out.println("Создать плейлист");
    }

    @FXML
    private void openMedia() {
        System.out.println("Открыть файл");
    }

    //@FXML
    //private void resetMedia() {
    //System.out.println("Запуск файла с самого начала");
    //mediaPlayer.stop();
    //songSlider.setValue(0);
    //beginTimer();
    //mediaPlayer.play();
    //}


    private void playMedia() {
        labelButtonPPR.setGraphic(iconPause);
        System.out.println("Воспроизведение");
        beginTimer();
        mediaPlayer.play();
        isPlaying = true;
    }
    private void pauseMedia() {
        labelButtonPPR.setGraphic(iconPlay);
        System.out.println("Пауза");
        mediaPlayer.pause();
        cancelTimer();
        isPlaying = false;
    }


    private void setIcons(){
        Image imageMute = new Image(new File("src/resources/mute.png").toURI().toString());
        iconMute = new ImageView(imageMute);
        iconMute.setFitWidth(25);
        iconMute.setFitHeight(25);

        Image imageVol = new Image(new File("src/resources/volume.png").toURI().toString());
        iconVolume = new ImageView(imageVol);
        iconVolume.setFitWidth(25);
        iconVolume.setFitHeight(25);

        Image imagePlay = new Image(new File("src/resources/play-btn.png").toURI().toString());
        iconPlay = new ImageView(imagePlay);
        iconPlay.setFitWidth(27);
        iconPlay.setFitHeight(27);

        Image imagePause = new Image(new File("src/resources/stop-btn.png").toURI().toString());
        iconPause = new ImageView(imagePause);
        iconPause.setFitWidth(25);
        iconPause.setFitHeight(25);

        Image imageReset = new Image(new File("src/resources/reset-btn.png").toURI().toString());
        iconReset = new ImageView(imageReset);
        iconReset.setFitWidth(25);
        iconReset.setFitHeight(25);

        Image imageNext = new Image(new File("src/resources/next-btn.png").toURI().toString());
        iconNext = new ImageView(imageNext);
        iconNext.setFitWidth(23);
        iconNext.setFitHeight(23);

        Image imagePrevious = new Image(new File("src/resources/previous-btn.png").toURI().toString());
        iconPrevious = new ImageView(imagePrevious);
        iconPrevious.setFitWidth(23);
        iconPrevious.setFitHeight(23);

        Image imageShuffle = new Image(new File("src/resources/shuffle-btn.png").toURI().toString());
        iconShuffle = new ImageView(imageShuffle);
        iconShuffle.setFitWidth(22);
        iconShuffle.setFitHeight(22);

        Image imagePlus = new Image(new File("src/resources/plus-btn.png").toURI().toString());
        iconPlus = new ImageView(imagePlus);
        iconPlus.setFitWidth(21);
        iconPlus.setFitHeight(21);

        Image imageMinus = new Image(new File("src/resources/minus-btn.png").toURI().toString());
        iconMinus = new ImageView(imageMinus);
        iconMinus.setFitWidth(21);
        iconMinus.setFitHeight(21);

        backMedia.setGraphic(iconMinus);
        forwardMedia.setGraphic(iconPlus);
        shuffleMedia.setGraphic(iconShuffle);
        volumeOff.setGraphic(iconVolume);
        labelButtonPPR.setGraphic(iconPause);
        nextSongButton.setGraphic(iconNext);
        previousSongButton.setGraphic(iconPrevious);
    }

    public String getTime(Duration time) {

        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();

        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;
        if (hours > 59) hours = hours % 60;

        if (hours > 0) return String.format("%d:%02d:%02d",
                hours,
                minutes,
                seconds);
        else return String.format("%02d:%02d",
                minutes,
                seconds);
    }
    public void bindCurrentTimeLabel() {
        labelCurrentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() {
                return getTime(mediaPlayer.getCurrentTime());
            }
        }, mediaPlayer.currentTimeProperty()));
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songSlider.setValue(current / end);
                if (current / end == 1) {
                    labelButtonPPR.setGraphic(iconReset);
                    cancelTimer();
                }
            }
        };
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }
}
