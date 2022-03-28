public class PlayerCharacter {
    //class for player character - movement and missile instantiation

    // attributes of player character
    private double X, Y;
    private int playerLives = 3;
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

    public int getPlayerLives() {
        return playerLives;
    }

    public int getangle() {
        return angle;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setPlayerLives(int lives) {
        playerLives = lives;
    }
    
    public void setangle(int theta) {
        angle = theta;
    }

}
