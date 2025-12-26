package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BulletsLayer extends JPanel {
    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private ArrayList<Integer> directions = new ArrayList<>();
    private int speed = 15;

    public BulletsLayer() {
        this.setLayout(null);       // for drawing
        this.setOpaque(false);      // no background
    }

    public void fireBullet(int x, int y, int dir) {
        bullets.add(new Rectangle(x-2, y, 4, 12));
        directions.add(dir);
    }

    public void moveBullets() {
        for(int i=0; i<bullets.size(); i++) {
            Rectangle b = bullets.get(i);
            b.y -= speed;
            b.x += (directions.get(i) * 4);         // for diagonal movement
            if(b.y < 0) {
                removeBullet(i);
                i--;
            }
        }
        repaint();      // for drawing new positions
    }

    public void removeBullet(int i) {
        bullets.remove(i);
        directions.remove(i);
    }

    public ArrayList<Rectangle> getBulletRects() { return bullets; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);        // clean the panel
        g.setColor(Color.YELLOW);
        for(Rectangle b : bullets)
            g.fillRect(b.x, b.y, b.width, b.height);        // draw each rectangle
    }
}