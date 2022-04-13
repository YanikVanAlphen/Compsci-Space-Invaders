import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Game {
    //class that enables game to function

    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)

    public static final double ENEMY_RADIUS = 0.5; //radius of enemy
    public static final double PLAYER_RADIUS = 0.35; //radius of player
    public static final double MISSILE_RADIUS = 0.08; //radius of missile

    public static double VX_ENEMY = 0.060; // speed of enemies
    public static double VX_PLAYER = 0.060; // speed of player

    public static final int ENEMY_NUM = 5; //5 by 5 enemy block
    public int ENEMY_KILLED = 0; // count number of enemies killed to advance to next level

    public ArrayList<Missile> MISSILES = new ArrayList<Missile>(); // array list of missiles that can be created and removed where necessary
    public ArrayList<Missile> ENEMY_MISSILES = new ArrayList<Missile>(); // array list of enemy missiles

    public static final double MISSILE_SPEED = 0.15; // speed at which missiles fire
    public static final double ENEMYMISSILE_SPEED = 0.08; //speed at which enemy missiles fire


    public static long FRAME_COUNT = 0;// allows while loop, which keeps the game running, to execute a number of times before missile appears on screen
    public static long FRAME_COUNT1 = 0;  // allows enemy missiles to be spaced out


    public int LIVES = 3; // player lives
    public int SCORE = 0; // score while game is running

    public double LEVELUP = 1.2; // enemy speed multiplier when player moves up a level
    public int SCORE_INCREASE = 1; // score multiplier when player moves up a level
    public double MISSILE_PERIOD = 2.0; // period for enemy missiles to occur

    // initialization of three bunkers, passing height and width of bunker to the constructor
    Bunker bunker1 = new Bunker(1, 2);
    Bunker bunker2 = new Bunker(1, 2);
    Bunker bunker3 = new Bunker(1, 2);

    public void newGame() { // method wherein the game runs
        Menu menu = new Menu();

        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(StdDraw.RED);

        Enemy[][] enemies = new Enemy[ENEMY_NUM][ENEMY_NUM]; //enemy "block"(array) instantiated

        PlayerCharacter player = new PlayerCharacter(5.0, 0.5); //player character instantiated
        createPlayer(player); // draw player to screen
        createEnemies(ENEMY_NUM, enemies);
        createBunkers();

        while (LIVES > 0) {

            StdDraw.setFont();
            StdDraw.text(9, 9.8, "Lives: " + LIVES);
            StdDraw.text(5, 9.8, "Score: " + SCORE);
            StdDraw.text(1, 9.8, "Level: " + SCORE_INCREASE);

            FRAME_COUNT++;
            FRAME_COUNT1++;

            if (StdDraw.isKeyPressed(82)) { // 82 = ascii for "r"
                LIVES = 3;
                reset();
                removeMissiles();
                removeEnemyMissiles();
                newGame();
            }

            if (StdDraw.isKeyPressed(81)) { // 81 = ascii for "q"
                menu.quit();
            }

            if (enemyByPlayer(enemies, player)) {  //player killed by enemies
                ENEMY_KILLED = 0;
                LIVES--;
                if (LIVES <= 0) {
                    writeScore(SCORE);
                    reset();
                    break;  //game over; breaks out of loop
                }

                removeMissiles();
                removeEnemyMissiles();
                menu.livesDecrease(LIVES);
                newGame();
            }

            StdDraw.enableDoubleBuffering();

            updateEnemies(enemies);
            updatePlayer(player);
            updateMissiles();
            updateBunkers(player);
            destroy(enemies);

            if (ENEMY_KILLED == (ENEMY_NUM * ENEMY_NUM)) { //pass to next level when all enemies are destroyed
                ENEMY_KILLED = 0;
                VX_ENEMY *= LEVELUP;
                SCORE_INCREASE++;
                MISSILE_PERIOD *= 0.8; //increase rate of enemy missile fire as FRAME_COUNT1 has to be bigger than a decreasing amount

                removeMissiles();
                removeEnemyMissiles();
                menu.nextlevel(SCORE_INCREASE);
                newGame();
            }

            StdDraw.show();
            StdDraw.pause(20);
            StdDraw.clear(Color.BLACK);

        }

    }

    public void createEnemies(int N, Enemy[][] enemies) { //Enemy array is created and drawn onto canvas
        StdDraw.setPenColor(StdDraw.RED);

        for (int i = 0; i < N; i++) {    //Create array of Enemy objects
            for (int j = 0; j < N; j++) {
                Enemy e1 = new Enemy(0.5 + ((2 * ENEMY_RADIUS) * j), 9.0 - ((2 * ENEMY_RADIUS) * i));
                enemies[i][j] = e1;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.picture(enemies[i][j].getX(), enemies[i][j].getY(), enemies[i][j].getEnemyPicture().toString(), 1, 1);
            }
        }
    }

    public void updateEnemies(Enemy[][] enemies) { // update enemy position, omits inactive enemies that have already been shot

        StdDraw.setPenColor(StdDraw.RED);

        for (int i = 0; i < ENEMY_NUM; i++) {
            for (int j = 0; j < ENEMY_NUM; j++) {
                if ((enemies[i][j].getX() + ENEMY_RADIUS > 10 || enemies[i][j].getX() - ENEMY_RADIUS < 0) && enemies[i][j].getActive() == true) {
                    VX_ENEMY = -VX_ENEMY;
                    for (int m = 0; m < ENEMY_NUM; m++) {
                        for (int n = 0; n < ENEMY_NUM; n++) {
                            enemies[m][n].setY(enemies[m][n].getY() - ENEMY_RADIUS);
                        }
                    }
                    i = ENEMY_NUM;
                    j = ENEMY_NUM;
                }
            }
        }

        for (int i = 0; i < ENEMY_NUM; i++) {
            for (int j = 0; j < ENEMY_NUM; j++) {
                enemies[i][j].setX(enemies[i][j].getX() + VX_ENEMY);
            }
        }


        for (int i = 0; i < ENEMY_NUM; i++) {
            for (int j = 0; j < ENEMY_NUM; j++) {
                if (enemies[i][j].getActive()) {
                    StdDraw.picture(enemies[i][j].getX(), enemies[i][j].getY(), enemies[i][j].getEnemyPicture().toString(), 1, 1);
                }

            }
        }

        if ((MISSILE_PERIOD != 2.0) && FRAME_COUNT1 > MISSILE_PERIOD * 40) { //creates random enemy missile
            for (int i = 0; i < ENEMY_NUM * ENEMY_NUM; i++) {
                int n = (int) (Math.random() * (ENEMY_NUM));
                int m = (int) (Math.random() * (ENEMY_NUM));
                if (enemies[n][m].getActive()) {
                    ENEMY_MISSILES.add(new Missile(enemies[n][m].getX(), enemies[n][m].getY(), 0));
                    FRAME_COUNT1 = 0;
                    break;
                }
            }
        }

    }

    public boolean enemyByPlayer(Enemy[][] enemies, PlayerCharacter player) { // checks for cases wherein a player is killed
        boolean answer2 = false;
        boolean answer1 = false;

        for (int i = 0; i < ENEMY_NUM; i++) { //killed by enemies
            for (int j = 0; j < ENEMY_NUM; j++) {
                if ((enemies[i][j].getActive()) && (enemies[i][j].getY() - ENEMY_RADIUS < player.getY() + 1)) {
                    answer1 = true;
                    i = ENEMY_NUM;
                    j = ENEMY_NUM;
                }
            }
        }

        for (int i = 0; i < ENEMY_MISSILES.size(); i++) { //killed by enemy missiles
            if (Math.sqrt(Math.pow(ENEMY_MISSILES.get(i).getY() - player.getY(), 2) + Math.pow(ENEMY_MISSILES.get(i).getX() - player.getX(), 2)) < MISSILE_RADIUS + PLAYER_RADIUS) {
                answer2 = true;
            }
        }

        return answer1 || answer2;
    }

    public void removeEnemyMissiles() { //remove all enemy missiles
        for (int i = 0; i < ENEMY_MISSILES.size(); i++) {
            ENEMY_MISSILES.remove(i);
        }
    }

    public void destroy(Enemy[][] enemies) { // event that an enemy is shot by a missile
        for (int i = 0; i < ENEMY_NUM; i++) {
            for (int j = 0; j < ENEMY_NUM; j++) {
                for (int k = 0; k < MISSILES.size(); k++) {
                    Enemy e = enemies[i][j];
                    Missile m = MISSILES.get(k);

                    if (e.getActive()) {
                        if ((m.getX() > e.getX() - ENEMY_RADIUS - MISSILE_RADIUS) &&
                                (m.getX() < e.getX() + ENEMY_RADIUS + MISSILE_RADIUS) &&
                                (m.getY() > e.getY() - ENEMY_RADIUS - MISSILE_RADIUS) &&
                                (m.getY() < e.getY() + ENEMY_RADIUS + MISSILE_RADIUS)) {
                            MISSILES.remove(k);
                            e.setActive(false);
                            SCORE += (SCORE_INCREASE * 10);
                            ENEMY_KILLED++;
                            StdAudio.playInBackground("images/missileExplode.wav");
                        }
                    }
                }
            }
        }
    }

    public void createPlayer(PlayerCharacter player) { //draws player on canvas
        StdDraw.picture(player.getX(), player.getY(), player.getPlayerPicture().toString(), 1, 1, (-1) * player.getAngle());
    }

    public void updatePlayer(PlayerCharacter player) { //updates position of player according to keys pressed; also fires missiles if space-bar is pressed

        if (StdDraw.isKeyPressed(65)) { //65 = keycode for 'a'; rotate left
            if (player.getAngle() >= -90) {
                player.setAngle(player.getAngle() - 5);
            }
        }

        if (StdDraw.isKeyPressed(68)) { //68 = keycode for 'd'; rotate right
            if (player.getAngle() <= 90) {
                player.setAngle(player.getAngle() + 5);
            }
        }

        if (StdDraw.isKeyPressed(37)) { // left arrow :37
            if (player.getX() >= 0 + PLAYER_RADIUS) {
                player.setX(player.getX() - 2 * Math.abs(VX_PLAYER));// move left
            }
        }

        if (StdDraw.isKeyPressed(39)) { // right arrow: 39
            if (player.getX() <= X_SCALE - PLAYER_RADIUS)
                player.setX(player.getX() + 2 * Math.abs(VX_PLAYER));// move right

        }

        if (FRAME_COUNT > 5 && StdDraw.isKeyPressed(32)) { // space-bar: 32
            FRAME_COUNT = 0;
            MISSILES.add(new Missile(player.getX(), player.getY(), player.getAngle()));
            StdAudio.playInBackground("images/missileShoot.wav");
        }
        StdDraw.picture(player.getX(), player.getY(), player.getPlayerPicture().toString(), 1, 1, (-1) * player.getAngle());
    }

    public void updateMissiles() {  //update and draw missile on screen
        for (int i = 0; i < MISSILES.size(); i++) { //update missile position and display;
            if (!checkBunkerHit(MISSILES.get(i).getX(), MISSILES.get(i).getY(), i, 1)) {
                double theta = (double) MISSILES.get(i).getAngle() * Math.PI / 180;
                MISSILES.get(i).setX(MISSILES.get(i).getX() + (Math.sin(theta) * MISSILE_SPEED));
                MISSILES.get(i).setY(MISSILES.get(i).getY() + (Math.cos(theta) * MISSILE_SPEED));

                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledCircle(MISSILES.get(i).getX(), MISSILES.get(i).getY(), MISSILE_RADIUS);
            }
        }

        for (int i = 0; i < ENEMY_MISSILES.size(); i++) { //update enemy missile position and display;
            if (!checkBunkerHit(ENEMY_MISSILES.get(i).getX(), ENEMY_MISSILES.get(i).getY(), i, 0)) {
                ENEMY_MISSILES.get(i).setY(ENEMY_MISSILES.get(i).getY() - ENEMYMISSILE_SPEED);

                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.filledCircle(ENEMY_MISSILES.get(i).getX(), ENEMY_MISSILES.get(i).getY(), MISSILE_RADIUS);
            }
        }

        for (int i = 0; i < MISSILES.size(); i++) { //remove out of bounds missiles
            if (MISSILES.get(i).getY() + MISSILE_RADIUS > Y_SCALE || MISSILES.get(i).getX() + MISSILE_RADIUS > X_SCALE || MISSILES.get(i).getX() - MISSILE_RADIUS < 0)
                MISSILES.remove(i);
        }

        for (int i = 0; i < ENEMY_MISSILES.size(); i++) { //remove out of bounds enemy missiles
            if ((ENEMY_MISSILES.get(i).getY() - MISSILE_RADIUS) < 0)
                ENEMY_MISSILES.remove(i);
        }
    }

    public void removeMissiles() { //remove all missiles
        for (int i = 0; i < MISSILES.size(); i++) {
            MISSILES.remove(i);
        }
    }

    public void createBunkers() { // passing reference x- and y- coordinates to each bunker. These coordinates are the coordinates of the top- leftmost bunker block
        // the rest of the bunker blocks receive coordinates based off of the reference coordinates
        bunker1.placeBunker(1.0, 2.5);
        placeBunker(bunker1);

        bunker2.placeBunker(4.0, 2.5);
        placeBunker(bunker2);

        bunker3.placeBunker(7.0, 2.5);
        placeBunker(bunker3);

    }

    public void placeBunker(Bunker bunker) { // draw bunkers on screen
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                StdDraw.filledRectangle(bunker.getXCoord(x, y), bunker.getYCoord(x, y), bunker.getBunkerWidth() / 10, bunker.getBunkerHeight() / 6);
            }
        }
    }

    public void updateBunkers(PlayerCharacter player) { //updates bunker positions, omits inactive bunkers (bunkers that have been shot already)
        StdDraw.setPenColor(Color.RED);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                if (bunker1.getActive(x, y))
                    StdDraw.filledRectangle(bunker1.getXCoord(x, y), bunker1.getYCoord(x, y), bunker1.getBunkerWidth() / 10, bunker1.getBunkerHeight() / 6);

                if (bunker2.getActive(x, y))
                    StdDraw.filledRectangle(bunker2.getXCoord(x, y), bunker2.getYCoord(x, y), bunker2.getBunkerWidth() / 10, bunker2.getBunkerHeight() / 6);

                if (bunker3.getActive(x, y))
                    StdDraw.filledRectangle(bunker3.getXCoord(x, y), bunker3.getYCoord(x, y), bunker3.getBunkerWidth() / 10, bunker3.getBunkerHeight() / 6);
            }
        }
    }

    public boolean checkBunkerHit(double missileXPos, double missileYPos, int missileIndex, int missileType) { // compares coordinates of missiles and bunker blocks, checks if a bunker block is hit
        boolean sentinel = false;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                if (bunker1.checkCollision(x, y, missileXPos, missileYPos, MISSILE_RADIUS, bunker1.getBunkerHeight() / 10, bunker1.getBunkerWidth() / 6)) {
                    bunker1.setActive(x, y, false);
                    if (missileType == 1) {
                        MISSILES.remove(missileIndex);
                    }
                    if (missileType == 0) {
                        ENEMY_MISSILES.remove(missileIndex);
                    }
                    sentinel = true;
                    break;
                }

                if (bunker2.checkCollision(x, y, missileXPos, missileYPos, MISSILE_RADIUS, bunker2.getBunkerHeight() / 10, bunker2.getBunkerWidth() / 6)) {
                    bunker2.setActive(x, y, false);
                    if (missileType == 1) {
                        MISSILES.remove(missileIndex);
                    }
                    if (missileType == 0) {
                        ENEMY_MISSILES.remove(missileIndex);
                    }
                    sentinel = true;
                    break;
                }

                if (bunker3.checkCollision(x, y, missileXPos, missileYPos, MISSILE_RADIUS, bunker3.getBunkerHeight() / 10, bunker3.getBunkerWidth() / 6)) {
                    bunker3.setActive(x, y, false);
                    if (missileType == 1) {
                        MISSILES.remove(missileIndex);
                    }
                    if (missileType == 0) {
                        ENEMY_MISSILES.remove(missileIndex);
                    }
                    sentinel = true;
                    break;
                }
            }
        }
        return sentinel;
    }

    public static void writeScore(int score) { //writes score achieved during the game to text file that stores all scores ever achieved
        File scoreText = new File("HighScore.txt");

        if (!scoreText.exists()) {
            try {
                scoreText.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fwScore = null;
        try {
            fwScore = new FileWriter(scoreText, true);
            PrintWriter pwScore = new PrintWriter(fwScore);

            pwScore.println(score);
            pwScore.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void reset() { // resets initial values of game
        SCORE = 0;
        VX_ENEMY = 0.060;
        VX_PLAYER = 0.060;
        SCORE_INCREASE = 1;
        MISSILE_PERIOD = 2.0;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                bunker1.setActive(x, y, true);
                bunker2.setActive(x, y, true);
                bunker3.setActive(x, y, true);
            }
        }
    }
}
