package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Scoreboard extends JPanel {
    private int score = 0;

    private int[] highScores = new int[5];

    private int timeLeft = 30;
    private boolean isGameOver = false;
    private boolean isGameWon = false;

    private String powerUpMsg = "";
    private long msgExpiryTime = 0;
    private int w, h;

    public Scoreboard(int w, int h) {
        this.w = w;
        this.h = h;
        this.setOpaque(false);
        loadHighScores();
    }

    private void sortScores() {
        int n = highScores.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (highScores[j] < highScores[j + 1]) {
                    int temp = highScores[j];
                    highScores[j] = highScores[j + 1];
                    highScores[j + 1] = temp;
                }
            }
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public void updateLeaderboard() {

        if (score > highScores[4]) {
            highScores[4] = score;
            sortScores();
            saveHighScores();
        }
    }

    private void loadHighScores() {
        try {
            File f = new File("leaderboard.txt");
            if(f.exists()){
                BufferedReader r = new BufferedReader(new FileReader(f));       // Reads text
                for(int i=0; i<5; i++) {
                    String line = r.readLine();
                    if(line != null)
                        highScores[i] = Integer.parseInt(line.trim());
                }
                r.close();
            }
        } catch (Exception e) {}
    }

    private void saveHighScores() {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("leaderboard.txt"));
            for(int i=0; i<5; i++) {
                wr.write(String.valueOf(highScores[i]));
                wr.newLine();
            }
            wr.close();
        } catch (Exception e) {}
    }

    public void updateTime(int t) {
        this.timeLeft = t;
        if(System.currentTimeMillis() > msgExpiryTime)  // for removing power up message
            powerUpMsg = "";
        repaint();
    }

    public void showPowerUpMessage(String msg) {    // for printing power up message
        this.powerUpMsg = msg;
        this.msgExpiryTime = System.currentTimeMillis() + 2000;
        repaint();
    }

    public void gameOver() { isGameOver = true; isGameWon = false; updateLeaderboard(); repaint(); }
    public void displayWinMessage() { isGameOver = true; isGameWon = true; updateLeaderboard(); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);        // for clearing the panel
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // for smooth rendering

        int hudY = h - 60;
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(0, hudY, w, 60);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.CYAN); g2.drawString("Time: " + timeLeft, 20, hudY + 35);
        g2.setColor(Color.YELLOW); g2.drawString("Score: " + score, 180, hudY + 35);
        g2.setColor(Color.WHITE); g2.drawString("Top: " + highScores[0], 360, hudY + 35);

        if (!powerUpMsg.isEmpty()) {
            g2.setFont(new Font("Verdana", Font.BOLD, 22));
            g2.setColor(Color.ORANGE);
            int txtW = g2.getFontMetrics().stringWidth(powerUpMsg);
            g2.drawString(powerUpMsg, (w - txtW)/2, h/2 - 100);
        }

        if (isGameOver) {
            g2.setColor(new Color(0, 0, 0, 240));
            g2.fillRect(0, 0, w, h);

            g2.setFont(new Font("Verdana", Font.BOLD, 40));
            g2.setColor(isGameWon ? Color.GREEN : Color.RED);
            String title = isGameWon ? "MISSION CLEARED" : "GAME OVER";
            int tw = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (w - tw)/2, 150);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString("--- TOP 5 LEADERBOARD ---", (w - 300)/2 + 20, 220);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));

            for(int i=0; i<5; i++) {
                String s = (i+1) + ". " + highScores[i] + " pts";
                if(highScores[i] == score) g2.setColor(Color.GREEN);        // Highlight current score
                else g2.setColor(Color.WHITE);

                g2.drawString(s, (w - 100)/2, 260 + (i*30));
            }

            g2.setColor(Color.CYAN);
            g2.drawString("Your Score: " + score, (w - 140)/2, 450);
        }
    }
}