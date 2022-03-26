public class Missile {
    //class for player character - movement and missile instantiation

    // attributes of player character
    private double X, Y;
    private boolean missileIsActive; // is missile eliminated or not
    private int angle; //use this in rotation calculation

    public Missile(double x, double y, boolean active) {
        this.X = x;
        this.Y = y;
        this.missileIsActive = active;
        this.angle = 0;
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

    public void calcRotation(int angle, double speed){
        if (angle == 0){
            this.Y += speed;
        } else {
            double x = speed * Math.cos(angle * Math.PI / 180);
            double y = speed * Math.sin(angle * Math.PI / 180);

            this.X += x;
            this.Y += y;
        }
    }
}

