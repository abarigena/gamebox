package hockey.server;

import hockey.Ball;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameServer {
    private ServerSocket serverSocket;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2Socket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    private int p1x, p1y, p2x, p2y;
    private Ball ball;
    private int p1Score, p2Score;

    public GameServer(int port){
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;

        p1x = 100;
        p1y = 250;

        p2x = 680;
        p2y = 250;

        ball = new Ball(390, 290, 20, Color.BLACK);

        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error initializing server socket");
        }

    }

    public void acceptConnections (){
        try{
            System.out.println("waiting for connections");

            while (numPlayers < maxPlayers) {
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player " + numPlayers + " has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if (numPlayers == 1) {
                    p1Socket = socket;
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;
                } else {
                    p2Socket = socket;
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();
                    new Thread(p1ReadRunnable).start();
                    new Thread(p2ReadRunnable).start();
                    new Thread(p1WriteRunnable).start();
                    new Thread(p2WriteRunnable).start();
                    Timer timer = new Timer(true);
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            updateGameState();
                        }
                    }, 0, 30);
                }
            }

            System.out.println("No longer accepting connections");
        } catch (IOException e) {
            System.out.println("Error accepting connections");
        }
    }

    private void updateGameState() {
        ball.move();

        // Проверяем коллизии с игроками
        if (ball.getBounds().intersects(new Rectangle(p1x, p1y, 20, 100))) {
            ball.reverseXVelocity();
            ball.increaseVelocity();
        }

        if (ball.getBounds().intersects(new Rectangle(p2x, p2y, 20, 100))) {
            ball.reverseXVelocity();
            ball.increaseVelocity();
        }

        checkGoals();
    }
    private void checkGoals() {
        if (ball.getX() <= 30) {
            p2Score++;
            resetBall();
        } else if (ball.getX() >= 770) {
            p1Score++;
            resetBall();
        }
    }
    private void resetBall() {
        ball.setPosition(390, 290);
        ball.resetVelocity();
    }

    private class ReadFromClient implements Runnable {
        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int pid, DataInputStream in) {
            playerID = pid;
            dataIn = in;
            System.out.println("RFC " + playerID + " Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (playerID == 1) {
                        p1x = dataIn.readInt();
                        p1y = dataIn.readInt();
                    } else {
                        p2x = dataIn.readInt();
                        p2y = dataIn.readInt();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading from client");
            }
        }
    }

    private class WriteToClient implements Runnable {
        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int pid, DataOutputStream out) {
            playerID = pid;
            dataOut = out;
            System.out.println("WTC " + playerID + " Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (playerID == 1) {
                        dataOut.writeInt(p2x);
                        dataOut.writeInt(p2y);
                    } else {
                        dataOut.writeInt(p1x);
                        dataOut.writeInt(p1y);
                    }

                    // Отправляем координаты мяча обоим игрокам
                    dataOut.writeInt(ball.getX());
                    dataOut.writeInt(ball.getY());
                    dataOut.writeInt(p1Score);
                    dataOut.writeInt(p2Score);

                    dataOut.flush();

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("Error in WTC run() sleep");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error writing to client");
            }
        }

        public void sendStartMsg() {
            try {
                dataOut.writeUTF("We now have 2 players");
            } catch (IOException e) {
                System.out.println("Error sending start message");
            }
        }
    }
}
