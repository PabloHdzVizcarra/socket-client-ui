module com.example.socketclientui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    
    requires org.kordamp.bootstrapfx.core;
    requires socket.io.client;
    requires json;
    requires engine.io.client;
    
    opens com.example.socketclientui to javafx.fxml;
    exports com.example.socketclientui;
}