package SuperRainbowReef.Movable;

import SuperRainbowReef.Unmovable.Unbreakable.UnbreakableWall;
import SuperRainbowReef.GameWorld;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class Katch extends Movable implements Observer {

    private int angle;
    private boolean canShoot;
    private boolean moveLeftPressed, moveRightPressed, shootPressed, aimLeftPressed, aimRightPressed;

    private Pop pop;
    private GameWorld gameWorld;

    public Katch(int x, int y, int speed, BufferedImage img) {
        super(x, y, speed, img);
        this.moveLeftPressed = false;
        this.moveRightPressed = false;
        this.shootPressed = false;
        this.canShoot = true;
        this.aimLeftPressed = false;
        this.aimRightPressed = false;
        this.angle = 255;
    }

    public void toggleLeftPressed() {
        this.moveLeftPressed = true;
    }

    public void toggleRightPressed() {
        this.moveRightPressed = true;
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void toggleShootabilty() { this.canShoot = true; }

    public void toggleAimLeftPressed() {
        this.aimLeftPressed = true;
    }

    public void toggleAimRightPressed() { this.aimRightPressed = true; }

    public void unToggleLeftPressed() { this.moveLeftPressed = false; }

    public void unToggleRightPressed() {
        this.moveRightPressed = false;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }

    public void unToggleShootability() { this.canShoot = false; }

    public void unToggleAimLeftPressed() {
        this.aimLeftPressed = false;
    }

    public void unToggleAimRightPressed() {
        this.aimRightPressed = false;
    }

    public boolean getShootability(){ return this.canShoot; }

    public void setPop(Pop pop) {
        this.pop = pop;
    }

    public void setGameWorld(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }

    private void shoot() {
        if (shootPressed && canShoot) {
            this.pop.setXVelocity(this.pop.getSpeed() * Math.cos(Math.toRadians(angle)));
            this.pop.setYVelocity(this.pop.getSpeed() * Math.sin(Math.toRadians(angle)));
            this.pop.setMoveOn();

            unToggleShootability();
            toggleAimLeftPressed();
            toggleAimRightPressed();

            this.angle = 255;
        }
    }

    @Override
    public void update(Observable obj, Object arg) {
        shoot();
        if (canShoot) {
            if (aimLeftPressed && angle >= 240) {
                angle -= 15;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            if (aimRightPressed && angle <= 300) {
                angle += 15;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }

        if (moveLeftPressed)
            this.setX(this.getX() - (int )speed * 2);

        if (moveRightPressed)
            this.setX(this.getX() + (int )speed * 2);

        UnbreakableWall unbreakableWall = new UnbreakableWall();
        for (UnbreakableWall unbreakableWall1 : this.gameWorld.getUnbreakableWalls())
            if (this.getRectangle().intersects(unbreakableWall1.getRectangle()))
                unbreakableWall = unbreakableWall1;


        if (unbreakableWall != null) {
            int max_int = Integer.MAX_VALUE;
            int[] collision = new int[]{max_int, max_int};

            if ((this.getX() + this.getWidth()) > unbreakableWall.getRectangle().x && this.getX() < unbreakableWall.getRectangle().x)
                collision[0] = (this.getX() + this.getWidth()) - unbreakableWall.getRectangle().x;

            if (this.getX() < (unbreakableWall.getRectangle().x + unbreakableWall.getRectangle().width) && (this.getX() + this.getWidth()) > (unbreakableWall.getRectangle().x + unbreakableWall.getRectangle().width))
                collision[1] = (unbreakableWall.getRectangle().x + unbreakableWall.getRectangle().width) - this.getX();


            int min = max_int;
            int index = -1;
            for (int i = 0; i < 2; i++) {
                if (collision[i] < min) {
                    min = collision[i];
                    index = i;
                }
            }

            if (index == 0)
                this.setX(this.getX() - (int)speed * 2);

            if (index == 1)
                this.setX(this.getX() + (int)speed * 2);

        }
    }

    @Override
    public void drawImage(Graphics2D g) {
        int[] playerXCoordinates = {this.getX() + (this.getWidth() / 2) + 40,
                this.getX() + (this.getWidth() / 2) + 30, this.getX() + (this.getWidth() / 2) + 30};
        int[] playerYCoordinates = {this.getY() - 17, this.getY() - 27, this.getY() - 7};

        Line2D line = new Line2D.Double(this.getX() + (this.getWidth() / 2), this.getY() - (this.pop.getHeight() / 2),
                this.getX() + (this.getWidth() / 2) + 30, this.getY() - (this.pop.getHeight() / 2));
        AffineTransform rotation = AffineTransform.getRotateInstance(Math.toRadians(angle), line.getX1(), line.getY1());

        if (!shootPressed && canShoot) {
            g.setColor(Color.YELLOW);
            g.draw(rotation.createTransformedShape(line));
            g.draw(rotation.createTransformedShape(new Polygon(playerXCoordinates, playerYCoordinates, 3)));
        }
        g.drawImage(img, this.getX(), this.getY(), this);
    }

}
