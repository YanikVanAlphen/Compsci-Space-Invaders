public class Bunker {
    BunkerBlock[][] bunkerArray = new BunkerBlock[3][5]; // Bunker class is comprised out of a 2D array of BunkerBlock instances
    private double bunkerLength; // length of  whole bunker
    private double bunkerWidth;  // width of whole bunker

    public Bunker(double length, double width) {
        this.bunkerLength = length;
        this.bunkerWidth = width;

        createBunker();
    }

    public double getBunkerLength() {
        return bunkerLength;
    }

    public double getBunkerWidth() {
        return bunkerWidth;
    }

    public void createBunker() { // initialize new Bunker
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                bunkerArray[x][y] = new BunkerBlock(bunkerWidth / 5, bunkerLength / 3, true);
            }
        }
    }

    public void placeBunker(double referenceXPos, double referenceYPos) { // place a bunker and set coordinates for all elements/blocks in bunker according to inital reference coordinates

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                bunkerArray[x][y].setXPos(referenceXPos + (y * bunkerArray[x][y].getXlength()));
                bunkerArray[x][y].setYPos(referenceYPos - (x * bunkerArray[x][y].getYlength()));
            }
        }
    }

    // using same logic as with collision of missile and enemies, method checks if any given missile is within the hitbox of a bunker block
    public boolean checkCollision(int xIndex, int yIndex, double missileXPos, double missileYPos, double missileRadius, double bunkerBlockLength, double bunkerBlockWidth) {
        if ((missileXPos > bunkerArray[xIndex][yIndex].getXPos() - bunkerBlockWidth - missileRadius) &&
                (missileXPos < bunkerArray[xIndex][yIndex].getXPos() + bunkerBlockWidth + missileRadius) &&
                (missileYPos > bunkerArray[xIndex][yIndex].getYPos() - bunkerBlockLength - missileRadius) &&
                (missileYPos < bunkerArray[xIndex][yIndex].getYPos() + bunkerBlockLength + missileRadius) &&
                bunkerArray[xIndex][yIndex].getActive()) {
            return true;
        } else {
            return false;
        }
    }

    public double getXCoord(int indexPosX, int indexPosY) {
        return bunkerArray[indexPosX][indexPosY].getXPos();
    }

    public double getYCoord(int indexPosX, int indexPosY) {
        return bunkerArray[indexPosX][indexPosY].getYPos();
    }

    public boolean getActive(int indexPosX, int indexPosY) {
        return bunkerArray[indexPosX][indexPosY].getActive();
    }

    public void setActive(int indexPosX, int indexPosY, boolean state) {
        bunkerArray[indexPosX][indexPosY].setActive(state);
    }
}
