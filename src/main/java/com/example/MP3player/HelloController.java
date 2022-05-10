package com.example.mp3player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

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
    private ProgressBar songProgressBar;
    @FXML
    private Label songLabel;

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
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            mediaPlayer.play();
            beginTimer();
            System.out.println(filePath);
            songLabel.setText(name);
        }


    }

    @FXML
    private void playMedia() {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    @FXML
    private Slider volumeSlider;

    @FXML
    private void resetMedia(ActionEvent event) {
        mediaPlayer.stop();
        songProgressBar.setProgress(0);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    @FXML
    private void pauseMedia(ActionEvent event) {
        mediaPlayer.pause();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        cancelTimer();
    }



    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songProgressBar.setProgress(current/end);

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