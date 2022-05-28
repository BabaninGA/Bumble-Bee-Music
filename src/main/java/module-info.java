module com.example.mp3player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.apache.commons.io;
    requires lizzy;


    opens com.example.mp3player to javafx.fxml;
    exports com.example.mp3player;
}