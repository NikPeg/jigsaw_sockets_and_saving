package jigsaw.sockets.jigsaw_sockets;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *  TODO: explain - how this program works... try to start it first...
 *
 */
public class ConnectionClient {

    private static Socket socket = null;
    private static PrintWriter out = null;
    private static Scanner in = null;
    public static boolean connected = false;
    public static String name = null;
    public static String winner;

    public static void connect(String[] args) throws IOException {
        String serverHost = "localHost";
        int serverPort = 3456;
        if (args.length > 1) {
            serverHost = args[0];
            serverPort = Integer.parseInt(args[1]);
        }
        System.out.println("Client: started...");
        socket = new Socket(serverHost, serverPort);
        System.out.println("Client: socket created...");

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
        connected = true;
    }

    public static void closeConnection() throws IOException {
        out.close();
        in.close();
        socket.close();
        connected = false;
    }

    public static void send(String text) {
        out.println(text);
    }

    public static int nextFigure() {
        for (int i = 0; i < 10; ++i) {
            try {
                send("NEXT");
                while (!in.hasNextLine()) {
                    Thread.sleep(50);
                }
                return Integer.parseInt(in.nextLine());
            }
            catch (InterruptedException e) {
                System.out.println("Problem in current thread. Try again...");
            }
        }
        try {
            System.out.println("Server isn't working. Buy!");
            ConnectionClient.closeConnection();
        }
        catch (IOException e) {
            System.out.println("Can't close connection.");
        }
        return 0;
    }

    public static boolean askForPlayers() {
        try {
            send("READY");
            while (!in.hasNextLine()) {
                Thread.sleep(50);
            }
            return Boolean.parseBoolean(in.nextLine());
        }
        catch (InterruptedException e) {
            System.out.println("Problem in current thread. Try again...");
        }
        try {
            System.out.println("Server isn't working. Buy!");
            ConnectionClient.closeConnection();
        }
        catch (IOException e) {
            System.out.println("Can't close connection.");
        }
        return false;
    }

    public static String getPlayers() {
        send("PLAYERS");
        return in.nextLine();
    }

    public static String getMaxTime() {
        send("TIME");
        if (!in.hasNextLine()) {
            return null;
        }
        return in.nextLine();
    }

    public static void endGame(int score, int seconds) {
        send("END");
        out.println(score);
        out.println(seconds);
        in.nextLine();
    }

    public static boolean askForWinner() {
        send("WINNER");
        winner = in.nextLine().trim();
        return !winner.equals("WAITING");
    }

    public static void tryAgain() {
        send("AGAIN");
        in.nextLine();
        winner = null;
        Figure.deleteAll();
    }
}
