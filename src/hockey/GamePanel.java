package hockey;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GamePanel extends JPanel implements Runnable {
    private Player player1;
    private Player player2;
    private int p1Score, p2Score;

    private Ball ball;

    private KeyHandler keyHandler;
    private Thread gameThread;
    private Socket socket;
    public static int playerID;



    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public GamePanel(int port) {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        setFocusable(true);

        // Создаем игроков и мяч
        ball = new Ball(390, 290, 20, Color.BLACK);
        createPlayers();

        // Подключаемся к серверу
        connectToServer(port);

        // Запускаем игровой поток
        startGameThread();
    }

    public void createPlayers() {
        if (playerID == 1) {
            player1 = new Player(100, 250, 20, 100, Color.BLUE);
            player2 = new Player(680, 250, 20, 100, Color.RED);
        } else {
            player1 = new Player(680, 250, 20, 100, Color.RED);
            player2 = new Player(100, 250, 20, 100, Color.BLUE);
        }
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (keyHandler.isWPressed()) {
            player1.moveUp();
        }
        if (keyHandler.isSPressed()) {
            player1.moveDown();
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawField(g);
        player1.draw(g);
        player2.draw(g);
        ball.draw(g);
        drawScores(g);
    }

    private void drawField(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 800, 600);
        g.drawRect(30, 25, 740, 550);
        g.setColor(Color.RED);
        g.drawLine(30, 75, 30, 525);
        g.drawLine(770, 75, 770, 525);
    }

    public void connectToServer(int port) {
        try {
            socket = new Socket("localhost", port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("Player " + playerID);
            if (playerID == 1) {
                System.out.println("Waiting for player 2 to connect...");
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
        } catch (IOException e) {
            System.out.println("Error connecting to server");
        }
    }
    private void drawScores(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Player 1: " + p1Score, 50, 50);
        g.drawString("Player 2: " + p2Score, 600, 50);
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in) {
            dataIn = in;
            System.out.println("RFS runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (player2 != null) {
                        player2.setX(dataIn.readInt());
                        player2.setY(dataIn.readInt());
                    }
                    int ballX = dataIn.readInt();
                    int ballY = dataIn.readInt();
                    ball.setPosition(ballX, ballY);

                    p1Score = dataIn.readInt();
                    p2Score = dataIn.readInt();
                }
            } catch (IOException e) {
                System.out.println("Error reading from server");
            }
        }

        public void waitForStartMsg() {
            try {
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                new Thread(rfsRunnable).start();
                new Thread(wtsRunnable).start();
            } catch (IOException e) {
                System.out.println("Error waiting for start message");
            }
        }
    }

    private class WriteToServer implements Runnable {
        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out) {
            dataOut = out;
            System.out.println("WTS runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (player1 != null) {
                        dataOut.writeInt(player1.getX());
                        dataOut.writeInt(player1.getY());
                        dataOut.flush();
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("Error in WTS run() sleep");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error writing to server");
            }
        }
    }
}
