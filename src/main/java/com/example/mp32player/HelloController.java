package com.example.mp32player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;

import java.io.File;
import java.io.IOException;

public class HelloController {

    @FXML
    private Button AddFile;
    @FXML
    void onAddFile(ActionEvent event) {
        AddFile.setOnAction(event1 -> System.out.println("добавить"));
    }

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField search;

    @FXML
    private Button searchButton;

    @FXML
    private Button Play;
    @FXML
    void onPlay(ActionEvent event) {
        Play.setOnAction(event2 -> System.out.println("вкл/выкл"));
    }

    @FXML
    private ScrollBar Scroll;

    @FXML
    private ProgressIndicator volume;

    @FXML
    void onSearchAsClick(ActionEvent event) {
        searchButton.setOnAction(event3 -> System.out.println("Поиск"));
        System.out.println("Поиск " + search.getText());
    }

    private boolean search() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Поиск...");
        File file = chooser.showSaveDialog(null);
        file.createNewFile();

        System.out.println("Поиск: " + file.getAbsolutePath());
        System.out.println("Поиск: " + searchButton.getText());
        return true;
    }
}
