public class Game {
    public static final int WIDTH = 600; //width of canvas in pixels
    public static final int HEIGHT = 600; //height of canvas in pixels
    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)
    public static final double ENEMY_RADIUS = 0.5; //radius of enemy
    public static final double PLAYER_RADIUS = 0.25; //radius of player
    public static double VX = 0.060;

    public void createGame() {
        createCanvas();
        startMenu();
    }
    //change is good, love change
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

        int N = 5; //NxN matrix of enemies
        Enemy[][] enemies = new Enemy[N][N];

        CreateEnemies(N, enemies);


        while (true) {
            /*if (StdDraw.isKeyPressed(114)) { // 114 = ascii for "r"; doesn't work
                System.out.println("RESTART");
                newGame();
            }*/
            UpdateEnemies(N, enemies);

        }

    }

    public void CreateEnemies(int N, Enemy[][] enemies) {

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

    public void UpdateEnemies(int N, Enemy[][] enemies) {

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
                System.out.println(enemies[i][j].getX() + " " + enemies[i][j].getY()); //Check for position of enemy objects in array

            }
        }

        StdDraw.clear();

        StdDraw.enableDoubleBuffering();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                StdDraw.filledCircle(enemies[i][j].getX(), enemies[i][j].getY(), ENEMY_RADIUS);
            }
        }

        StdDraw.show(20);
    }
}
