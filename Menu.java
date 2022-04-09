import java.awt.*;

public class Menu {
    public static final int WIDTH = 600; //width of canvas in pixels
    public static final int HEIGHT = 600; //height of canvas in pixels
    public static final int X_SCALE = 10; //dividing canvas up horizontally (helps with placement of drawings)
    public static final int Y_SCALE = 10; //dividing canvas up vertically (helps with placement of drawings)

    public void createGame() {
        createCanvas();
        startMenu();
    }

    public void createCanvas() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, X_SCALE);
        StdDraw.setYscale(0, Y_SCALE);
    }

    public void startMenu() {
        StdDraw.clear();

        Font font1 = new Font("Serif", Font.BOLD, 50);
        StdDraw.setFont(font1);
        StdDraw.text(5, 9.5, "Zombie Zapper");

        Font font2 = new Font("Serif", Font.PLAIN, 30);
        StdDraw.setFont(font2);
        StdDraw.text(5, 8.7, "Press Enter to enter at own risk...");

        Font font3 = new Font("Serif", Font.BOLD, 35);
        StdDraw.setFont(font3);
        StdDraw.text(5, 6, "Survival Guide:");

        StdDraw.setFont(font2);
        StdDraw.text(5, 5.5, "Shoot(Space)");
        StdDraw.text(5, 4.5, "Rotate: Left(q), Right(e)");
        StdDraw.text(5, 3.5, "Move: left(left arrow), right(right arrow)");

        StdDraw.setFont(font3);
        StdDraw.text(5, 0.5, "To restart the game press 'r'");

        Game game = new Game();

        while (!StdDraw.isKeyPressed(10)) { //while enter has not been pressed, start menu screen must remain same
            while (!StdDraw.hasNextKeyTyped()) {
            } // loop until a key is pressed
            System.out.println("WRONG KEY:  " + (int) StdDraw.nextKeyTyped());
        }


        if (StdDraw.isKeyPressed(10)) { // 10 = ascii for Enter
            game.newGame();
            game = null;
            
        } else /*if (StdDraw.isKeyPressed(113))*/ {
            StdDraw.clear(Color.BLACK);
            System.out.println("TERMINATING:  " + (int) StdDraw.nextKeyTyped());
            System.exit(1);
        }
    }
}
