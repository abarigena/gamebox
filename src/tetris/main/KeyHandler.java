package tetris.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public static boolean upPressed, downPressed, leftpressed, rightPresed, pausePressed,exitGame;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftpressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPresed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            if(pausePressed){
                pausePressed = false;
            }else{
                pausePressed = true;
            }
        }
        if(code == KeyEvent.VK_ESCAPE){
            exitGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
