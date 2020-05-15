package SuperRainbowReef;

import SuperRainbowReef.Movable.Katch;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KatchControl implements KeyListener {

    private final Katch katch;
    private final int left;
    private final int right;
    private final int aimLeft;
    private final int aimRight;
    private final int shoot;

    public KatchControl(Katch katch, int left, int right, int aimLeft, int aimRight, int shoot) {
        this.katch = katch;
        this.left = left;
        this.right = right;
        this.aimLeft = aimLeft;
        this.aimRight = aimRight;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) { }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == this.left) {
            this.katch.toggleLeftPressed();
        }
        if (keyPressed == this.right) {
            this.katch.toggleRightPressed();
        }
        if (keyPressed == this.shoot) {
            this.katch.toggleShootPressed();
        }
        if (keyPressed == this.aimLeft) {
            this.katch.toggleAimLeftPressed();
        }
        if (keyPressed == this.aimRight) {
            this.katch.toggleAimRightPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == this.left) {
            this.katch.unToggleLeftPressed();
        }
        if (keyReleased == this.right) {
            this.katch.unToggleRightPressed();
        }
        if (keyReleased == this.shoot) {
            this.katch.unToggleShootPressed();
        }
        if (keyReleased == this.aimLeft) {
            this.katch.unToggleAimLeftPressed();
        }
        if (keyReleased == this.aimRight) {
            this.katch.unToggleAimRightPressed();
        }
    }
}
