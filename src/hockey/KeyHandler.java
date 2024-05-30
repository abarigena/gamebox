package hockey;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean upPressed, downPressed;
    private boolean wPressed, sPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> upPressed = true;
            case KeyEvent.VK_DOWN -> downPressed = true;
            case KeyEvent.VK_W -> wPressed = true;
            case KeyEvent.VK_S -> sPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isWPressed() {
        return wPressed;
    }

    public boolean isSPressed() {
        return sPressed;
    }
}
