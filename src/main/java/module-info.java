module jigsaw.sockets.jigsaw_sockets {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;
    requires org.junit.jupiter.api;


    opens jigsaw.sockets.jigsaw_sockets to javafx.fxml;
    exports jigsaw.sockets.jigsaw_sockets;
}