package jigsaw.sockets.jigsaw_sockets;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private Label hostText;

    @FXML
    private TextField hostField;

    @FXML
    private Button startButton;

    @FXML
    private TextField portField;

    @FXML
    private Label portText;

    @FXML
    protected void onStartButtonClick(ActionEvent actionEvent) {
        if (!ConnectionClient.connected) {
            try {
                ConnectionClient.connect(new String[]{hostField.getText(), portField.getText()});
                hostText.setText("Enter your name:");
                startButton.setText("Start game!");
                portText.setVisible(false);
                portField.setVisible(false);
                hostField.setText("");
            } catch (IOException e) {
                startButton.setText("Try again");
            }
        } else if (hostField.getText().trim().equals("")) {
            hostText.setText("Enter your REAL name:");
        } else {
            ConnectionClient.send(hostField.getText());
            ConnectionClient.name = hostField.getText().trim();
            startButton.setVisible(false);
            hostField.setVisible(false);
            portText.setVisible(false);
            hostText.setText("Waiting for all players...");
            Runnable r = new Waiter(ConnectionClient::askForPlayers, this::startGame);
            Thread t = new Thread(r);
            t.start();
        }
    }

    public void startGame() {
        hostText.setText("Good luck!");
        Platform.runLater(() -> new MainApplication(this).start(new Stage()));
    }

    public void endGame(int time) {
        hostText.setText("Your score is " + Figure.getScore() + " your time is " + time + ". Great!");
        portText.setText("Waiting for other players...");
        portText.setVisible(true);
        ConnectionClient.endGame(Figure.getScore(), time);
        Runnable r = new Waiter(ConnectionClient::askForWinner, this::setWinner);
        Thread t = new Thread(r);
        t.start();
    }

    private void setWinner() {
        portText.setText("Winner is " + ConnectionClient.winner);
        startButton.setText("Try again");
        startButton.setVisible(true);
        startButton.setOnAction(this::restartButtonClick);
    }

    private void restartButtonClick(ActionEvent actionEvent) {
        ConnectionClient.tryAgain();
        startButton.setVisible(false);
        hostField.setVisible(false);
        portText.setVisible(false);
        hostText.setText("Waiting for all players...");
        Runnable r = new Waiter(ConnectionClient::askForPlayers, this::startGame);
        Thread t = new Thread(r);
        t.start();
    }

    public void forcedGameStop(int time, String cause) {
        if (cause.equals("Closing")) {
            System.exit(0);
        }
        hostText.setText("Your score is " + Figure.getScore() + " your time is " + time + ". Great!");
        portText.setText(cause);
        portText.setVisible(true);
        startButton.setText("Try again");
        startButton.setVisible(true);
        startButton.setOnAction(this::restartApplication);
    }

    private void restartApplication(ActionEvent actionEvent) {
        ConnectionClient.connected = false;
        hostText.setText("Enter server's host:");
        hostField.setText("localhost");
        hostField.setVisible(true);
        portText.setText("Enter server's port:");
        portField.setVisible(true);
        startButton.setText("Connect to server");
        startButton.setOnAction(this::onStartButtonClick);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        ConnectionClient.send("BUY");
        Platform.exit();
    }
}