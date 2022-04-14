public class BunkerBlock { // singular bunker block, many instances of the bunker block make up a full bunker, used to create a degrading effect when a bunker is shot at
    //attributes - width length and active state
    private double Xlength;
    private double Ylength;
    private boolean isActive;

    // coordinates on screen
    private double xPos;
    private double yPos;

    public BunkerBlock(double x, double y, boolean state) {
        this.Xlength = x;
        this.Ylength = y;
        this.isActive = state;
    }

    public double getXlength() {
        return Xlength;
    }

    public double getYlength() {
        return Ylength;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean state) {
        isActive = state;
    }

    public void setXPos(double xPosition) {
        xPos = xPosition;
    }

    public void setYPos(double yPosition) {
        yPos = yPosition;
    }
}
