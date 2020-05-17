package SuperRainbowReef;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;

public abstract class GameObject extends JComponent {

    int x, y, width, height;
    Rectangle rectangle;
    protected BufferedImage img;

    public abstract void update();
    public abstract void drawImage(Graphics2D g);

    public GameObject() {}

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameObject(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.img = img;
        this.rectangle = new Rectangle(x, y, this.width, this.height);
    }

    public GameObject(int x, int y, BufferedImage img, ImageObserver observer) {
        this.img = img;
        this.x = x;
        this.y = y;
        try {
            this.width = img.getWidth(observer);
            this.height = img.getHeight(observer);
        } catch (NullPointerException e) {
            this.width = 0;
            this.height = 0;
        }
        this.rectangle = new Rectangle(x, y, this.width, this.height);
    }

    public void setX(int x) { this.x = x; }

    public int getX() {
        return this.x;
    }

    public void setY(int y) { this.y = y; }

    public int getY() {
        return this.y;
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public void setWidth(int width) {
        this.width = width;
        this.rectangle.setSize(width, this.height);
    }

    public void setHeight(int height) {
        this.height = height;
        this.rectangle.setSize(this.width, height);
    }

    public void setRectangle(int x, int y, int width, int height) { this.rectangle = new Rectangle(x, y, width, height); }

    public Rectangle getRectangle() { return new Rectangle(x, y, width, height); }

    public void setImage(BufferedImage img) { this.img = img; }

    public Image getImage() { return this.img; }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(this.x, this.y);
    }

    @Override
    public void setLocation(Point location) {
        this.x = location.x;
        this.y = location.y;
        this.rectangle.setLocation(location);
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.rectangle.setSize(width, height);
    }

    @Override
    public void setSize(Dimension dim) { this.rectangle.setSize(dim); }

    @Override
    public Point getLocation() { return new Point(this.x, this.y); }

    @Override
    public Dimension getSize() { return this.rectangle.getSize(); }

}
