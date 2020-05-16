package SuperRainbowReef;

import SuperRainbowReef.Movable.*;
import SuperRainbowReef.Unmovable.Breakable.*;
import SuperRainbowReef.Unmovable.Unbreakable.*;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameWorld implements Runnable {

    private JFrame frame;
    private String frameTitle;
    private int frameWidth, frameHeight;

    private int[][] map;

    private Event event;

    private final GameWorldObservable gameWorldObservable;
    private KatchControl katchControl;

    private Thread thread;
    private boolean isRunning = false;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<SolidBlocks> solidBlocks = new ArrayList<>();
    private ArrayList<CoralBlocks> coralBlocks = new ArrayList<>();
    private ArrayList<BigLegs> bigLegs = new ArrayList<>();
    private ArrayList<PowerUp> powerUp = new ArrayList<>();
    private static Pop pop;
    private static Katch katch;

    public GameWorld() {
        this.gameWorldObservable = new GameWorldObservable();
    }

    public static void main(String args[]) {
        GameWorld rainbowWorld = new GameWorld();
        rainbowWorld.start();
    }

    private void init() throws IOException {
        this.frameTitle = "Super Rainbow Reef";
        this.frameWidth = 615;
        this.frameHeight = 470;
        this.event = new Event(615, 470);

        BufferedImage katchIMG = ImageIO.read(getClass().getResource("/Katch.gif"));
        BufferedImage popIMG = ImageIO.read(getClass().getResource("/Pop.gif"));

        this.katch = new Katch(615 / 2 - katchIMG.getWidth() / 2, 470 - katchIMG.getHeight() - popIMG.getHeight(), 3, katchIMG);
        this.katch.setGameWorld(this);

        this.pop = new Pop(this.katch, popIMG, 2);
        this.pop.setGameWorld(this);

        this.katch.setPop(this.pop);
        this.katchControl = new KatchControl(katch, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        this.gameWorldObservable.addObserver(katch);
        this.gameWorldObservable.addObserver(pop);
        this.event.setKatch(this.katch);
        this.event.setPop(this.pop);

        initMap();

        frame = new JFrame(frameTitle);
        this.frame.setSize(frameWidth, frameHeight);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(this.event);
        this.frame.addKeyListener(katchControl);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (isRunning) {
                this.gameWorldObservable.setChanged();
                this.gameWorldObservable.notifyObservers();
                render();

                Thread.sleep(1000 / 144);

                if (this.event.getLevelUpStatus()) {
                    this.event.setLevelUpStatus(false);
                    this.event.levelUp();
                    this.coralBlocks.removeAll(coralBlocks);
                    this.solidBlocks.removeAll(solidBlocks);
                    powerUp.removeAll(powerUp);
                    this.initMap();
                    this.createMapObjects();
                    if (this.event.getGameOver())
                        System.out.println("GAME OVER");

                }

            }
        } catch (InterruptedException | IOException ie) {

        }
        stop();
    }

    public synchronized void start() {
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        if (!isRunning)
            return;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initMap() throws IOException {
        if(event.getLevel() == 1) {
            this.map = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 8, 0, 8, 0, 2, 0, 5, 0, 13, 0, 3, 0, 6, 0, 12, 0, 0, 0, 3, 0, 8, 0, 7, 0, 2, 0, 8, 0, 8, 0, 1},
                    {1, 3, 0, 4, 0, 2, 0, 5, 0, 0, 0, 3, 0, 6, 0, 0, 0, 0, 0, 3, 0, 8, 0, 7, 0, 2, 0, 5, 0, 4, 0, 1},
                    {1, 13, 0, 5, 0, 2, 0, 5, 0, 7, 0, 3, 0, 6, 0, 0, 0, 0, 0, 3, 0, 9, 0, 6, 0, 2, 0, 5, 0, 4, 0, 1},
                    {1, 0, 0, 5, 0, 2, 0, 5, 0, 7, 0, 3, 0, 6, 0, 0, 0, 0, 0, 3, 0, 9, 0, 6, 0, 2, 0, 5, 0, 11, 0, 1},
                    {1, 5, 0, 8, 0, 2, 0, 5, 0, 12, 0, 0, 0, 6, 0, 4, 0, 6, 0, 3, 0, 8, 0, 8, 0, 2, 0, 5, 0, 9, 0, 1},
                    {1, 10, 0, 8, 0, 2, 0, 5, 0, 0, 0, 0, 0, 6, 0, 4, 0, 6, 0, 3, 0, 8, 0, 6, 0, 2, 0, 5, 0, 9, 0, 1},
                    {1, 3, 0, 9, 0, 2, 0, 5, 0, 0, 0, 0, 0, 6, 0, 8, 0, 10, 0, 3, 0, 9, 0, 7, 0, 2, 0, 5, 0, 8, 0, 1},
                    {1, 6, 0, 10, 0, 2, 0, 5, 0, 0, 0, 0, 0, 6, 0, 4, 0, 6, 0, 3, 0, 9, 0, 7, 0, 2, 0, 5, 0, 13, 0, 1},
                    {1, 6, 0, 4, 0, 2, 0, 5, 0, 7, 0, 3, 0, 6, 0, 10, 0, 6, 0, 3, 0, 8, 0, 7, 0, 2, 0, 5, 0, 0, 0, 1},
                    {1, 7, 0, 4, 0, 2, 0, 8, 0, 7, 0, 3, 0, 6, 0, 4, 0, 6, 0, 3, 0, 8, 0, 8, 0, 2, 0, 5, 0, 4, 0, 1},
                    {1, 3, 0, 4, 0, 2, 0, 5, 0, 7, 0, 3, 0, 6, 0, 4, 0, 6, 0, 3, 0, 6, 0, 6, 0, 2, 0, 5, 0, 4, 0, 1},
                    {1, 10, 0, 8, 0, 2, 0, 5, 0, 7, 0, 3, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 2, 0, 4, 0, 10, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},};
        }
        if(this.event.getLevel() == 2){
            this.map = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 9, 0, 13, 0, 2, 0, 12, 0, 0, 0, 7, 0, 8, 0, 2, 0, 12, 0, 0, 0, 11, 0, 7, 0, 2, 0, 4, 0, 13, 0, 1},
                    {1, 9, 0, 0, 0, 2, 0, 0, 0, 0, 0, 7, 0, 8, 0, 2, 0, 0, 0, 0, 0, 8, 0, 7, 0, 2, 0, 3, 0, 0, 0, 1},
                    {1, 9, 0, 5, 0, 2, 0, 0, 0, 0, 0, 7, 0, 8, 0, 2, 0, 0, 0, 0, 0, 9, 0, 6, 0, 2, 0, 3, 0, 4, 0, 1},
                    {1, 9, 0, 5, 0, 2, 0, 3, 0, 0, 0, 6, 0, 8, 0, 2, 0, 0, 0, 3, 0, 9, 0, 6, 0, 2, 0, 4, 0, 11, 0, 1},
                    {1, 13, 0, 8, 0, 2, 0, 3, 0, 5, 0, 6, 0, 8, 0, 2, 0, 6, 0, 3, 0, 8, 0, 8, 0, 2, 0, 3, 0, 9, 0, 1},
                    {1, 0, 0, 8, 0, 2, 0, 7, 0, 5, 0, 6, 0, 7, 0, 2, 0, 6, 0, 3, 0, 8, 0, 6, 0, 2, 0, 3, 0, 9, 0, 1},
                    {1, 4, 0, 7, 0, 2, 0, 7, 0, 13, 0, 5, 0, 7, 0, 2, 0, 10, 0, 3, 0, 9, 0, 7, 0, 2, 0, 4, 0, 8, 0, 1},
                    {1, 4, 0, 10, 0, 2, 0, 3, 0, 0, 0, 5, 0, 7, 0, 2, 0, 6, 0, 3, 0, 9, 0, 7, 0, 2, 0, 3, 0, 13, 0, 1},
                    {1, 4, 0, 13, 0, 2, 0, 3, 0, 7, 0, 5, 0, 7, 0, 2, 0, 6, 0, 3, 0, 8, 0, 7, 0, 2, 0, 3, 0, 4, 0, 1},
                    {1, 4, 0, 0, 0, 2, 0, 3, 0, 7, 0, 3, 0, 7, 0, 2, 0, 6, 0, 3, 0, 8, 0, 8, 0, 2, 0, 4, 0, 10, 0, 1},
                    {1, 4, 0, 4, 0, 2, 0, 4, 0, 7, 0, 3, 0, 7, 0, 2, 0, 6, 0, 3, 0, 6, 0, 6, 0, 2, 0, 3, 0, 7, 0, 1},
                    {1, 10, 0, 8, 0, 2, 0, 4, 0, 7, 0, 3, 0, 0, 0, 2, 0, 0, 0, 10, 0, 0, 0, 0, 0, 2, 0, 3, 0, 7, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},};
        }
        if(this.event.getLevel() == 3){
            this.map = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 4, 0, 13, 0, 2, 0, 13, 0, 6, 0, 2, 0, 8, 0, 5, 0, 5, 0, 2, 0, 12, 0, 0, 0, 2, 0, 9, 0, 6, 0, 1},
                    {1, 4, 0, 0, 0, 2, 0, 3, 0, 6, 0, 2, 0, 8, 0, 7, 0, 5, 0, 2, 0, 0, 0, 0, 0, 2, 0, 13, 0, 6, 0, 1},
                    {1, 4, 0, 3, 0, 2, 0, 3, 0, 13, 0, 2, 0, 8, 0, 7, 0, 5, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 6, 0, 1},
                    {1, 4, 0, 10, 0, 2, 0, 3, 0, 0, 0, 2, 0, 8, 0, 7, 0, 5, 0, 2, 0, 9, 0, 6, 0, 2, 0, 9, 0, 6, 0, 1},
                    {1, 4, 0, 3, 0, 2, 0, 3, 0, 5, 0, 2, 0, 8, 0, 7, 0, 5, 0, 2, 0, 8, 0, 8, 0, 2, 0, 9, 0, 6, 0, 1},
                    {1, 13, 0, 3, 0, 2, 0, 7, 0, 5, 0, 2, 0, 7, 0, 7, 0, 6, 0, 2, 0, 8, 0, 6, 0, 2, 0, 9, 0, 10, 0, 1},
                    {1, 0, 0, 3, 0, 2, 0, 7, 0, 13, 0, 2, 0, 7, 0, 7, 0, 6, 0, 2, 0, 9, 0, 7, 0, 2, 0, 13, 0, 6, 0, 1},
                    {1, 5, 0, 10, 0, 2, 0, 3, 0, 0, 0, 2, 0, 7, 0, 7, 0, 6, 0, 2, 0, 9, 0, 7, 0, 2, 0, 0, 0, 6, 0, 1},
                    {1, 5, 0, 10, 0, 2, 0, 3, 0, 7, 0, 2, 0, 7, 0, 13, 0, 6, 0, 2, 0, 8, 0, 7, 0, 2, 0, 9, 0, 6, 0, 1},
                    {1, 5, 0, 3, 0, 2, 0, 3, 0, 7, 0, 2, 0, 7, 0, 0, 0, 6, 0, 2, 0, 8, 0, 8, 0, 2, 0, 13, 0, 6, 0, 1},
                    {1, 5, 0, 3, 0, 2, 0, 4, 0, 7, 0, 2, 0, 7, 0, 10, 0, 6, 0, 2, 0, 6, 0, 6, 0, 2, 0, 0, 0, 6, 0, 1},
                    {1, 5, 0, 3, 0, 2, 0, 4, 0, 7, 0, 2, 0, 7, 0, 7, 0, 6, 0, 2, 0, 0, 0, 0, 0, 2, 0, 9, 0, 6, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},};
        }
        createMapObjects();
    }

    public void createMapObjects() throws IOException {
        BufferedImage img;
        int cell = 19;
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 32; j++) {
                if (map[i][j] == 1) {
                    img = ImageIO.read(getClass().getResource("/Wall.gif"));
                    walls.add(new Wall(j * cell, i * cell,
                            img.getWidth(), img.getHeight(), img));
                }
                if (map[i][j] == 2) {
                    img = ImageIO.read(getClass().getResource("/Block_solid.gif"));
                    solidBlocks.add(new SolidBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img));
                }
                if (map[i][j] == 3) {
                    img = ImageIO.read(getClass().getResource("/Block1.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 10));
                }
                if (map[i][j] == 4) {
                    img = ImageIO.read(getClass().getResource("/Block2.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 15));
                }
                if (map[i][j] == 5) {
                    img = ImageIO.read(getClass().getResource("/Block3.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 20));
                }
                if (map[i][j] == 6) {
                    img = ImageIO.read(getClass().getResource("/Block4.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 25));
                }
                if (map[i][j] == 7) {
                    img = ImageIO.read(getClass().getResource("/Block5.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 30));
                }
                if (map[i][j] == 8) {
                    img = ImageIO.read(getClass().getResource("/Block6.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 35));
                }
                if (map[i][j] == 9) {
                    img = ImageIO.read(getClass().getResource("/Block7.gif"));
                    coralBlocks.add(new CoralBlocks(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 40));
                }
                if (map[i][j] == 10) {
                    img = ImageIO.read(getClass().getResource("/Block_life.gif"));
                    powerUp.add(new PowerUp(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 50, "life"));
                }
                if (map[i][j] == 11) {
                    img = ImageIO.read(getClass().getResource("/Block_split.gif"));
                    powerUp.add(new PowerUp(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 50, "split"));
                }
                if (map[i][j] == 12) {
                    img = ImageIO.read(getClass().getResource("/Bigleg.gif"));
                    bigLegs.add(new BigLegs(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 100));
                }
                if (map[i][j] == 13) {
                    img = ImageIO.read(getClass().getResource("/Bigleg_small.gif"));
                    bigLegs.add(new BigLegs(j * cell, i * cell, img.getWidth(), img.getHeight(), img, 100));
                }

            }
        }
        this.event.setMapObjects(walls, solidBlocks, coralBlocks, powerUp, bigLegs);

        for (Wall wall : walls)
            this.gameWorldObservable.addObserver(wall);

        for (SolidBlocks solidBlock : solidBlocks)
            this.gameWorldObservable.addObserver(solidBlock);

        for (CoralBlocks coralBlock : coralBlocks)
            this.gameWorldObservable.addObserver(coralBlock);

        for (PowerUp up : powerUp)
            this.gameWorldObservable.addObserver(up);

        for (BigLegs bigLegs : bigLegs)
            this.gameWorldObservable.addObserver(bigLegs);

    }

    private void render() { this.event.repaint(); }

    public ArrayList<Wall> getWalls() { return this.walls; }

    public ArrayList<SolidBlocks> getSolidBlocks() { return this.solidBlocks; }

    public ArrayList<CoralBlocks> getCoralBlocks() { return this.coralBlocks; }

    public ArrayList<BigLegs> getBigLegs() { return this.bigLegs; }

    public ArrayList<PowerUp> getPowerUp() { return this.powerUp; }

    public int getMapWidth() { return 615; }

    public int getMapHeight() { return 470; }

}
