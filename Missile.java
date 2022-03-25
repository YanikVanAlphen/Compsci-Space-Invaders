public class Missile {
    //class for player character - movement and missile instantiation

    // attributes of player character
    private double X, Y;
    private boolean missileIsActive; // is missile eliminated or not

    public Missile(double x, double y, boolean active) {
        this.X = x;
        this.Y = y;
        this.missileIsActive = active;
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
}

