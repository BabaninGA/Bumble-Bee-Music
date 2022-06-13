package com.example.mp3player;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

//Запуск приложения, завершение работы приложения и настройка параметров запуска приложения
public class HelloApplication extends Application {

    //Настройка параметров запуска приложения
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 768, 522);
        stage.setTitle("Bumble-Bee Music");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("file:src/resources/Main.png"));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            //Завершение работы приложения
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    //Непосредственно запуск приложения
    public static void main(String[] args) {
        launch();
    }
}