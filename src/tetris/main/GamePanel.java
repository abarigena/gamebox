package tetris.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager playManager;
    public static int maxScore=0;
    public GamePanel(){

        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        //Добавляем кнопки
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        playManager = new PlayManager();




    }
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void stopGame() {
        gameThread = null;
    }
    public void setPlayManager(PlayManager playManager) {
        this.playManager = playManager;
    }

    @Override
    public void run() {

        //игровой цикл
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta +=(currentTime-lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta>=1){
                update();
                repaint();
                delta--;
            }
        }
    }
    private void update(){
        if(KeyHandler.pausePressed==false && playManager.gameOver ==false){
            playManager.update();
        }else {
            maxScore = Math.max(PlayManager.score,maxScore);

        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        playManager.draw(g2);
    }
}
