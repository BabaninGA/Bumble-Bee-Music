package com.example.mp3player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 522);
        stage.setTitle("Bumble-Bee Music");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("file:src/resources/Main.png"));
    }

    public static void main(String[] args) {
        launch();
        SongTest test1 = new SongTest();
        test1.checkAll();
    }
}