public class Game {
    public static final int WIDTH = 600; //width of canvas in pixels
    public static final int HEIGHT = 600; //height of canvas in pixels
    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)
    public static final double ENEMY_RADIUS = 0.5; //radius of enemy
    public static final double PLAYER_RADIUS = 0.25; //radius of player
    public static final double MISSILE_RADIUS = 0.2; //radius of missile
    public static double VX = 0.060;

    public static void main(String[] args) {
        createGame();  // some wierd case where my class needs a main, will look into it
    }

    public static void createGame() {
        createCanvas();
        startMenu();
    }

    public static void createCanvas() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, X_SCALE);
        StdDraw.setYscale(0, Y_SCALE);

        for (int i = 0; i <= X_SCALE; i++) { //drawing lines to help with initial placement of drawings
            StdDraw.line(i, 0, i, Y_SCALE);
            StdDraw.line(0, i, X_SCALE, i);
        }
    }

    public static void startMenu() { // relook at while statement
        StdDraw.clear();
        StdDraw.text(4, 4, "MENU");

        while (!StdDraw.isKeyPressed(10)) { //while enter or "q" hasn't be pressed, start menu screen must remain same
            while (!StdDraw.hasNextKeyTyped()) {
            } // loop until a key is pressed
            System.out.println("WRONG KEY:  " + (int) StdDraw.nextKeyTyped());
        }


        if (StdDraw.isKeyPressed(10)) { // 10 = ascii for ENTER
            newGame();
        } else {
            StdDraw.clear();
            System.out.println("TERMINATING:  " + (int) StdDraw.nextKeyTyped());
            System.exit(1);
        }
    }

    public static void newGame() {

        StdDraw.clear();

        StdDraw.setPenColor(StdDraw.RED);

        int N = 5; //NxN matrix of enemies
        Enemy[][] enemies = new Enemy[N][N];

        PlayerCharacter player = new PlayerCharacter(5.0, 0.5); //player character instantiated
        createPlayer(player);

        Missile missile1 = new Missile(player.getX(), player.getY() + PLAYER_RADIUS, false);

        createEnemies(N, enemies);


        while (true) {
            /*if (StdDraw.isKeyPressed(114)) { // 114 = ascii for "r"; doesn't work
                System.out.println("RESTART");
                newGame();
            }*/
            updateEnemies(N, enemies, player, missile1);
            //updateMissiles();
            //updatePlayer(player);
        }

    }

    public static void createPlayer(PlayerCharacter player) {

        StdDraw.filledCircle(player.getX(), player.getY(), 0.25);
    }

    public static void createEnemies(int N, Enemy[][] enemies) {

        for (int i = 0; i < N; i++) {    //Create array of Enemy objects
            for (int j = 0; j < N; j++) {
                Enemy e1 = new Enemy(0.5 + ((2 * ENEMY_RADIUS) * j), 9.5 - ((2 * ENEMY_RADIUS) * i));
                enemies[i][j] = e1;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), 0.5);
            }
        }
    }

    /* Absolute value function -- makes sure that player moves in correct direction
     * as VX variable oscillates between positive and negative */
    public static double calcAbsoluteValue(double value) {
        if (value > 0)
            return value;
        else
            return -value;
    }

    public static void updatePlayer(PlayerCharacter player, Missile missile1) {
        //Missile[] missile1 = new Missile[N];

        if (StdDraw.isKeyPressed(37)) { // left arrow :37
            if (player.getX() >= 0 + PLAYER_RADIUS) {
                player.setX(player.getX() - 2 * calcAbsoluteValue(VX));// move left
            } else {
                // set multiplier to zero
            }
        } else {
            // key release
        }

        if (StdDraw.isKeyPressed(39)) { // right: arrow 39
            if (player.getX() <= X_SCALE - PLAYER_RADIUS) {
                player.setX(player.getX() + 2 * calcAbsoluteValue(VX));// move right
            } else {
                // set multiplier to zero
            }
        } else {
            // key release
        }

        if (StdDraw.isKeyPressed(32)) { // spacebar
            //missile[count] = createNewMissile(player.getX(), player.getY() + PLAYER_RADIUS); // add missile radius, too
            missile1.setActive(true);
            missile1.setX(player.getX());
            StdDraw.filledCircle(missile1.getX(), missile1.getY(), MISSILE_RADIUS);
        }

        if (missile1.getActive()) {
            updateMissiles(missile1);
        }
        StdDraw.filledCircle(player.getX(), player.getY(), PLAYER_RADIUS);
    }

    public static Missile createNewMissile(double xCoord, double yCoord) {
        Missile newMissile = new Missile(xCoord, yCoord, false);
        return newMissile;
        // make missileArray globally accesable and check howto make it dynamic
    }

    public static void updateMissiles(Missile missile1) {
        missile1.setY(missile1.getY() + calcAbsoluteValue(VX));
        StdOut.print(missile1.getY());
        StdDraw.filledCircle(missile1.getX(), missile1.getY(), MISSILE_RADIUS);
    }

    public static void updateEnemies(int N, Enemy[][] enemies, PlayerCharacter player, Missile missile1) {

        if (enemies[0][N - 1].getX() + ENEMY_RADIUS > 10 || enemies[0][0].getX() - ENEMY_RADIUS < 0) {
            VX = -VX;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    enemies[i][j].setY(enemies[i][j].getY() - 0.5);
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                enemies[i][j].setX(enemies[i][j].getX() + VX);
                // System.out.println(enemies[i][j].getX() + " " + enemies[i][j].getY()); //Check for position of enemy objects in array

            }
        }

        StdDraw.clear();
        updatePlayer(player, missile1);//update player position
        StdDraw.enableDoubleBuffering();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);

            }
        }

        StdDraw.show(20);
    }

    /*public static void keyPressed(KeyEvent key_event) { //event that key pressed
        int keyPress = key_event.getKeyCode();

        if (keyPress == KeyEvent.VK_LEFT) {
            //move left
        }

        if (keyPress == KeyEvent.VK_RIGHT) {
            //move left
        }
    }


    public static void keyReleased(KeyEvent key_event) { //event that key released
        int keyPress = key_event.getKeyCode();
        //dont move
    } */
}
