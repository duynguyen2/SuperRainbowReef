package SuperRainbowReef.Unmovable.Unbreakable;

import SuperRainbowReef.Unmovable.Unmovable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends Unmovable {

    public Wall() {}

    public Wall(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void update() { }

    @Override
    public void drawImage(Graphics2D g) { }
}
