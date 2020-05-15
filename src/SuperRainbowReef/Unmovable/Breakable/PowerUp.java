package SuperRainbowReef.Unmovable.Breakable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class PowerUp extends Breakable implements Observer {

    private String powerUp;

    public PowerUp(int x, int y, int width, int height, BufferedImage img, int pointValue, String powerUp) {
        super(x, y, img, pointValue);
        this.setHeight(img.getHeight());
        this.setWidth(img.getWidth());
        this.setRectangle(x, y, width, height);
        this.powerUp = powerUp;
    }

    public String getPowerUp() {
        return this.powerUp;
    }

    @Override
    public void drawImage(Graphics2D g) {
        g.drawImage(this.img, this.getX(), this.getY(), this);
    }

    @Override
    public void update(Observable o, Object arg) { }

}
