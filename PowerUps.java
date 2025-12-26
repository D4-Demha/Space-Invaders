package SpaceInvaders;
import java.util.Stack;

public class PowerUps {
    Scoreboard sb;
    Shooter sh;

    private Stack<Integer> powerUpStack = new Stack<>();

    public PowerUps(Scoreboard sb, Shooter sh) {
        this.sb = sb;
        this.sh = sh;
    }

    public boolean tryTriggerPowerUp() {
        if(Math.random() < 0.20) {
            activateRandomEffect();
            return true;            // Tell Main to start the 5-second timer
        }
        return false;
    }

    private void activateRandomEffect() {
        int r = (int)(Math.random() * 5);

        powerUpStack.push(r);

        applyEffect(r);
    }

    public void revertState() {
        if (!powerUpStack.isEmpty()) {
            powerUpStack.pop();

            if (!powerUpStack.isEmpty()) {
                int previousEffect = powerUpStack.peek();
                applyEffect(previousEffect);
                sb.showPowerUpMessage("REVERTING TO PREVIOUS!");
            } else {

                sh.resetShooter();
            }
        }
    }

    private void applyEffect(int r) {
        String msg = "";
        switch(r) {
            case 0:
                sh.setFastFire();
                msg = "FAST FIRE!";
                break;
            case 1:
                sh.setSlowFire();
                msg = "SLOW FIRE...";
                break;
            case 2:
                sh.setBigShooter();
                msg = "BIG SHOOTER!";
                break;
            case 3:
                sh.setSmallShooter();
                msg = "SMALL SHOOTER!";
                break;
            case 4:
                sh.setMultiBullet(true);
                msg = "MULTI-SHOT!";
                break;
        }
        sb.showPowerUpMessage(msg);
    }
}