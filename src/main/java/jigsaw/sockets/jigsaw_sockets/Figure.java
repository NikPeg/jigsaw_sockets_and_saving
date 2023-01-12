package jigsaw.sockets.jigsaw_sockets;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;
import java.util.Vector;

public class Figure {

    private static int figureIndex = 0;
    public final int squareSize;
    public final int figureType;
    private static final int figuresCount = 31;
    private static int mouseX, mouseY;
    private static final Vector<Figure> figures = new Vector<>();
    public static int defaultX = 45;
    public static int defaultY = 45;
    private Polygon polygon;
    private static int firstIndex = 0;
    public static UserLoginController controller;

    public Figure(int squareSize) {
        this.squareSize = squareSize;
        figureType = ConnectionClient.nextFigure();
    }

    private static void goBack(Polygon polygon) {
        polygon.setLayoutX(0);
        polygon.setLayoutY(0);
    }

    private static void onMouseReleased(Polygon figurePolygon, MouseEvent event) {
        double deltaX = figurePolygon.getLayoutX() - (figurePolygon.getLayoutX() - Field.leftUpperX + defaultX) % Field.squareSize;
        double deltaY = figurePolygon.getLayoutY() - (figurePolygon.getLayoutY() - Field.leftUpperY + defaultY) % Field.squareSize;
        if (event.getSceneX() < Field.leftUpperX || event.getSceneX() > Field.leftUpperX + Field.fieldSize) {
            goBack(figurePolygon);
        } else if (event.getSceneY() < Field.leftUpperY || event.getSceneY() > Field.leftUpperY + Field.fieldSize) {
            goBack(figurePolygon);
        } else if (!Field.isFree(deltaX, deltaY, figures.lastElement())) {
            goBack(figurePolygon);
        } else {
            figurePolygon.setLayoutX(deltaX);
            figurePolygon.setLayoutY(deltaY);
            Field.occupy(deltaX, deltaY, figures.lastElement());
            Figure.showNext();
            figurePolygon.setDisable(true);
        }
        event.consume();
    }

    private static void onMousePressed(Polygon figurePolygon, MouseEvent event) {
        mouseX = (int) event.getSceneX();
        mouseY = (int) event.getSceneY();
        event.consume();
    }

    private static void onMouseDragged(Polygon figurePolygon, MouseEvent event) {
        figurePolygon.setLayoutX(event.getSceneX() - mouseX);
        figurePolygon.setLayoutY(event.getSceneY() - mouseY);
        event.consume();
    }

    public static void showNext() {
        Figure figure = new Figure(Field.squareSize);
        figure.polygon = figure.generatePolygon();
        figure.polygon.setFill(Color.BLUE);
        figure.polygon.setOnMousePressed(event -> Figure.onMousePressed(figure.polygon, event));
        figure.polygon.setOnMouseDragged(event -> Figure.onMouseDragged(figure.polygon, event));
        figure.polygon.setOnMouseReleased(event -> onMouseReleased(figure.polygon, event));
        figures.add(figure);
        MainApplication.root.getChildren().add(figure.polygon);
    }

    public Polygon generatePolygon() {
        Polygon result = getPolygon();
        result.setFill(Color.BLUE);
        result.setStroke(Color.BLACK);
        result.setStrokeWidth(3);
        return result;
    }

    private Polygon getPolygon() {
        return switch (figureType) {
            case 0 -> new Polygon(defaultX, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 1 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize);
            case 2 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize);
            case 3 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 4 -> new Polygon(defaultX, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 5 -> new Polygon(defaultX + 2 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize);
            case 6 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 7 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize);
            case 8 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize);
            case 9 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize);
            case 10 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize);
            case 11 -> new Polygon(defaultX, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 12 -> new Polygon(defaultX + 2 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize);
            case 13 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 3 * squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 14 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 15 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + 3 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 16 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX + 3 * squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize);
            case 17 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 18 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 19 -> new Polygon(defaultX + 2 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + 3 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize);
            case 20 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 21 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 22 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 23 -> new Polygon(defaultX, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize);
            case 24 -> new Polygon(defaultX, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 25 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize);
            case 26 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize);
            case 27 -> new Polygon(defaultX, defaultY,
                    defaultX + squareSize, defaultY,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX, defaultY + 3 * squareSize);
            case 28 -> new Polygon(defaultX, defaultY,
                    defaultX + 3 * squareSize, defaultY,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 2 * squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX + squareSize, defaultY + squareSize,
                    defaultX, defaultY + squareSize);
            case 29 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 3 * squareSize,
                    defaultX + squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize);
            case 30 -> new Polygon(defaultX + squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY,
                    defaultX + 2 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + squareSize,
                    defaultX + 3 * squareSize, defaultY + 2 * squareSize,
                    defaultX, defaultY + 2 * squareSize,
                    defaultX, defaultY + squareSize,
                    defaultX + squareSize, defaultY + squareSize);
            default -> null;
        };
    }

    public static int getScore() {
        return figures.size() - 1;
    }

    public static Vector<Integer> generateSequence() {
        Vector<Integer> res = new Vector<>();
        Random rnd = new Random();
        for (int i = 0; i < Constants.maxCount; ++i) {
            res.add(rnd.nextInt(figuresCount));
        }
        return res;
    }

    public static void deleteAll() {
        figures.clear();
        Field.clear();
    }
}
