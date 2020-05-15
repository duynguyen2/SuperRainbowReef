package SuperRainbowReef.Unmovable;

import SuperRainbowReef.GameObject;
import java.awt.image.BufferedImage;

public abstract class Unmovable extends GameObject {

    public Unmovable() {}

    public Unmovable(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

}
