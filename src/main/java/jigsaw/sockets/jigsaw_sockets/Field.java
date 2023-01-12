package jigsaw.sockets.jigsaw_sockets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Field {
    public static int leftUpperX = 250;
    public static int leftUpperY = 250;
    public static int squareSize = 44;
    public static int fieldSize = squareSize * 9;
    private static boolean[][] occupied = new boolean[9][9];

    public static void drawField(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);
        for (int i = 0; i < 10; ++i) {
            gc.strokeLine(leftUpperX + squareSize * i, leftUpperY, leftUpperX + squareSize * i, leftUpperY + fieldSize);
        }
        for (int i = 0; i < 10; ++i) {
            gc.strokeLine(leftUpperX, leftUpperY + squareSize * i, leftUpperX + fieldSize, leftUpperY + squareSize * i);
        }
    }

    public static boolean isFree(double deltaX, double deltaY, Figure figure) {
        int squareX = (int) (Figure.defaultX + deltaX - leftUpperX) / squareSize;
        int squareY = (int) (Figure.defaultY + deltaY - leftUpperY) / squareSize;
        for (int[] pair : Constants.figuresSquares[figure.figureType]) {
            if (occupied[squareX + pair[0]][squareY + pair[1]]) {
                return false;
            }
        }
        return true;
    }

    public static void occupy(double deltaX, double deltaY, Figure figure) {
        int squareX = (int) (Figure.defaultX + deltaX - leftUpperX) / squareSize;
        int squareY = (int) (Figure.defaultY + deltaY - leftUpperY) / squareSize;
        for (int[] pair : Constants.figuresSquares[figure.figureType]) {
            occupied[squareX + pair[0]][squareY + pair[1]] = true;
        }

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                System.out.print((occupied[x][y] ? 1 : 0) + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void clear() {
        for (int x = 0; x < 9; ++x) {
            for (int y = 0; y < 9; ++y) {
                occupied[x][y] = false;
            }
        }
    }
}
