package jigsaw.sockets.jigsaw_sockets;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerController {
    public static Thread serverThread;

    @FXML
    private Label readyText;

    @FXML
    protected void onStartButtonClick() {
        try {
            ConnectionServer.playersCount = Integer.parseInt(playersCount.getText());
            ConnectionServer.maxTime = Integer.parseInt(timeField.getText());
        }
        catch (NumberFormatException e) {
            readyText.setText("Type correct values!");
            return;
        }
        if (ConnectionServer.playersCount <= 0) {
            readyText.setText("Type correct values!");
            return;
        }
        if (ConnectionServer.maxTime <= 0) {
            readyText.setText("Type correct values!");
            return;
        }
        Runnable r = new ConnectionServer();
        serverThread = new Thread(r);
        serverThread.start();
        playersText.setText("Server has been started!");
        playersCount.setVisible(false);
        timeText.setVisible(false);
        timeField.setVisible(false);
        startButton.setVisible(false);
    }

    @FXML
    private Label playersText;

    @FXML
    private TextField playersCount;

    @FXML
    private Label timeText;

    @FXML
    private TextField timeField;

    @FXML
    private Button startButton;
}