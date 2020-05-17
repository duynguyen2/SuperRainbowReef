package SuperRainbowReef.Unmovable.Unbreakable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class Wall extends UnbreakableWalls implements Observer {

    public Wall() {}

    public Wall(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, img);
        this.setHeight(img.getHeight());
        this.setWidth(img.getWidth());
        this.setRectangle(x, y, width, height);
    }

    @Override
    public void drawImage(Graphics2D g) {
        g.drawImage(this.img, this.getX(), this.getY(), this);
    }

    @Override
    public void update(Observable o, Object arg) { }

    @Override
    public void update() { }

}
