public class PlayerCharacter {
    //class for player character - movement and missile instantiation

    // attributes of player character
    private double X, Y;
    private int playerHealth = 100;
    private int angle = 0;

    public PlayerCharacter(double x, double y) {
        this.X = x;
        this.Y = y;
    }

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
    public void setangle( int theta) {
        angle = theta;
    }

    public int getangle() {
        return angle;
    }
}
