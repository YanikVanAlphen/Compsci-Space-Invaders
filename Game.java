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
    public static final double ENEMYMISSILE_SPEED = 0.08;


    public static long FRAME_COUNT = 0;// allows while loop, which keeps the game running, to execute a number of times before missile appears on screen
                                       // allows missiles to appear spaced apart
    public static long FRAME_COUNT1 = 0;  // allows enemy missiles to be spaced out
    public static long FRAME_COUNT2 = 0;                   //frame count for animations



    public int LIVES = 3; // player lives
    public int SCORE = 0; // score while game is running

    public double LEVELUP = 1.2; // enemy speed multiplier when player moves up a level
    public int SCORE_INCREASE = 1; // score multiplier when player moves up a level
    public double MISSILE_PERIOD = 2.0; // period for enemy missiles to occur


    public void newGame() {
        Menu menu = new Menu();
        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(StdDraw.RED);

        Enemy[][] enemies = new Enemy[ENEMY_NUM][ENEMY_NUM]; //enemy "block"(array) instantiated

        PlayerCharacter player = new PlayerCharacter(5.0, 0.5); //player character instantiated
        createPlayer(player);
        createEnemies(ENEMY_NUM, enemies);

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
                removeENEMYMissiles();
                newGame();
            }

            if (StdDraw.isKeyPressed(81)) { // 81 = ascii for "q"
                menu.quit();
            }

            if (enemyByPlayer(enemies, player)) {  //player killed by enemies
                ENEMY_KILLED = 0;
                LIVES--;
                if (LIVES <= 0) {   //ADD REMOVE MISSILE FUNCTIONS?
                    gameover();
                    writeScore(SCORE);
                    reset();
                    break;
                    //game over
                }
                removeMissiles();
                removeENEMYMissiles();
                livesdecrease(LIVES);
                newGame();
            }

            StdDraw.enableDoubleBuffering();

            updateEnemies(ENEMY_NUM, enemies);
            updatePlayer(player);
            updateMissiles();
            destroy(enemies);

            if (ENEMY_KILLED == (ENEMY_NUM * ENEMY_NUM)) { //pass to next level when all enemies are destroyed
                ENEMY_KILLED = 0;
                VX_ENEMY *= LEVELUP;
                SCORE_INCREASE++;
                MISSILE_PERIOD *= 0.8; //increase rate of enemy missile fire
                removeMissiles();
                removeENEMYMissiles();
                nextlevel(SCORE_INCREASE);
                newGame();
            }

            StdDraw.show();
            StdDraw.pause(20);
            StdDraw.clear(Color.BLACK);

        }
        //System.out.println(LIVES + " " + player.getPlayerLives());

    }

    public void removeMissiles() { //remove all missiles
        for (int i = 0; i < MISSILES.size(); i++) {
            MISSILES.remove(i);
        }
    }

    public void removeENEMYMissiles() { //remove all enemy missiles
        for (int i = 0; i < ENEMY_MISSILES.size(); i++) {
            ENEMY_MISSILES.remove(i);
        }
    }

    public boolean enemyByPlayer(Enemy[][] enemies, PlayerCharacter player) {
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
            if ( Math.sqrt( Math.pow(ENEMY_MISSILES.get(i).getY() - player.getY(),2) + Math.pow(ENEMY_MISSILES.get(i).getX() - player.getX(),2)) < MISSILE_RADIUS + PLAYER_RADIUS){
                answer2 = true;
            }
        }

        if (answer1||answer2)
            return true;
        else return false;
    }

    public void createPlayer(PlayerCharacter player) { //draws player on canvas
        StdDraw.picture(player.getX(), player.getY(), "images/player.jpg", 1, 1);
    }

    public void createEnemies(int N, Enemy[][] enemies) { //Enemy array is created and drawn onto canvas

        for (int i = 0; i < N; i++) {    //Create array of Enemy objects
            for (int j = 0; j < N; j++) {
                Enemy e1 = new Enemy(0.5 + ((2 * ENEMY_RADIUS) * j), 9.0 - ((2 * ENEMY_RADIUS) * i));
                enemies[i][j] = e1;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);
            }
        }
    }


    public void updatePlayer(PlayerCharacter player) { //updates position of player due to keys pressed; also fires missiles if space-bar is pressed

        if (StdDraw.isKeyPressed(65)) { //65 = ascii for 'a'; rotate left
            if (player.getAngle() >= -90) {
                player.setAngle(player.getAngle() - 1);
            }
        }

        if (StdDraw.isKeyPressed(68)) { //68 = ascii for 'd'; rotate right
            if (player.getAngle() <= 90) {
                player.setAngle(player.getAngle() + 1);
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
        StdDraw.picture(player.getX(), player.getY(), "images/player.jpg", 1, 1);
    }


    public void updateMissiles() {  //update and draw missile on screen

        for (int i = 0; i < MISSILES.size(); i++) { //update missile position and display;

            double theta = (double) MISSILES.get(i).getAngle() * Math.PI / 180;
            MISSILES.get(i).setX(MISSILES.get(i).getX() + (Math.sin(theta) * MISSILE_SPEED));
            MISSILES.get(i).setY(MISSILES.get(i).getY() + (Math.cos(theta) * MISSILE_SPEED));

            StdDraw.setPenColor(Color.WHITE);
            StdDraw.filledCircle(MISSILES.get(i).getX(), MISSILES.get(i).getY(), MISSILE_RADIUS);
            StdDraw.setPenColor(Color.RED);
            System.out.println(MISSILES.get(i).getX() + " " + MISSILES.get(i).getY());

        }

        for (int i = 0; i < ENEMY_MISSILES.size(); i++) { //update enemy missile position and display;

            ENEMY_MISSILES.get(i).setY(ENEMY_MISSILES.get(i).getY() - ENEMYMISSILE_SPEED);
            StdDraw.setPenColor(Color.YELLOW);
            StdDraw.filledCircle(ENEMY_MISSILES.get(i).getX(), ENEMY_MISSILES.get(i).getY(), MISSILE_RADIUS);
            StdDraw.setPenColor(Color.RED);
            System.out.println(ENEMY_MISSILES.get(i).getX() + " " + ENEMY_MISSILES.get(i).getY());

        }

        for (int i = 0; i < MISSILES.size(); i++) { //remove out of bounds missiles
            if (MISSILES.get(i).getY() + MISSILE_RADIUS > Y_SCALE|| MISSILES.get(i).getX() + MISSILE_RADIUS > X_SCALE || MISSILES.get(i).getX() - MISSILE_RADIUS < 0)
                MISSILES.remove(i);
        }

        for (int i = 0; i < ENEMY_MISSILES.size(); i++) { // remove out of bounds enemy missiles
            if ((ENEMY_MISSILES.get(i).getY() - MISSILE_RADIUS) < 0)
                ENEMY_MISSILES.remove(i);
        }

        /*int a = MISSILES.size();
        int b = ENEMY_MISSILES.size();


        for (int i = 0; i < a; i++) {    //missile and enemy missile collision
            for (int j = 0; j < b; j++) {
                if ( Math.sqrt( Math.pow(ENEMY_MISSILES.get(j).getY() - MISSILES.get(i).getY(),2) + Math.pow(ENEMY_MISSILES.get(j).getX() - MISSILES.get(i).getX(),2)) < MISSILE_RADIUS*2){
                    ENEMY_MISSILES.remove(j);
                    MISSILES.remove(i);
                    SCORE += 5;
                    i = a;
                    j = b;
                }
            }
        } */

    }


    public void destroy(Enemy[][] enemies) {
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

    public void updateEnemies(int N, Enemy[][] enemies) {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((enemies[i][j].getX() + ENEMY_RADIUS > 10 || enemies[i][j].getX() - ENEMY_RADIUS < 0) && enemies[i][j].getActive() == true) {
                    VX_ENEMY = -VX_ENEMY;
                    for (int m = 0; m < N; m++) {
                        for (int n = 0; n < N; n++) {
                            enemies[m][n].setY(enemies[m][n].getY() - ENEMY_RADIUS);
                        }
                    }
                    i = N;
                    j = N;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                enemies[i][j].setX(enemies[i][j].getX() + VX_ENEMY);
                // System.out.println(enemies[i][j].getX() + " " + enemies[i][j].getY()); //Check for position of enemy objects in array

            }
        }


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (enemies[i][j].getActive()) {
                    StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);
                }

            }
        }

        if ( !(MISSILE_PERIOD == 2.0) && FRAME_COUNT1 > MISSILE_PERIOD*40) { //creates random enemy missile
            for (int i = 0; i < N*N; i++) {
                int n = (int) (Math.random()*N);
                int m = (int) (Math.random()*N);
                if (enemies[n][m].getActive()) {
                    ENEMY_MISSILES.add(new Missile(enemies[n][m].getX(), enemies[n][m].getY(), 0));
                    FRAME_COUNT1 = 0;
                    break;
                }
            }
        }

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

    public void reset() {
        SCORE = 0;
        VX_ENEMY = 0.060;
        VX_PLAYER = 0.060;
        SCORE_INCREASE = 1;
        MISSILE_PERIOD = 2.0;
    }

    public static void gameover() { //animation for game over
        Font font1 = new Font("Serif", Font.BOLD, 50);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.BOOK_RED);

        while (FRAME_COUNT2 < 110) {

            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();

            if ((FRAME_COUNT2 < 20) || ((FRAME_COUNT2 > 30) && (FRAME_COUNT2 < 50)) || ((FRAME_COUNT2 > 60) && (FRAME_COUNT2 < 80)) || ((FRAME_COUNT2 > 90) && (FRAME_COUNT2 < 110)) )
                StdDraw.text(5, 6.5, "GAME OVER");

            FRAME_COUNT2++;
            StdDraw.show();
            StdDraw.pause(20);

        }
        FRAME_COUNT2 = 0;
    }

    public static void nextlevel(int level) { //animation for next level
        Font font1 = new Font("Serif", Font.BOLD, 50);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.GREEN);

        while (FRAME_COUNT2 < 40) {

            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();

            if (FRAME_COUNT2 < 20)
                StdDraw.text(5, 6.5, "LEVEL: " + (level - 1));
            else
                StdDraw.text(5, 6.5, "LEVEL: " + (level));

            FRAME_COUNT2++;
            StdDraw.show();
            StdDraw.pause(20);

        }
        FRAME_COUNT2 = 0;
    }

    public static void livesdecrease(int lives) { //animation for lives decrease
        Font font1 = new Font("Serif", Font.BOLD, 50);
        StdDraw.setFont(font1);

        while (FRAME_COUNT2 < 60) {

            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();

            if (FRAME_COUNT2 < 30)
                StdDraw.text(5, 6.5, "LIVES: " + (lives + 1));
            else
                StdDraw.text(5, 6.5, "LIVES: " + lives);

            FRAME_COUNT2++;
            StdDraw.show();
            StdDraw.pause(20);

        }
        FRAME_COUNT2 = 0;
    }


}
