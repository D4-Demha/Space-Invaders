package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class ObjectsManager extends JPanel {

    private Queue<Integer> alienWave = new LinkedList<>();      // for sequence of aliens (0 for easy, 1 for medium, and 2 for hard)

    private int alienW = 40, alienH = 40;
    private int frameW;
    private ImageIcon[] icons = new ImageIcon[3];

    public ObjectsManager(int w) {
        this.frameW = w;
        this.setLayout(null);       // for paint feature to work pixel by pixel
        this.setOpaque(false);      // no background
        createIcons();
        generateWave();
    }

    private void generateWave() {       // Uses enqueue operation (add()) to populate the queue
        for(int i=0; i<12; i++)
            alienWave.add(0);
        for(int i=0; i<12; i++)
            alienWave.add(1);
        for(int i=0; i<12; i++)
            alienWave.add(2);
    }

    public void spawnAlien() {      // Uses dequeue operation (poll()) to populate the queue
        if (!alienWave.isEmpty()) {

            int type = alienWave.poll();        // DE-QUEUE

            JLabel alien = new JLabel();
            alien.setIcon(icons[type]);
            int x = (int)(Math.random() * (frameW - alienW - 20));
            alien.setBounds(x, -50, alienW, alienH);
            this.add(alien);
        }
    }

    public void moveObjects() {
        for(Component c : this.getComponents()) {
            if(c.isVisible()) {
                c.setLocation(c.getX(), c.getY() + 1);
            }
        }
    }

    public boolean checkBottomHit(int limitY) {
        for(Component c : this.getComponents()) {
            if(c.isVisible() && c.getY() + c.getHeight() >= limitY)
                return true;
        }
        return false;
    }

    private void createIcons() {
        Color[] cols = {Color.RED, Color.GREEN, Color.MAGENTA};
        for(int i=0; i<3; i++) {
            BufferedImage img = new BufferedImage(alienW, alienH, BufferedImage.TYPE_INT_ARGB);     // for drawing once and re-using later
            Graphics2D g = img.createGraphics();        // for creating a blank image
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);     // for smoothing edges
            g.setColor(cols[i]);
            g.fillOval(0, 0, alienW, alienH-10);            // Alien's body
            g.fillOval(0, alienH-15, 12, 15);       // tentacle
            g.fillOval(14, alienH-15, 12, 15);      // tentacle
            g.fillOval(28, alienH-15, 12, 15);      // tentacle
            g.setColor(Color.WHITE); g.fillOval(8, 10, 10, 10);     // for eyes
            g.fillOval(22, 10, 10, 10);                             // for eyes
            g.setColor(Color.BLACK); g.fillOval(10, 12, 4, 4);      // for eyes
            g.fillOval(24, 12, 4, 4);                               // for eyes
            g.dispose();                    // free graphics object
            icons[i] = new ImageIcon(img);          // populate "icons" array
        }
    }
}