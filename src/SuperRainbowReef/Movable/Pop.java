package SuperRainbowReef.Movable;

import SuperRainbowReef.*;
import SuperRainbowReef.Unmovable.Breakable.*;
import SuperRainbowReef.Unmovable.Unbreakable.*;
import SuperRainbowReef.GameWorld;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Pop extends Movable implements Observer {

    private int score = 0, life = 3;
    private int mapSizeX, mapSizeY;
    private double velocityX, velocityY;
    private double xDouble, yDouble;
    private int speedCap;

    private boolean isMoving;

    private Katch katch;
    private GameWorld gameWorld;

    public Pop(Katch katch, BufferedImage img, int speed) {
        super(katch.getX(), katch.getY(), speed, img);
        this.velocityX = 0;
        this.velocityY = 0;
        this.speedCap = speed;
        this.katch = katch;
        this.isMoving = false;
        this.setX(this.getX() + this.katch.getWidth() / 4);
        this.setY(this.getY() - this.getHeight());
        this.yDouble = this.getY();
        this.xDouble = this.getX();

    }

    public void setMoveOn() {
        this.isMoving = true;
    }

    public void setMoveOff() {
        this.isMoving = false;
    }

    public void setXVelocity(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setYVelocity(double velocityY) {
        this.velocityY = velocityY;
    }

    public void updateScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    double getXDouble() { return this.xDouble; }

    void setXDouble(double xDouble) { this.xDouble = xDouble; }

    double getYDouble() { return this.yDouble; }

    void setYDouble(double yDouble) { this.yDouble = yDouble; }

    public void setGameWorld(GameWorld gameWorld){
        this.gameWorld = gameWorld;
        this.mapSizeX = gameWorld.getMapWidth();
        this.mapSizeY = gameWorld.getMapHeight();
    }

    public void drawImage(Graphics2D g) { g.drawImage(img, (int) this.getXDouble(), (int) this.getYDouble(), null); }

    @Override
    public void update(Observable o, Object arg) {
        if (this.isMoving) {
            if (this.speed <= speedCap) {
                this.speed += 0.2;
                if (velocityY < 0)
                    velocityY--;

                else
                    velocityY++;

            }

            this.xDouble += velocityX;
            this.yDouble += velocityY;
            if (this.yDouble <= 0 || this.yDouble >= mapSizeY)
                respawn();

            if (this.xDouble <= 0 || this.xDouble >= mapSizeX)
                respawn();
        }
        if (!this.isMoving)
            this.xDouble = this.katch.getX() + this.katch.getWidth() / 4;

        GameObject obj = collision();

        if (obj instanceof Katch) {
            katchCollision();
        } else {

            if (obj != null) {
                this.setXDouble(xDouble + velocityX);
                this.setYDouble(yDouble + velocityY);

                int leftSide = (int)Math.round(xDouble), rightSide = (int)Math.round(xDouble) + this.getWidth();
                int topSide = (int)Math.round(yDouble), bottomSide = (int)Math.round(yDouble) + this.getHeight();

                int objLeftSide = obj.getX(), objRightSide = obj.getX() + obj.getWidth();
                int objTopSide = obj.getY(), objBottomSide = obj.getY() + obj.getHeight();

                if (obj instanceof CoralBlocks) {
                    updateScore(((CoralBlocks) obj).getPoints());
                    this.gameWorld.getCoralBlocks().remove(obj);
                }

                else if (obj instanceof BigLegs) {
                    updateScore(((BigLegs) obj).getPoints());
                    this.gameWorld.getBigLegs().remove(obj);
                }

                else if (obj instanceof PowerUp) {
                    if (((PowerUp) obj).getPowerUp().equals("life"))
                        life++;

                    updateScore(((PowerUp) obj).getPoints());
                    this.gameWorld.getPowerUp().remove(obj);
                }

                int[] intersections = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

                if (rightSide > objLeftSide && leftSide < objLeftSide)
                    intersections[0] = rightSide - objLeftSide;

                if (leftSide < objRightSide && rightSide > objRightSide)
                    intersections[1] = objRightSide - leftSide;

                if (topSide < objBottomSide && bottomSide > objBottomSide)
                    intersections[2] = objBottomSide - topSide;

                if (bottomSide > objTopSide && topSide < objTopSide)
                    intersections[3] = bottomSide - objTopSide;


                int min = Integer.MAX_VALUE, index = -1;
                for (int i = 0; i < 4; i++) {
                    if (intersections[i] < min) {
                        min = intersections[i];
                        index = i;
                    }
                }

                if (index == 0 || index == 1)
                    velocityX *= -1;

                if (index == 2 || index == 3)
                    velocityY *= -1;

            }
        }
    }

    public void respawn() {
        velocityX = 0;
        velocityY = 0;
        setMoveOff();
        life--;
        this.yDouble = this.getY();
        this.xDouble = this.katch.getX() + this.katch.getWidth() / 4;
        this.katch.toggleShootabilty();
    }

    private void katchCollision() {
        this.setXDouble(xDouble + velocityX);
        this.setYDouble(yDouble + velocityY);

        int[] KatchBorders = new int[4];
        for (int i = 0; i < 4; i++)
            KatchBorders[i] = this.katch.getX() + ((this.katch.getWidth() / 5) * (i + 1));


        if ((int)Math.round(xDouble) + (this.getWidth() / 2) < KatchBorders[0]) {
            if (velocityX > 0)
                velocityX *= -1;

            else
                velocityX -= 1;

        }

        else if ((int)Math.round(xDouble) + (this.getWidth() / 2) < KatchBorders[1]) {
            if (velocityX > 0)
                velocityX *= -1;

            else if (velocityX == 0)
                velocityX = -1;

        }

        else if ((int)Math.round(xDouble) + (this.getWidth() / 2) < KatchBorders[2])
            velocityX = 0;

        else if ((int)Math.round(xDouble) + (this.getWidth() / 2) < KatchBorders[3]) {

            if (velocityX < 0)
                velocityX *= -1;

            else if (velocityX == 0)
                velocityX = 1;

        }
        else {
            if (velocityX < 0)
                velocityX *= -1;

            else if (velocityX == 0)
                velocityX = 1;

            else
                velocityX += 1;
        }
        velocityY *= -1;
    }

    private GameObject collision() {
        this.setWidth(img.getWidth());
        this.setHeight(img.getHeight());
        Rectangle popRectangle = new Rectangle(this.getRectangle());
        popRectangle.setLocation((int)Math.round(xDouble + velocityX), (int)Math.round(yDouble + velocityY));

        GameObject obj = null;

        for (Wall curr : this.gameWorld.getWalls()) {
            if (popRectangle.intersects(curr.getRectangle()))
                obj = checkBorder(obj, curr);

        }

        for (SolidBlocks curr : this.gameWorld.getSolidBlocks()) {
            if (popRectangle.intersects(curr.getRectangle()))
                obj = checkBorder(obj, curr);

        }

        for (CoralBlocks curr : this.gameWorld.getCoralBlocks()) {
            if (popRectangle.intersects(curr.getRectangle()))
                obj = checkBorder(obj, curr);

        }

        for (BigLegs curr : this.gameWorld.getBigLegs()) {
            if (popRectangle.intersects(curr.getRectangle()))
                obj = checkBorder(obj, curr);

        }

        for (PowerUp curr : this.gameWorld.getPowerUp()) {
            if (popRectangle.intersects(curr.getRectangle()))
                obj = checkBorder(obj, curr);

        }

        if (!(this.katch.getShootability())) {
            if (popRectangle.intersects(this.katch.getRectangle()))
                obj = checkBorder(obj, this.katch);

        }
        return obj;
    }

    private GameObject checkBorder(GameObject obj1, GameObject obj2) {
        if (obj1 != null) {
            Rectangle popRectangle = new Rectangle(this.getRectangle());
            popRectangle.setLocation((int)Math.round(xDouble + velocityX), (int)Math.round(yDouble + velocityY));

            double distance1 = Math.sqrt(Math.pow(((int)Math.round(xDouble) + this.getWidth() / 2 - (obj1.getX() + obj1.getWidth() / 2)),
                    2) + Math.pow(((int)Math.round(yDouble) + this.getHeight() / 2 - (obj1.getY() + obj1.getHeight() / 2)), 2));
            double distance2 = Math.sqrt(Math.pow(((int)Math.round(xDouble) + this.getWidth() / 2 - (obj2.getX() + (obj2.getWidth() / 2))), 2)
                    + Math.pow(((int)Math.round(yDouble) + this.getHeight() / 2 - (obj2.getY() + (obj2.getHeight() / 2))), 2));

            if (distance1 < distance2)
                return obj1;

        }
        return obj2;
    }

}
