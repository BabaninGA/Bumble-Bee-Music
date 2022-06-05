package com.example.mp3player;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.File;


public class Icons {

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


    public static void setIcons(){
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
}
