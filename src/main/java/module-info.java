module com.example.mp32player {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mp32player to javafx.fxml;
    exports com.example.mp32player;
}