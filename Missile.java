public class Missile {
    //class for player character - movement and missile instantiation

    // attributes of player character
    private double X, Y;
    private boolean missileIsActive; // is missile eliminated or not
    private int angle; 

    public Missile(double x, double y, boolean active, int theta) {
        this.X = x;
        this.Y = y;
        this.missileIsActive = active;
        this.angle = theta;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public boolean getActive() {
        return missileIsActive;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setActive(boolean active) {
        missileIsActive = active;
    }

    public int getangle() {
        return angle;
    }

    public void setangle( int theta ) {
        angle = theta;
    }
}
