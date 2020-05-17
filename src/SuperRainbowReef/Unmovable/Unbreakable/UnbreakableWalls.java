package SuperRainbowReef.Unmovable.Unbreakable;

import SuperRainbowReef.Unmovable.Unmovable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakableWalls extends Unmovable {

    public UnbreakableWalls() {}

    public UnbreakableWalls(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void update() { }

    @Override
    public void drawImage(Graphics2D g) { }
}
