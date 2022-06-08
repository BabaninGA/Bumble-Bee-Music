package com.example.mp3player;


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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

import static org.apache.commons.io.FileUtils.copyDirectoryToDirectory;
import static org.apache.commons.io.FileUtils.copyFileToDirectory;


public class HelloController implements Initializable {

    private Timer timer;
    private TimerTask task;
    private MediaPlayer mediaPlayer;

    private Media media;
    private boolean running;
    private boolean isMuted = false;
    private boolean isPlaying = true;

    private boolean wasPlaying = false;
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

    private int flag1 = 0;
    private boolean active_track = false;
    private boolean shuffle_on = false;
    private boolean repeat_on = false;
    private String current_playlist = "allTracks";
    private ArrayList<String> playlists = new ArrayList<String>();
    private ArrayList<String> playlist_names = new ArrayList<>();
    private File main_directory = new File("C:\\Playlists");

    @FXML
    private Label shuffleMedia;
    @FXML
    private Button openMedia;
    @FXML
    private Button importDirectory;
    @FXML
    private Button importPlaylist;
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
    private Label songAnimatedLabel;
    @FXML
    private HBox hboxTime;
    @FXML
    private HBox hboxVolume;
    @FXML
    private HBox animatedLabel;
    @FXML
    private Label nextSongButton;
    @FXML
    private Label previousSongButton;
    @FXML
    private ListView<String> playlistList;
    @FXML
    private ListView<String> songList;
    @FXML
    private Label songName;
    @FXML
    private Label songAuthor;
    @FXML
    private HBox songHbox;


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
        current_playlist = "allTracks";

        String track = filePath.replaceAll("file:/", "");
        track = track.replaceAll("%20", " ");
        System.out.println(track);
        flag1 = 0;
        List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\allTracks.txt"), "utf-8");
        System.out.println(lines.size());
        if (lines.size() != 0) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i) == track) {
                    flag1++;
                } else {
                }
            }
            if (flag1 == 0) {
                FileWriter writer1 = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                BufferedWriter bufferWriter = new BufferedWriter(writer1);
                bufferWriter.write(track + "\n");
                bufferWriter.close();
            }
        }
        else{
            FileWriter writer1 = new FileWriter("C:\\Playlists\\allTracks.txt", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer1);
            bufferWriter.write(track + "\n");
            bufferWriter.close();
        }

        if (wasPlaying == true) {

            mediaPlayer.stop();
        }


        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            setIcons();
            volumeSlider.setValue(10.0);
            mediaPlayer.setVolume(10.0 * 0.01);
            songLabel.setText(name);
            songAnimatedLabel.setText(name + " ");
            playMedia();
            wasPlaying = true;


            bottomMenu.setVisible(true);

            hboxTime.getChildren().remove(labelRemainingTime);
            hboxVolume.getChildren().remove(volumeSlider);
            hboxVolume.getChildren().remove(labelVolume);
            animatedLabel.getChildren().remove(songAnimatedLabel);
        }

    }

    @FXML
    void importDirectory(ActionEvent event) throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File playlist_import_dir = directoryChooser.showDialog(null);
        while (Objects.requireNonNull(playlist_import_dir.listFiles()).length == 0) {
            playlist_import_dir = directoryChooser.showDialog(null);
        }
        File dir = new File(playlist_import_dir.toURI());
        List<File> lst = new ArrayList<>();
        flag1 = 0;
        String import_dir = null;
        for (File file : dir.listFiles()) {
            if ((file.isFile()) & (file.toString().endsWith("mp3"))) {
                lst.add(file);
                String trackfromplaylist = file.toString();
                trackfromplaylist = trackfromplaylist.replaceAll("%20", " ");
                trackfromplaylist = trackfromplaylist.replace("\\", "/");
                List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\allTracks.txt"), "utf-8");
                for (int i = 1; i < lines.size(); i++) {
                    if (lines.get(i) == file.toString()) {
                        flag1++;
                    } else {
                        flag1 = flag1;
                    }
                }
                if (flag1 == 0) {
                    FileWriter writer1 = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer1);
                    bufferWriter.write((trackfromplaylist + "\n"));
                    bufferWriter.close();
                }
                String directory = playlist_import_dir.toString().replace("\\", "/");
                ArrayList array = new ArrayList<String>(List.of(directory.split("/")));
                import_dir = (String) array.get(array.size() - 1);
                FileWriter writer2 = new FileWriter("C:\\Playlists\\" + import_dir + ".txt", true);
                BufferedWriter bufferWriter1 = new BufferedWriter(writer2);
                bufferWriter1.write(trackfromplaylist + "\n");
                bufferWriter1.close();
                File createFile = new File("C:\\Playlists\\" + import_dir + ".txt");
                if (!createFile.exists())
                    try {
                        createFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
        current_playlist = import_dir;
        System.out.println(current_playlist);
        refreshPlaylists();
        refreshSongs();
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
    private void importPlaylist() throws IOException {
        System.out.println("Импортировать плейлист");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select playlists in .txt", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        String filePath = fileChooser.showOpenDialog(null).toURI().toString();
        filePath = filePath.replaceAll("file:/", "");
//        filePath = filePath.replaceAll("\\", "/");
        File f = new File(filePath);
        System.out.println(f);
        copyFileToDirectory(f, main_directory);
        refreshPlaylists();
        refreshSongs();
    }

    @FXML
    private void exportPlaylist() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File playlist_export_dir = directoryChooser.showDialog(null);
        File f = new File(main_directory + "\\" + current_playlist + ".txt");
        System.out.println(f);
        copyFileToDirectory(f, playlist_export_dir);
    }

    @FXML
    private void resetMedia() {
        System.out.println("Запуск файла с самого начала");
        mediaPlayer.stop();
        songSlider.setValue(0);
        beginTimer();
        mediaPlayer.seek(Duration.seconds(0));
        playMedia();
    }


    private void playMedia() {
        labelButtonPPR.setGraphic(iconPause);
        System.out.println("Воспроизведение");
        beginTimer();
        mediaPlayer.play();
        mediaplfer();
        isPlaying = true;
    }

    private void pauseMedia() {
        labelButtonPPR.setGraphic(iconPlay);
        System.out.println("Пауза");
        mediaPlayer.pause();
        cancelTimer();
        isPlaying = false;
    }


    private void setIcons() {
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

    public void mediaplfer() {
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total = media.getDuration();
                songSlider.setMax(total.toSeconds());
            }
        });


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
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                if (!songSlider.isValueChanging()) {
                    songSlider.setValue(newTime.toSeconds());
                }
                if (current / end > 0.999) {
                    labelButtonPPR.setGraphic(iconReset);
                }
                String remaining = getTimeString(mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis());
                labelRemainingTime.setText(remaining);
                labelCurrentTime.getText();
                labelTotalTime.getText();
            }
        });
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                String currentlabel = songAnimatedLabel.getText();
                String parts[] = currentlabel.split("");
                String changedlabel = currentlabel.substring(1, currentlabel.length()) + parts[0];
                songAnimatedLabel.setText(changedlabel);
            }
        });
        mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                songSlider.setMax(newDuration.toSeconds());
                labelTotalTime.setText(getTime(newDuration));

            }
        });
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

    private void changeCurrentPlaylist(String new_name) {
        current_playlist = new_name;
    }

    private void refreshPlaylists() {
        int playlist_amount = Objects.requireNonNull(main_directory.listFiles()).length;
        if (playlist_amount > 0) {
            playlists.clear();
            playlist_names.clear();
            playlistList.getItems().clear();
            for (File f : Objects.requireNonNull(main_directory.listFiles())) {
                playlists.add(f.getName());
                playlist_names.add(f.getName().replaceAll(".txt", ""));
            }

            playlistList.getItems().addAll(playlist_names);
        }
    }

    private void refreshSongs() throws IOException {
        Scanner s = new Scanner(new File("C:\\Playlists\\" + current_playlist + ".txt"));
        ArrayList<String> lines = new ArrayList<String>();
        int length = 0;
        while (s.hasNextLine()) {
            String G[] = s.nextLine().split("/");
            int glength = G.length - 1;
            lines.add(G[glength].replaceAll(".mp3", ""));
            length++;
        }
        s.close();
        System.out.println("Текущий плейлист - " + current_playlist);
        songList.getItems().clear();
        songList.getItems().addAll(lines);
    }
    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);

        return m + ":" + s;
    }

    public static String formatTime(double time) {
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t);
        }
        return "0" + t;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File createFile = new File("C:\\Playlists\\allTracks.txt");
        if (!createFile.exists())
            try {
                createFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        if (!main_directory.exists()) {
            main_directory.mkdir();
        }
        refreshPlaylists();
        try {
            refreshSongs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        playlistList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changeCurrentPlaylist(newValue);
                try {
                    refreshSongs();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        animatedLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                animatedLabel.getChildren().add(songAnimatedLabel);
                songHbox.getChildren().remove(songLabel);
            }
        });

        animatedLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                animatedLabel.getChildren().remove(songAnimatedLabel);
                songHbox.getChildren().add(songLabel);
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

        hboxTime.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hboxTime.getChildren().add(labelRemainingTime);
                totalTime = mediaPlayer.getCurrentTime().toMinutes();
                currentTime = mediaPlayer.getTotalDuration().toMinutes();
            }
        });

        hboxTime.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hboxTime.getChildren().remove(labelRemainingTime);
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
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                if ((current / end) > 0.999) {
                    resetMedia();
                }
            }
        });
    }



}
