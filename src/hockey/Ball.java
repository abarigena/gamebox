package hockey;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball {
    private int x, y, diameter;
    private int xVelocity, yVelocity;
    private Color color;
    private Random random;

    public Ball(int x, int y, int diameter, Color color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
        this.random = new Random();
        resetVelocity();
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;

        // Отскок от верхней и нижней границ
        if (y <= 25 || y >= 575 - diameter) {
            yVelocity = -yVelocity;
        }

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void reverseXVelocity() {
        xVelocity = -xVelocity;
    }
    public void resetVelocity() {
        int minSpeed = 3;
        int maxSpeed = 5;

        xVelocity = random.nextInt(maxSpeed - minSpeed + 1) + minSpeed;
        yVelocity = random.nextInt(maxSpeed - minSpeed + 1) + minSpeed;

        // Случайное направление скорости (влево или вправо для x, вверх или вниз для y)
        xVelocity *= random.nextBoolean() ? 1 : -1;
        yVelocity *= random.nextBoolean() ? 1 : -1;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void increaseVelocity() {
        int increaseAmount = 1;
        xVelocity += (xVelocity > 0 ? increaseAmount : -increaseAmount);
        yVelocity += (yVelocity > 0 ? increaseAmount : -increaseAmount);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }
}
