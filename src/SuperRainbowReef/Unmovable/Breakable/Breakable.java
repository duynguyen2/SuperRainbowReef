package SuperRainbowReef.Unmovable.Breakable;

import SuperRainbowReef.Unmovable.Unmovable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Breakable extends Unmovable {

    private int points;

    public Breakable(int x, int y, BufferedImage img, int points) {
        super(x, y, img);
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    @Override
    public void update() { }

    @Override
    public void drawImage(Graphics2D g) { }
}
