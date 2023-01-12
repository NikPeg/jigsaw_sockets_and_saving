package jigsaw.sockets.jigsaw_sockets;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Server settings");
        scene.setFill(Color.AZURE);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        if (ServerController.serverThread != null) {
            ServerController.serverThread.interrupt();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}