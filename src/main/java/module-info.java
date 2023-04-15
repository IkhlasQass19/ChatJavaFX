module com.example.chatjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatjavafx to javafx.fxml;
    exports com.example.chatjavafx;
    exports com.example.chatjavafx.Server;
    opens com.example.chatjavafx.Server to javafx.fxml;
}