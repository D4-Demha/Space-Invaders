package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Shooter extends JPanel {
    private int screenX, screenY;
    private int width = 50, height = 50;
    private int frameW;
    private int speed = 8;

    private boolean left, right;
    private boolean shootingRequest = false;
    private boolean multiBullet = false;
    private long lastFireTime = 0;
    private long fireDelay = 300;

    public Shooter(int w, int h) {
        this.frameW = w;
        this.screenX = w / 2 - 25;
        this.screenY = h - 110;
        this.setLayout(null);       // for paint to work
        this.setOpaque(false);      // transparent background
        this.setBounds(screenX, screenY, width, height);
    }

    public void update() {
        if (left)
            screenX = Math.max(0, screenX - speed);
        if (right)
            screenX = Math.min(frameW - width - 15 , screenX + speed);
        this.setLocation(screenX, screenY);
        this.setSize(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);         // Clears the previous drawing
        Graphics2D g2 = (Graphics2D) g;          // convert to Graphics2D for advanced settings
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        // for smoothing jagged pixel edges

        g2.setColor(new Color(0, 150, 255));
        int[] xp = {width/2, width, 0};
        int[] yp = {0, height, height};
        g2.fillPolygon(xp, yp, 3);      // draws a solid triangle using data in arrays

        g2.setColor(Color.WHITE);
        g2.fillOval(width/2 - 5, height/2, 10, 15);
        g2.setColor(Color.ORANGE);
        g2.fillOval(width/2 - 5, height - 10, 10, 10);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            left = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            right = true;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            long now = System.currentTimeMillis();
            if(now - lastFireTime > fireDelay) {
                shootingRequest = true;
                lastFireTime = now;
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
    }

    public int getWidth() { return width; }
    public boolean isShooting() { return shootingRequest; }
    public void setShooting(boolean b) { shootingRequest = b; }
    public boolean isMultiBullet() { return multiBullet; }

    public void setBigShooter() { width = 80; }
    public void setSmallShooter() { width = 30; }
    public void setFastFire() { fireDelay = 100; }
    public void setSlowFire() { fireDelay = 600; }
    public void setMultiBullet(boolean b) { multiBullet = b; }
    public void resetShooter() { width = 50; fireDelay = 300; multiBullet = false; }        // for resetting to defaults
}