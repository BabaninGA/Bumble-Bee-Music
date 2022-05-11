package com.example.mp3player;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    private Timer timer;
    private TimerTask task;
    private boolean running;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;

    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider songSlider;
    @FXML
    private Label songLabel;
    @FXML
    private ScrollBar scrollBar;

    @FXML
    private void forwardMedia(ActionEvent actionEvent){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(+5)));
    }
    @FXML
    private void backMedia(ActionEvent actionEvent){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
    }

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
        System.out.println(name);
        name = name.replaceAll("%20"," ");

        if (filePath != null){
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            beginTimer();
            System.out.println(filePath);
            songLabel.setText(name);

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                    songSlider.setValue(newValue.toSeconds());
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
        }
    }

    @FXML
    private void playMedia() {
        beginTimer();
        mediaPlayer.play();
    }
    @FXML
    private void pauseMedia(ActionEvent event) {
        mediaPlayer.pause();
        cancelTimer();
    }
    @FXML
    private void resetMedia(ActionEvent event) {
        mediaPlayer.stop();
        songSlider.setValue(0);
        mediaPlayer.play();
    }

    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songSlider.setValue(current/end);

                volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                    }
                });
                if (current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }
}