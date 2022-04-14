import javax.swing.*;

public class PlayerCharacter {
    //class for player character - movement and missile instantiation

    // Instance Variables:
    private double X, Y; // x, y coordinates of  centre of player
    private int angle = 0; //angle player is at due to rotation
    private ImageIcon playerPicture = new ImageIcon("images/player.png");

    //Constructor:
    public PlayerCharacter(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    //Instance Methods:
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getAngle() {
        return angle;
    }

    public ImageIcon getPlayerPicture() {
        return playerPicture;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setAngle(int theta) {
        angle = theta;
    }

}
