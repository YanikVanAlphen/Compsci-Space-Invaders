import java.util.ArrayList;

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

    public ArrayList<Missile> MISSILES = new ArrayList<Missile>();
    public static final double MISSILE_SPEED = 0.15;
    public static long FRAME_COUNT = 0;

    public static void main(String[] args) {
        Game g = new Game();  // some wierd case where my class needs a main, will look into it
    }

    public Game(){
        createGame();
    }

    public void createGame() {
        createCanvas();
        startMenu();
    }

    public void createCanvas() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, X_SCALE);
        StdDraw.setYscale(0, Y_SCALE);

        for (int i = 0; i <= X_SCALE; i++) { //drawing lines to help with initial placement of drawings
            StdDraw.line(i, 0, i, Y_SCALE);
            StdDraw.line(0, i, X_SCALE, i);
        }
    }

    public void startMenu() { // relook at while statement
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

    public void newGame() {

        StdDraw.clear();

        StdDraw.setPenColor(StdDraw.RED);

        Enemy[][] enemies = new Enemy[ENEMY_NUM][ENEMY_NUM];

        PlayerCharacter player = new PlayerCharacter(5.0, 0.5); //player character instantiated
        createPlayer(player);
        createEnemies(ENEMY_NUM, enemies);

        while (true) {
            FRAME_COUNT++;
            /*if (StdDraw.isKeyPressed(114)) { // 114 = ascii for "r"; doesn't work
                System.out.println("RESTART");
                newGame();
            }*/
            StdDraw.enableDoubleBuffering();

            updateEnemies(ENEMY_NUM, enemies);
            updatePlayer(player);
            updateMissiles(); //update and draw missile on screen
            destroy(enemies);



            StdDraw.show();
            StdDraw.pause(20);
            StdDraw.clear();

        }

    }

    public void createPlayer(PlayerCharacter player) {

        StdDraw.filledCircle(player.getX(), player.getY(), 0.25);
    }

    public void createEnemies(int N, Enemy[][] enemies) {

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


    public void updatePlayer(PlayerCharacter player) {

        if (StdDraw.isKeyPressed(37)) { // left arrow :37
            if (player.getX() >= 0 + PLAYER_RADIUS) {
                player.setX(player.getX() - 2 * Math.abs(VX));// move left
            }
        }

        if (StdDraw.isKeyPressed(39)) { // right arrow: 39
            if (player.getX() <= X_SCALE - PLAYER_RADIUS)
                player.setX(player.getX() + 2 * Math.abs(VX));// move right

        }

        if (FRAME_COUNT > 5 && StdDraw.isKeyPressed(32)) { // space-bar: 32
            FRAME_COUNT = 0;
            MISSILES.add(new Missile(player.getX(), player.getY(), true));
        }

        StdDraw.filledCircle(player.getX(), player.getY(), PLAYER_RADIUS);
    }


    public void updateMissiles() {  // update missiles state
       for(int i = 0; i < MISSILES.size(); i++){
           MISSILES.get(i).setY(MISSILES.get(i).getY() + MISSILE_SPEED);

           StdDraw.filledCircle(MISSILES.get(i).getX(), MISSILES.get(i).getY() + PLAYER_RADIUS, MISSILE_RADIUS);

           System.out.println(MISSILES.get(i).getX() + " " + MISSILES.get(i).getY());

           if (MISSILES.get(i).getY() + MISSILE_RADIUS > Y_SCALE)
               MISSILES.remove(i);
       }
    }


    public void destroy(Enemy[][] enemies){
      for(int i = 0; i < ENEMY_NUM; i++){
          for(int j = 0; j < ENEMY_NUM; j++){
              for(int k = 0; k < MISSILES.size(); k++){
                  Enemy e = enemies[i][j];
                  Missile m = MISSILES.get(k);

                  if(e.getActive()) {
                      if ((m.getX() >  e.getX() - ENEMY_RADIUS - MISSILE_RADIUS) && (m.getX() <  e.getX() + ENEMY_RADIUS + MISSILE_RADIUS)
                              && (m.getY() >  e.getY() - ENEMY_RADIUS - MISSILE_RADIUS) && (m.getY() <  e.getY() + ENEMY_RADIUS + MISSILE_RADIUS)) {
                          MISSILES.remove(k);
                          e.setActive(false);
                      }
                  }
              }
          }
      }
    }

    public void updateEnemies(int N, Enemy[][] enemies) {

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
                if (enemies[i][j].getActive()) {
                    StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);
                }

            }
        }
    }
}
