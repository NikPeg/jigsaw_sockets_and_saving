package jigsaw.sockets.jigsaw_sockets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UserLoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("user-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("User settings");
        scene.setFill(Color.AZURE);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        ConnectionClient.send("BUY");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}