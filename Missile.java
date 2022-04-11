public class Missile {
    //class for missile

    // Instance variables:
    private double X, Y; // x,y coordinates of centre of missile
    private int angle; //angle of gun according to angle of player (gun attached to player)

    //Constructor:
    public Missile(double x, double y, int theta) {
        this.X = x;
        this.Y = y;
        this.angle = theta;
    }

    //Instance methods:
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int theta) {
        angle = theta;
    }
}
