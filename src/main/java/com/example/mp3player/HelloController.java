package com.example.mp3player;


import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

import static org.apache.commons.io.FileUtils.copyFileToDirectory;


public class HelloController implements Initializable {

    private Timer timer;
    private TimerTask task;
    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean running = true;
    private boolean isMuted = false;
    private boolean isPlaying = true;
    private boolean wasPlaying = false;
    private boolean videoWasPlaying = false;
    private double volPerc = 10;
    private int prevVol;
    private int savedVol = 0;
    private double totalTime;
    private double currentTime;
    private int flag1 = 0;
    private boolean shuffle_on = false;
    private boolean prevShuff = false;
    private boolean repeat_on = false;
    private int songNumber;
    private static String current_playlist = "allTracks";
    private ArrayList<String> playlists = new ArrayList<String>();
    private ArrayList<String> playlist_names = new ArrayList<>();
    private File main_directory = new File("C:\\Playlists");

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
    private ImageView iconActiveShuffle;

    @FXML
    private MediaView mediaView;
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
    private Pane mediaPane;
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
    private Button switchtovideo;


    @FXML
    private void addMedia(ActionEvent event) throws IOException {
        try {
            System.out.println("Добавить файл");
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select mp3 or mp4", "*.mp3", "*.mp4");
            fileChooser.getExtensionFilters().add(filter);
            String filePath = fileChooser.showOpenDialog(null).toURI().toString();
            File f = new File(filePath);
            if (f.getName().matches("^[ A-Za-z0-9а-яёйА-ЯЁЙ!/<>?*@,.#()-]{1,40}[.mp3]$")) {
                System.out.println(filePath);
                String name = f.getName();
                name = name.replaceAll("%20", " ");
                name = name.replaceAll(".mp3", "");
                Boolean match = name.matches("^[ A-Za-z0-9а-яёйА-ЯЁЙ!/<>?*@,.#()-]{1,40} - [ A-Za-z0-9а-яёйА-ЯЁЙ!/<>?*@,.#()-]{1,40}$");
                if (match) {
                    String songparts[] = name.split(" - ");
                    String regex = " - " + songparts[1];
                    String sb = name.replaceAll(regex, "");
                    songAuthor.setText(sb);
                    songName.setText(songparts[1]);
                } else {
                    songAuthor.setVisible(false);
                    songName.setText(name);
                }
                current_playlist = "allTracks";

                String track = filePath.replaceAll("file:/", "");
                track = track.replaceAll("%20", " ");
                flag1 = 0;
                List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\allTracks.txt"), "utf-8");
                if (lines.size() != 0) {
                    for (int i = 0; i < lines.size(); i++) {
                        if (lines.get(i).equals(track)) {
                            flag1++;
                        }
                    }
                    if (flag1 == 0) {
                        FileWriter writer1 = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                        BufferedWriter bufferWriter = new BufferedWriter(writer1);
                        bufferWriter.write(track + "\n");
                        bufferWriter.close();
                    }
                } else {
                    FileWriter writer1 = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer1);
                    bufferWriter.write(track + "\n");
                    bufferWriter.close();
                }

                if (wasPlaying) {

                    mediaPlayer.stop();
                }


                if (filePath != null) {
                    Media media = new Media(filePath);
                    mediaPlayer = new MediaPlayer(media);
                    setIcons();
                    songLabel.setText(name);
                    songAnimatedLabel.setText(name + " ");
                    playMedia();
                    wasPlaying = true;


                    bottomMenu.setVisible(true);

                    hboxTime.getChildren().remove(labelRemainingTime);
                    hboxVolume.getChildren().remove(volumeSlider);
                    hboxVolume.getChildren().remove(labelVolume);
                    animatedLabel.getChildren().remove(songAnimatedLabel);
                    refreshSongs();
                    refreshPlaylists();
                }
            }
            else{
                if (wasPlaying) {

                    mediaPlayer.stop();
                }
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                playMedia();
                setIcons();
                String name = f.getName();
                name = name.replaceAll("%20", " ");
                name = name.replaceAll(".mp4", "");
                songLabel.setText(name);
                songAnimatedLabel.setText(name + " ");
                bottomMenu.setVisible(true);
                songList.setVisible(false);
                songList.setDisable(true);
                shuffleMedia.setDisable(true);
                previousSongButton.setDisable(true);
                nextSongButton.setDisable(true);
                videoWasPlaying = true;
                mediaView.setVisible(true);

                hboxTime.getChildren().remove(labelRemainingTime);
                hboxVolume.getChildren().remove(volumeSlider);
                hboxVolume.getChildren().remove(labelVolume);
                animatedLabel.getChildren().remove(songAnimatedLabel);
            }
        } catch (RuntimeException e) {
            System.out.println("incorrect input");
        }
    }


    @FXML
    void importDirectory(ActionEvent event) throws IOException {
        try {
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
                    if (lines.size() != 0) {
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).equals(trackfromplaylist)) {
                                flag1++;
                            }
                        }
                        if (flag1 == 0) {
                            FileWriter writerh = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writerh);
                            bufferWriter.write(trackfromplaylist + "\n");
                            bufferWriter.close();
                        }
                    } else {
                        FileWriter writerh = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                        BufferedWriter bufferWriter = new BufferedWriter(writerh);
                        bufferWriter.write(trackfromplaylist + "\n");
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
            refreshSongs();
            refreshPlaylists();
        } catch (RuntimeException e) {
            System.out.println("incorrect input");
        }
    }

    @FXML
    private void importPlaylist() throws IOException {
        try {
            System.out.println("Импортировать плейлист");
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select playlists in .txt", "*.txt");
            fileChooser.getExtensionFilters().add(filter);
            String filePath = fileChooser.showOpenDialog(null).toURI().toString();
            filePath = filePath.replaceAll("file:/", "");
            File f = new File(filePath);
            copyFileToDirectory(f, main_directory);
            List<String> parts = FileUtils.readLines(new File(filePath), "utf-8");
            List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\allTracks.txt"), "utf-8");
            for (int i = 0; i < parts.size(); i++) {
                String trackfromplaylist = parts.get(i);

                trackfromplaylist = trackfromplaylist.replaceAll("%20", " ");
                trackfromplaylist = trackfromplaylist.replace("\\", "/");
                if (lines.size() != 0) {
                    for (int g = 0; g < lines.size(); g++) {
                        if (lines.get(g).equals(trackfromplaylist)) {
                            flag1++;
                        } else {
                        }
                    }
                    if (flag1 == 0) {
                        FileWriter writerh = new FileWriter("C:\\Playlists\\allTracks.txt", true);
                        BufferedWriter bufferWriter = new BufferedWriter(writerh);
                        bufferWriter.write(trackfromplaylist + "\n");
                        bufferWriter.close();
                    }
                }
            }
            refreshSongs();
            refreshPlaylists();
        } catch (RuntimeException e) {
            System.out.println("incorrect input");
        }
    }

    @FXML
    private void exportPlaylist() throws IOException {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File playlist_export_dir = directoryChooser.showDialog(null);
            File f = new File(main_directory + "\\" + current_playlist + ".txt");
            System.out.println(f);

            copyFileToDirectory(f, playlist_export_dir);
        } catch (RuntimeException e) {
            System.out.println("incorrect output");
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
    private void resetMedia() {
        System.out.println("Запуск файла с самого начала");
        mediaPlayer.stop();
        songSlider.setValue(0);
        beginTimer();
        mediaPlayer.seek(Duration.seconds(0));
        playMedia();
    }

    private void forwardMedia() throws IOException {
        List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\" + current_playlist + ".txt"), "utf-8");
        if (shuffle_on == false) {
            if (songNumber < lines.size() - 1) {
                mediaPlayer.stop();
                mediaPlayer.seek(Duration.millis(0));
                songNumber++;
                playPlaylist();
                songList.getSelectionModel().select(songNumber);
            } else {
                mediaPlayer.stop();
                mediaPlayer.seek(Duration.millis(0));
                songNumber = 0;
                playPlaylist();
                songList.getSelectionModel().select(songNumber);
            }
        } else {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.millis(0));
            songNumber = getRandom(1, lines.size() - 1);
            songList.getSelectionModel().select(songNumber);
            playPlaylist();
        }
    }

    private void previousMedia() throws IOException {
        List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\" + current_playlist + ".txt"), "utf-8");
        if (shuffle_on == false) {
            if (songNumber > 0) {
                mediaPlayer.stop();
                mediaPlayer.seek(Duration.millis(0));
                songNumber--;
                playPlaylist();
            } else {
                mediaPlayer.stop();
                mediaPlayer.seek(Duration.millis(0));
                songNumber = lines.size() - 1;
                playPlaylist();
            }
        } else {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.millis(0));
            songNumber = getRandom(1, lines.size() - 1);
            songList.getSelectionModel().select(songNumber);
            playPlaylist();
        }
    }

    public static int getRandom(int min, int max) throws IOException {
        int x = (int) ((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    private void playMedia() throws NullPointerException {
        labelButtonPPR.setGraphic(iconPause);
        System.out.println("Воспроизведение");
        beginTimer();
        mediaPlayer.play();
        isPlaying = true;
        savepar();
        mediaplfer();
        wasPlaying = true;
    }

    private void pauseMedia() {
        labelButtonPPR.setGraphic(iconPlay);
        System.out.println("Пауза");
        mediaPlayer.pause();
        cancelTimer();
        isPlaying = false;
    }

    private void changeCurrentPlaylist(String new_name) {
        current_playlist = new_name;
    }

    private void savepar() {
        if (wasPlaying) {
            shuffle_on = prevShuff;
            volumeSlider.setValue(savedVol);
            mediaPlayer.setVolume(savedVol * 0.01);
            if (volPerc == 0) {
                volumeOff.setGraphic(iconMute);
            } else {
                volumeOff.setGraphic(iconVolume);
            }
            if (shuffle_on == true) {
                shuffleMedia.setGraphic(iconActiveShuffle);
            } else {
                shuffleMedia.setGraphic(iconShuffle);
            }
        } else {
            shuffle_on = false;
            volumeSlider.setValue(10.0);
            mediaPlayer.setVolume(10.0 * 0.01);
            savedVol = 10;
            if (volPerc == 0) {
                volumeOff.setGraphic(iconMute);
            } else {
                volumeOff.setGraphic(iconVolume);
            }
        }
    }

    private void playPlaylist() throws IOException {
        List<String> lines = FileUtils.readLines(new File("C:\\Playlists\\" + current_playlist + ".txt"), "utf-8");
        String filePath = lines.get(songNumber);
        File f = new File(filePath);
        String name = f.getName();
        URI filepath = URI.create(("file:/" + filePath).replaceAll(" ", "%20"));
        media = new Media(filepath.toString());
        name = name.replaceAll("%20", " ");
        name = name.replaceAll(".mp3", "");
        Boolean match = name.matches("^[ A-Za-z0-9а-яёйА-ЯЁЙ!/<>?*@,.#()-]{1,40} - [ A-Za-z0-9а-яёйА-ЯЁЙ!/<>?*@,.#()-]{1,40}$");
        if (match) {
            String songparts[] = name.split(" - ");
            String regex = " - " + songparts[1];
            String sb = name.replaceAll(regex, "");
            songAuthor.setVisible(true);
            songAuthor.setText(sb);
            songName.setText(songparts[1]);
        } else {
            songAuthor.setVisible(false);
            songName.setText(name);
        }
        mediaPlayer = new MediaPlayer(media);
        setIcons();
        bottomMenu.setVisible(true);
        songLabel.setText(name);
        songAnimatedLabel.setText(name + " ");
        hboxTime.getChildren().remove(labelRemainingTime);
        hboxVolume.getChildren().remove(volumeSlider);
        hboxVolume.getChildren().remove(labelVolume);
        animatedLabel.getChildren().remove(songAnimatedLabel);
        playMedia();
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
            }
        };
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();
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

        Image imageActiveShuffle = new Image(new File("src/resources/active-shuffle-btn.png").toURI().toString());
        iconActiveShuffle = new ImageView(imageActiveShuffle);
        iconActiveShuffle.setFitWidth(22);
        iconActiveShuffle.setFitHeight(22);

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
                playlistList.getSelectionModel().getSelectedItem();
                try {
                    refreshSongs();
                    refreshPlaylists();
                    bottomMenu.setVisible(true);
                    songList.setVisible(true);
                    songList.setDisable(false);
                    shuffleMedia.setDisable(false);
                    previousSongButton.setDisable(false);
                    nextSongButton.setDisable(false);
                    if (videoWasPlaying){
                        mediaView.setVisible(false);
                    }
                    if (isPlaying) {

                        mediaPlayer.stop();
                    }
                    songAuthor.setVisible(true);
                    songName.setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        nextSongButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Следующий трек");
                try {
                    forwardMedia();
                    songList.getSelectionModel().select(songNumber);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        previousSongButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Предыдущий трек");
                try {
                    previousMedia();
                    songList.getSelectionModel().select(songNumber);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        shuffleMedia.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (shuffle_on) {
                    shuffle_on = false;
                    System.out.println("Шафл выключен");
                    shuffleMedia.setGraphic(iconShuffle);
                } else {
                    shuffle_on = true;
                    System.out.println("Шафл включен");
                    shuffleMedia.setGraphic(iconActiveShuffle);
                }
                prevShuff = shuffle_on;
            }
        });
        songList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    if (wasPlaying) {
                        mediaPlayer.stop();
                    }
                    songNumber = songList.getSelectionModel().getSelectedIndex();
                    try {
                        playPlaylist();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }



    public void mediaplfer() throws NullPointerException {
        if (isPlaying) {
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total = media.getDuration();
                    songSlider.setMax(total.toSeconds());
                }
            });
            mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                    songAuthor.setVisible(true);
                    songName.setVisible(true);
                    songSlider.setMax(newDuration.toSeconds());
                    labelTotalTime.setText(getTime(newDuration));

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
                        try {
                            forwardMedia();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    String remaining = getTimeString(mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis());
                    labelRemainingTime.setText(remaining);
                    labelCurrentTime.getText();
                    labelTotalTime.getText();
                }
            });
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                    String currentlabel = songAnimatedLabel.getText();
                    String parts[] = currentlabel.split("");
                    String changedlabel = currentlabel.substring(1, currentlabel.length()) + parts[0];
                    songAnimatedLabel.setText(changedlabel);
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
                    savedVol = result;
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
}