package hockey;

import java.awt.*;


public class Player {
    private int x, y, width, height;
    private Color color;


    public Player(int x,int y, int width, int height, Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }
    public void moveUp() {
        if (y > 0) {
            y -= 10;
        }
    }

    public void moveDown() {
        if (y + height < 600) {
            y += 10;
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Геттеры и сеттеры для координат (если нужно будет передвигать игроков)
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
