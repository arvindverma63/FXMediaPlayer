module com.example.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mediaplayer to javafx.fxml;
    exports com.example.mediaplayer;
}