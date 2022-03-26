public class Enemy {
    // Instance Variables:
    private double X, Y;
    private boolean enemyIsActive; //has enemy been eliminated or not?

    // Constructor/s:
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

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setActive(boolean active){
        enemyIsActive = active;
    }
}
