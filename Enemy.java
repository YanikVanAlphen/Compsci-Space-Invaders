import javax.swing.*;

public class Enemy {
    //class for enemy

    // Instance Variables:
    private double X, Y; // x, y coordinate of centre of enemy
    private boolean enemyIsActive; //indicates whether enemy has been eliminated or not
    private ImageIcon enemyPicture = new ImageIcon("images/enemy.png");

    // Constructor:
    public Enemy(double x, double y) {
        this.X = x;
        this.Y = y;
        this.enemyIsActive = true;
    }

    // Instance Methods:
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public boolean getActive() {
        return enemyIsActive;
    }

    public ImageIcon getEnemyPicture() {
        return enemyPicture;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setActive(boolean active) {
        enemyIsActive = active;
    }
}
