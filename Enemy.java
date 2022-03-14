public class Enemy {
    // Instance Variables:
    private double X, Y;

    // Constructor/s:
    public Enemy(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    // Instance Methods:
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
}
