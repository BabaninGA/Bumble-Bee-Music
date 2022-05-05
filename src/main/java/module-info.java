module com.example.mp32player {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.MP3player to javafx.fxml;
    exports com.example.MP3player;
}