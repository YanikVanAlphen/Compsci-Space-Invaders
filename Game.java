public class Game {
    public static final int WIDTH = 600; //width of canvas in pixels
    public static final int HEIGHT = 600; //height of canvas in pixels
    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)
    public static final double ENEMY_RADIUS = 0.5; //radius of enemy
    public static final double PLAYER_RADIUS = 0.25; //radius of player
    public static final double MISSILE_RADIUS = 0.2; //radius of missile
    public static double VX = 0.060;
    public static final int ENEMY_NUM = 5; //5 by 5 enemy block
    public static final int MISSILE_NUM = 5; //Have max 5 missiles in play at a time

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

        Enemy[][] enemies = new Enemy[ENEMY_NUM][ENEMY_NUM];

        Missile[] missiles = new Missile[MISSILE_NUM]; // declare array of missile objects


        PlayerCharacter player = new PlayerCharacter(5.0, 0.5); //player character instantiated
        createPlayer(player);
        createEnemies(ENEMY_NUM, enemies);
        createMissiles(MISSILE_NUM, missiles);

        while (true) {
            /*if (StdDraw.isKeyPressed(114)) { // 114 = ascii for "r"; doesn't work
                System.out.println("RESTART");
                newGame();
            }*/
            StdDraw.enableDoubleBuffering();

            updateEnemies(ENEMY_NUM, enemies); //got rid of player and missiles passed as arguments - no longer need them in this method
            updatePlayer(player, missiles);
            displayMissiles(missiles);  //display missiles



            StdDraw.show();
            StdDraw.pause(20);
            StdDraw.clear();

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

    public static void createMissiles(int M, Missile[] missiles) {
        for (int i = 0; i < M; i++) { //initialise array of missiles
            Missile missile1 = new Missile(0.0, 0.0, false);
            missiles[i] = missile1;
        }
    }

    /* Absolute value function -- makes sure that player moves in correct direction
       as VX variable oscillates between positive and negative */
    public static double calcAbsoluteValue(double value) {
        if (value > 0)
            return value;
        else
            return -value;
    }

    public static void updatePlayer(PlayerCharacter player, Missile[] missiles) {

        if (StdDraw.isKeyPressed(37)) { // left arrow :37
            if (player.getX() >= 0 + PLAYER_RADIUS) {
                player.setX(player.getX() - 2 * calcAbsoluteValue(VX));// move left
            } /*else {
                // set multiplier to zero
            }
        } else {
            // key release*/
        }

        if (StdDraw.isKeyPressed(39)) { // right arrow: 39
            if (player.getX() <= X_SCALE - PLAYER_RADIUS)
                player.setX(player.getX() + 2 * calcAbsoluteValue(VX));// move right

        }

        if (StdDraw.isKeyPressed(32)) { // space-bar: 32
            updateMissiles(missiles, player);
        }

        StdDraw.filledCircle(player.getX(), player.getY(), PLAYER_RADIUS);
    }


    public static void updateMissiles(Missile[] missiles, PlayerCharacter player) {  // update missiles state
       for ( int i = 0; i < MISSILE_NUM; i++) {
           if ( missiles[i].getActive() == false) {
               missiles[i].setActive(true);
               missiles[i].setX(player.getX());
               missiles[i].setY(player.getY());
               break; //use this instead of i i = MISSILE_NUM
           }
       }
    }

    public static void displayMissiles(Missile[] missiles) {   // Draw active missiles and update positions
        for ( int i = 0; i < MISSILE_NUM; i++) {
            if (missiles[i].getActive()) {
                missiles[i].setY(missiles[i].getY() + calcAbsoluteValue(VX));
                //StdOut.print(missiles[i].getY()); //check to see if missile is moving
                StdDraw.filledCircle(missiles[i].getX(), missiles[i].getY(), MISSILE_RADIUS);
            }
        }

    }

    public static void destroy(Missile[] missiles, Enemy[][] enemies){
      for (int i = 0; i < ENEMY_NUM; i++){
          for(int j = 0; j < ENEMY_NUM; j++){
              for(int k = 0; k < MISSILE_NUM; k++){
                  if((enemies[i][j].getX() == missiles[k].getX() && (enemies[i][j].getY() == missiles[k].getY())))

              }
          }
      }
    }

    public static void updateEnemies(int N, Enemy[][] enemies) {

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


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);

            }
        }
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
