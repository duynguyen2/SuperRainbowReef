package SuperRainbowReef;

import SuperRainbowReef.Movable.Katch;
import SuperRainbowReef.Movable.Pop;
import SuperRainbowReef.Unmovable.Breakable.BigLegs;
import SuperRainbowReef.Unmovable.Breakable.CoralBlocks;
import SuperRainbowReef.Unmovable.Breakable.PowerUp;
import SuperRainbowReef.Unmovable.Unbreakable.UnbreakableBlock;
import SuperRainbowReef.Unmovable.Unbreakable.UnbreakableWall;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Event extends JPanel {

    private BufferedImage screen, backgroundIMG, winIMG, loseIMG;

    private int mapWidth, mapHeight;
    private int level = 1;

    private ArrayList<UnbreakableWall> unbreakableWalls = new ArrayList<>();
    private ArrayList<UnbreakableBlock> unbreakableBlocks = new ArrayList<>();
    private ArrayList<CoralBlocks> coralBlocks = new ArrayList<>();
    private ArrayList<BigLegs> bigLegs = new ArrayList<>();
    private ArrayList<PowerUp> powerUp = new ArrayList<>();

    private static Pop pop;
    private static Katch katch;

    private boolean gameOver = false;
    private boolean winner = false;
    private boolean levelUp = false;

    public Event(int mapWidth, int mapHeight) throws IOException {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.setSize(mapWidth, mapHeight);
        this.setPreferredSize(new Dimension(mapWidth, mapHeight));
        this.backgroundIMG = ImageIO.read(getClass().getResource("/Background1.bmp"));
        this.winIMG = ImageIO.read(getClass().getResource("/Congratulation.gif"));
        this.loseIMG = ImageIO.read(getClass().getResource("/srrloss.jpg"));
    }

    public void getGameImage() {
        BufferedImage img = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        checkGameStatus();

        if (!gameOver && !winner) {
            g2.drawImage(this.backgroundIMG, 0, 0, this);
            drawMapLayout(g2);
            katch.drawImage(g2);
            pop.drawImage(g2);
            g2.drawString("Score: " + pop.getScore(), 520, 420);
            g2.drawString("Lives: " +  pop.getLife(), 520, 400);
            g2.drawString("Level:" + this.getLevel(), 520, 380);
            screen = img;
        } else if (winner) {
            g2.drawImage(this.winIMG, 0, 0, 615, 470, this);
            screen = img;
        } else if (gameOver) {
            g2.drawImage(this.backgroundIMG, 0, 0, this);
            g2.drawImage(this.loseIMG, 0, 0, 615, 470, this);
            g2.drawString("Score: " + pop.getScore(), 380, 250);
            g2.drawString("Lives: " +  pop.getLife(), 380, 250);
            g2.drawString("Level:" + this.getLevel(), 380, 250);
            screen = img;
        }

    }

    public void setMapObjects(ArrayList<UnbreakableWall> unbreakableWalls, ArrayList<UnbreakableBlock> unbreakableBlocks, ArrayList<CoralBlocks> coralBlocks, ArrayList<PowerUp> powerUp, ArrayList<BigLegs> bigLegs) {
        this.unbreakableWalls = unbreakableWalls;
        this.unbreakableBlocks = unbreakableBlocks;
        this.coralBlocks = coralBlocks;
        this.powerUp = powerUp;
        this.bigLegs = bigLegs;
    }

    public void setKatch(Katch katch) {
        this.katch = katch;
    }

    public void setPop(Pop pop) {
        this.pop = pop;
    }

    public void setLevelUpStatus(boolean bool) {
        this.levelUp = bool;
    }

    public int getLevel() {
        return level;
    }

    public boolean getLevelUpStatus() {
        return levelUp;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    private void drawMapLayout(Graphics2D g) {
        for (UnbreakableWall unbreakableWall : unbreakableWalls)
            unbreakableWall.drawImage(g);

        for (UnbreakableBlock solidBlock : unbreakableBlocks)
            solidBlock.drawImage(g);

        for (CoralBlocks coralBlock : coralBlocks)
            coralBlock.drawImage(g);

        for (PowerUp up : powerUp)
            up.drawImage(g);

        for (BigLegs curr : bigLegs)
            curr.drawImage(g);

    }

    public void checkGameStatus() {
        if (bigLegs.isEmpty()) {
            pop.respawn();
            levelUp = true;
        }
        else if (pop.getLife() <= 0)
            gameOver = true;
        else if(level > 3)
            winner = true;

    }

    public void levelUp() { level++; }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getGameImage();
        g.drawImage(screen, 0, 0, this);
    }

}
