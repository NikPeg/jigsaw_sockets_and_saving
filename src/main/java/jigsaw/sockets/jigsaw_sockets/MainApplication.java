package jigsaw.sockets.jigsaw_sockets;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainApplication extends Application {

    private final int width = 700;
    private final int height = 700;
    public static Group root;
    private final UserLoginController controller;
    private IntegerProperty seconds;
    private Timeline timeline;

    public MainApplication(UserLoginController controller) {
        this.controller = controller;
        Figure.controller = controller;
    }

    @Override
    public void start(Stage stage) {

        root = new Group();
        Scene scene = prepareScene(stage, root);
        Canvas field = prepareField();

        seconds = new SimpleIntegerProperty(0);
        timeline = new Timeline(new KeyFrame(Duration.seconds(Constants.maxCount),
                new KeyValue(seconds, Constants.maxCount)));

        Button endGameButton = createEndButton();
        ButtonType cancel = new ButtonType("No, exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType tryAgain = new ButtonType("Try again", ButtonBar.ButtonData.OK_DONE);
        endGameButton.setOnAction(event -> {
            gameButtonPressed(stage, seconds, timeline, cancel, tryAgain);
        });

        Label timer = createTimer(stage, seconds, timeline, cancel, tryAgain);
        timeline.play();

        Label helloLabel = createHello();
        Label playersLabel = createPlayers();
        Label maxTimeLabel = createMaxTime();
        addAllOnRoot(stage, root, scene, field, endGameButton, timer, helloLabel, playersLabel, maxTimeLabel);
    }

    private <T extends Event> void closeWindowEvent(WindowEvent event) {
        endGame();
    }

    private void endGame() {
        ConnectionClient.send("BUY");
        controller.forcedGameStop(seconds.get(), "Closing");
    }

    private void gameButtonPressed(Stage stage, IntegerProperty seconds, Timeline timeline, ButtonType cancel, ButtonType tryAgain) {
        timeline.stop();
        controller.endGame(seconds.get());
        stage.close();
    }

    private Button createEndButton() {
        Button endGameButton = new Button();
        endGameButton.setText("End game");
        endGameButton.setLayoutX(Field.squareSize);
        endGameButton.setLayoutY(height - 87);
        endGameButton.setFont(Font.font(20));
        return endGameButton;
    }

    private Label createTimer(Stage stage, IntegerProperty seconds, Timeline timeline, ButtonType cancel, ButtonType tryAgain) {
        Label timer = new Label();
        timer.setLayoutX(250);
        timer.setLayoutY(50);
        timer.setFont(Font.font(20));
        timer.textProperty().bind(Bindings.createStringBinding(
                () -> getTimeString(stage, seconds, timeline, cancel, tryAgain), seconds)
        );
        return timer;
    }

    private String getTimeString(Stage stage, IntegerProperty seconds, Timeline timeline, ButtonType cancel, ButtonType tryAgain) {
        int res = seconds.get();
        String maxTime = ConnectionClient.getMaxTime();
        if (maxTime == null) {
            forcedGameStop(stage, "Server has stopped!");
        } else if (maxTime.equals("LAST")) {
            forcedGameStop(stage, "You are the last player! You've won!");
        }
        if (String.valueOf(res).equals(maxTime)) {
            gameButtonPressed(stage, seconds, timeline, cancel, tryAgain);
            return "";
        }
        return "Time: " + res + " seconds";
    }

    private Label createHello() {
        Label helloLabel = new Label("Hello, " + ConnectionClient.name + "!");
        helloLabel.setLayoutX(250);
        helloLabel.setLayoutY(10);
        helloLabel.setFont(Font.font(20));
        return helloLabel;
    }

    private Label createPlayers() {
        Label helloLabel = new Label("Players: " + ConnectionClient.getPlayers());
        helloLabel.setLayoutX(250);
        helloLabel.setLayoutY(30);
        helloLabel.setFont(Font.font(20));
        return helloLabel;
    }

    private Label createMaxTime() {
        Label helloLabel = new Label("Max playing time: " + ConnectionClient.getMaxTime());
        helloLabel.setLayoutX(250);
        helloLabel.setLayoutY(70);
        helloLabel.setFont(Font.font(20));
        return helloLabel;
    }

    private Canvas prepareField() {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Field.drawField(gc);
        return canvas;
    }

    private Scene prepareScene(Stage stage, Group root) {
        stage.setTitle("Jigsaw");
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.AZURE);
        return scene;
    }

    private void addAllOnRoot(Stage stage, Group root, Scene scene, Canvas canvas, Button btn, Label lbl1, Label lbl2,
                              Label lbl3, Label lbl4) {
        root.getChildren().add(canvas);
        root.getChildren().add(btn);
        root.getChildren().add(lbl1);
        root.getChildren().add(lbl2);
        root.getChildren().add(lbl3);
        root.getChildren().add(lbl4);
        Figure.showNext();
        stage.setScene(scene);
        stage.show();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private void forcedGameStop(Stage stage, String cause) {
        timeline.stop();
        int time = seconds.get();
        controller.forcedGameStop(time, cause);
        stage.close();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

