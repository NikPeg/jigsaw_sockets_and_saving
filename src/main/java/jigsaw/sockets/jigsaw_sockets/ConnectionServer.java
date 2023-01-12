package jigsaw.sockets.jigsaw_sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

/**
 * TODO: explain - how this program works... and why it should be started first :-)
 *  show - how to start both applications in one computer (communicating via localHost)...
 *
 */
public class ConnectionServer implements Runnable {
    public static int playersCount;
    public static int maxTime;
    protected String players = "";
    protected int readyPlayers = 0;
    protected int endPlayers = 0;
    protected int winnerScore = 0;
    protected int winnerTime = 2147483647;
    protected String winner;
    protected int disconnectedPlayers = 0;

    public void run() {
        int serverPort = 3456;
        Vector<Integer> figuresTypes = Figure.generateSequence();
        try {
            System.out.println("MultithreadedEchoServer: awaiting clients on port " + serverPort + " ...");
            ServerSocket s = new ServerSocket(serverPort);
            for (int i = 0; i < playersCount; ++i) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                ThreadedPlayerHandler r = new ThreadedPlayerHandler(incoming, figuresTypes, this);
                Thread t = new Thread(r);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ThreadedPlayerHandler extends ConnectionServer {

    private final Socket incoming;
    private Vector<Integer> figuresTypes;
    private int currentFigure;
    private ConnectionServer server;
    private Scanner in;
    private PrintWriter out;
    private String name;

    ThreadedPlayerHandler(Socket socket, Vector<Integer> figuresTypes, ConnectionServer server) {
        incoming = socket;
        this.figuresTypes = figuresTypes;
        currentFigure = 0;
        this.server = server;
    }

    public void run() {
        try {
            try {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                in = new Scanner(inStream);
                out = new PrintWriter(outStream, true /* autoFlush */);

                name = in.nextLine();
                System.out.println("line = " + name);
                server.readyPlayers++;
                server.players += name + " ";

                boolean done = false;
                while (!done && in.hasNextLine()) {
                    String line = in.nextLine();
                    out.println(handle(line.trim()));
                    System.out.println("line = " + line);
                    if (line.trim().equals("BYE"))
                        done = true;
                }
            } finally {
                incoming.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handle(String request) {
        return switch (request) {
            case "NEXT" -> figuresTypes.get(currentFigure++).toString();
            case "READY" -> String.valueOf((server.readyPlayers == playersCount));
            case "PLAYERS" -> server.players;
            case "TIME" -> getMaxTime();
            case "END" -> updateWinner();
            case "WINNER" -> getWinner();
            case "AGAIN" -> startAgain();
            case "BUY" -> handleDisconnected();
            default -> "BUY";
        };
    }

    private String getMaxTime() {
        if (ConnectionServer.playersCount > 1 && server.disconnectedPlayers == ConnectionServer.playersCount - 1) {
            return "LAST";
        }
        return String.valueOf(ConnectionServer.maxTime);
    }

    private String handleDisconnected() {
        server.disconnectedPlayers++;
        return "BUY";
    }

    private String startAgain() {
        if (server.readyPlayers == ConnectionServer.playersCount) {
            server.readyPlayers = 0;
        }
        server.readyPlayers++;
        server.endPlayers = 0;
        server.winnerScore = 0;
        server.winnerTime = 2147483647;
        server.winner = null;
        server.disconnectedPlayers = 0;
        return "OK";
    }

    private String getWinner() {
        if (server.endPlayers == ConnectionServer.playersCount) {
            return server.winner;
        }
        return "WAITING";
    }

    private String updateWinner() {
        int score = Integer.parseInt(in.nextLine());
        int time = Integer.parseInt(in.nextLine());
        server.endPlayers++;
        if (score > server.winnerScore) {
            server.winnerScore = score;
            server.winnerTime = time;
            server.winner = name;
        } else if (score == server.winnerScore && time < server.winnerTime) {
            winnerTime = time;
            server.winner = name;
        }
        return "OK";
    }
}