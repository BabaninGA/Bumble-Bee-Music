package com.example.mp3player;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

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

    private ImageView iconMute;
    private ImageView iconVolume;
    private ImageView iconMain;
    private ImageView iconPlay;
    private ImageView iconPause;
    private ImageView iconReset;
    private ImageView iconNext;
    private ImageView iconPrevious;
    private ImageView iconShuffle;

    @FXML
    private Label shuffleMedia;
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
    private Label labelVolume;
    @FXML
    private Label volumeOff;
    @FXML
    private Label iconm;
    @FXML
    private Label labelButtonPPR;
    @FXML
    private HBox hboxVolume;
    @FXML
    private Label nextSongButton;
    @FXML
    private Label previousSongButton;



    @FXML
    private void openMedia(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select mp3", "*.mp3");
        fileChooser.getExtensionFilters().add(filter);
        String filePath = fileChooser.showOpenDialog(null).toURI().toString();
        File f = new File(filePath);
        System.out.println(filePath);
        String[] subStr;
        String delimiter = "/";
        subStr = filePath.split(delimiter);
        int i = subStr.length - 1;
        String name = f.getName();
        name = name.replaceAll("%20", " ");
        name = name.replaceAll(".mp3", "");
        System.out.println(name);

        Image imageMute = new Image(new File("src/resources/mute.png").toURI().toString());
        iconMute = new ImageView(imageMute);
        iconMute.setFitWidth(25);
        iconMute.setFitHeight(25);

        Image imageVol = new Image(new File("src/resources/volume.png").toURI().toString());
        iconVolume = new ImageView(imageVol);
        iconVolume.setFitWidth(25);
        iconVolume.setFitHeight(25);

        Image iconmain = new Image(new File("src/resources/MAIN.png").toURI().toString());
        iconMain = new ImageView(iconmain);
        iconMain.setFitWidth(130);
        iconMain.setFitHeight(130);

        Image imagePlay = new Image(new File("src/resources/play-btn.png").toURI().toString());
        iconPlay = new ImageView(imagePlay);
        iconPlay.setFitWidth(25);
        iconPlay.setFitHeight(25);

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


        shuffleMedia.setGraphic(iconShuffle);
        iconm.setGraphic(iconMain);
        volumeOff.setGraphic(iconVolume);
        labelButtonPPR.setGraphic(iconPause);
        nextSongButton.setGraphic(iconNext);
        previousSongButton.setGraphic(iconPrevious);

        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            beginTimer();
            songLabel.setText(name);

            bottomMenu.setVisible(true);
            scrollBar.setVisible(true);
            createPlaylist.setVisible(true);

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


            labelButtonPPR.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isPlaying) {
                        pauseMedia();
                    } else {
                        playMedia();
                    }
                }
            });
        }
    }

    @FXML
    private void forwardMedia(ActionEvent actionEvent) {
        System.out.println("+ 10 секунд");
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(+10)));
    }
    @FXML
    private void backMedia(ActionEvent actionEvent) {
        System.out.println("- 10 секунд");
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
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

    //@FXML
    //private void resetMedia(ActionEvent event) {
        //System.out.println("Запуск файла с самого начала");
        //mediaPlayer.stop();
        //songSlider.setValue(0);
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

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songSlider.setValue(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
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
}