package SuperRainbowReef.Movable;

import SuperRainbowReef.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Movable extends GameObject {

    protected double speed;

    public Movable(int x, int y, double speed, BufferedImage img) {
        super(x, y, img, null);
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void update() { }

    @Override
    public void drawImage(Graphics2D g) { }
}
