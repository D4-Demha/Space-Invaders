package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int width = 550;
        int height = 700;
        double alienSpawnRate = 0.02;    //spawn rate of aliens

        JFrame frame = new JFrame("Space Invaders");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layers = new JLayeredPane();    // for Z-ordering depth
        layers.setPreferredSize(new Dimension(width, height));
        layers.setOpaque(true);
        layers.setBackground(new Color(15, 15, 30));   // for background color

        Scoreboard scoreboard = new Scoreboard(width, height);
        scoreboard.setBounds(0, 0, width, height);

        ObjectsManager invaders = new ObjectsManager(width);
        invaders.setBounds(0, 0, width, height);

        BulletsLayer bulletsLayer = new BulletsLayer();
        bulletsLayer.setBounds(0, 0, width, height);

        Shooter shooter = new Shooter(width, height);

        PowerUps powerUps = new PowerUps(scoreboard, shooter);

        layers.add(invaders, Integer.valueOf(100));
        layers.add(bulletsLayer, Integer.valueOf(200));
        layers.add(shooter, Integer.valueOf(300));
        layers.add(scoreboard, Integer.valueOf(400));

        frame.add(layers);
        frame.pack();
        frame.setLocationRelativeTo(null);    // centers the frame on the screen

        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) { shooter.keyPressed(e); }
            public void keyReleased(KeyEvent e) { shooter.keyReleased(e); }
            public void keyTyped(KeyEvent e) {}
        });

        frame.setVisible(true);

        boolean game_is_on = true;
        long powerUpTimer = 0;
        long startTime = System.currentTimeMillis();
        int roundDuration = 30;

        while(game_is_on) {
            try { Thread.sleep(15); } catch (InterruptedException e) {}

            int elapsed = (int)((System.currentTimeMillis() - startTime) / 1000);       // to get time in seconds
            int timeLeft = Math.max(0, roundDuration - elapsed);     // so time doesn't go below 0
            scoreboard.updateTime(timeLeft);

            if (timeLeft > 0) {
                if (Math.random() < alienSpawnRate)
                    invaders.spawnAlien();
            } else if (invaders.getComponents().length == 0) {
                scoreboard.displayWinMessage();
                game_is_on = false;
            }

            shooter.update();
            bulletsLayer.moveBullets();
            invaders.moveObjects();

            ArrayList<Rectangle> bullets = bulletsLayer.getBulletRects();
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Rectangle b = bullets.get(i);
                boolean hit = false;
                for (Component alien : invaders.getComponents()) {      // linear search for collision
                    if (alien.isVisible() && b.intersects(alien.getBounds())) {
                        alien.setVisible(false);
                        invaders.remove(alien);
                        bulletsLayer.removeBullet(i);
                        scoreboard.addScore(25);

                        if (powerUps.tryTriggerPowerUp()) {
                            powerUpTimer = System.currentTimeMillis() + 5000;
                        }
                        hit = true;
                        break;
                    }
                }
                if (hit)
                    continue;
            }

            if (powerUpTimer > 0 && System.currentTimeMillis() > powerUpTimer) {
                powerUps.revertState();

                powerUpTimer = System.currentTimeMillis() + 2000;
            }

            if (shooter.isShooting()) {
                int sx = shooter.getX() + shooter.getWidth()/2;
                int sy = shooter.getY();
                if (shooter.isMultiBullet()) {
                    bulletsLayer.fireBullet(sx, sy, -1);
                    bulletsLayer.fireBullet(sx, sy, 0);
                    bulletsLayer.fireBullet(sx, sy, 1);
                } else {
                    bulletsLayer.fireBullet(sx, sy, 0);
                }
                shooter.setShooting(false);
            }

            if (invaders.checkBottomHit(height - 80)) {
                scoreboard.gameOver();
                game_is_on = false;
            }
            frame.repaint();        // for updating the screen each iteration
        }
    }
}