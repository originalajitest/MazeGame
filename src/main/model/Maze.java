package model;

import java.util.Scanner;

//Contains mazes, initializes them.
//added throwable for NullPointerException, but found another solution.
public class Maze extends Throwable {

    private Player user;

    //Store the start point and the end point of the maze;
    private Integer startY;
    private Integer startX;
    private Integer endY;
    private Integer endX;

    private boolean solved;
    private boolean solvedOnce;

    static Scanner sc = new Scanner(System.in);

    //Empty mazes are initialised as {{null,null}} so the program can find the start and end, they must have that.
    private String[][] maze;
    private static final String[][] maze1 = new String[][]{
            {"F", null, "F"},
            {"F", "T", "F"},
            {"F", "T", null}};
    private static final String[][] maze2 = new String[][]{
            {null, "F", "F", "F"},
            {"T", "T", "T", "T"},
            {"F", "T", "F", "T"},
            {"F", "T", "F", null}};
    private static final String[][] maze3 = new String[][]{
            {null, "T", "T", "T", "F", "T"},
            {"T", "F", "F", "T", "F", "T"},
            {"T", "T", "F", "T", "F", "T"},
            {"F", "T", "F", "T", "T", "T"},
            {"T", "T", "T", "F", "F", "F"},
            {"T", "F", "T", "T", "T", null}};
    private static final String[][] maze4 = new String[][]{
            {null,null}};
    private static final String[][] maze5 = new String[][]{
            {null,null}};
    private static final String[][] maze6 = new String[][]{
            {null,null}};


    //REQUIRES: Maze is a rectangle or a square.
    public Maze(int pos) {
        assignMaze(pos);
        assignPoints();
        solved = false;
        solvedOnce = false;
    }

    //REQUIRES: pos is a number between 0 and 5 inclusive, Mazes are predefined.
    private void assignMaze(int pos) {
        switch (pos) {
            case 0:
                maze = maze1;
                break;
            case 1:
                maze = maze2;
                break;
            case 2:
                maze = maze3;
                break;
            case 3:
                maze = maze4;
                break;
            case 4:
                maze = maze5;
                break;
            case 5:
                maze = maze6;
                break;
        }
    }

    //REQUIRES: Maze is a 2D array, it has at least two entries (2 rows & 1 column or vice versa) so that we can assign
    // start and the end.
    private void assignPoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == null) {
                    if (startY == null) {
                        startX = j;
                        startY = i;
                    } else {
                        endX = j;
                        endY = i;
                    }
                    this.maze[i][j] = "T";
                }
            }
        }
    }

    //used For Debugging as it can show any maze and also used as ref for later code.
    public void printMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (j == startX && i == startY) {
                    System.out.print("S ");
                } else if (j == endX && i == endY) {
                    System.out.print("E ");
                } else if (maze[i][j] == "T") {
                    System.out.print("  ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public boolean isNotMaze() {
        return (((maze.length == 1) && (maze[0].length == 2)) || ((maze[0].length == 1) && (maze.length == 1)));
    }

    public void solved() {
        solved = true;
        solvedOnce = true;
    }

    public void initializePlayer() {
        user = new Player(startX, startY);
    }

    public boolean isSolved() {
        return solved;
    }

    public void checkPlay() {
        if (solved == true) {
            System.out.println("This maze has been solved");
        } else {
            play();
        }
    }

    private void play() {
        String move;
        printMazeWPlayer();
        do {
            do {
                do {
                    System.out.println("Enter a move (Up, Down, Right, Left):");
                    move = sc.next();
                } while (validInp(move));
            } while (possibleMove(move));

            applyMove(move);
            printMazeWPlayer();
        } while (!((user.getX() == endY) && (user.getY() == endX)));
        solved = true;
        solvedOnce = true;
        System.out.println("To be able to do this maze again enter (Again):");
        String again = sc.next();
        if (again.equalsIgnoreCase("Again")) {
            solved = false;
            initializePlayer();
        }
    }

    private void printMazeWPlayer() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if ((j == user.getX()) && (i == user.getY())) {
                    System.out.print("P ");
                } else if (j == startX && i == startY) {
                    System.out.print("S ");
                } else if (j == endX && i == endY) {
                    System.out.print("E ");
                } else if (maze[i][j] == "T") {
                    System.out.print("  ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    private boolean validInp(String inp) {
        return !(inp.equalsIgnoreCase("Up") || inp.equalsIgnoreCase("Down")
                || inp.equalsIgnoreCase("Right") || inp.equalsIgnoreCase("Left"));//!!!
    }

    private void applyMove(String inp) {
        if (inp.equalsIgnoreCase("Up")) {
            user.moveY(-1);
        } else if (inp.equalsIgnoreCase("Down")) {
            user.moveY(1);
        } else if (inp.equalsIgnoreCase("Right")) {
            user.moveX(1);
        } else {
            user.moveX(-1);
        }
    }

    private boolean possibleMove(String inp) {
        if (inp.equalsIgnoreCase("Up")) {
            return landingOkY(user.getX(), (user.getY() - 1));
        } else if (inp.equalsIgnoreCase("Down")) {
            return landingOkY(user.getX(), (user.getY() + 1));
        } else if (inp.equalsIgnoreCase("Right")) {
            return landingOkX((user.getX() + 1), user.getY());
        } else {
            return landingOkX((user.getX() - 1), user.getY());
        }
    }

    private boolean landingOkY(int j, int i) {
        if (i < 0 || i >= maze.length) {
            System.out.println("Invalid Move");
            return true;
        } else if (maze[i][j] == "T") {
            return false;
        } else if (maze[i][j] == "F") {
            System.out.println("Invalid Move");
            return true;
        } else {
            System.out.println("Invalid Move");
            return true;
        }
    }

    private boolean landingOkX(int j, int i) {
        if (j < 0 || j >= maze[0].length) {
            System.out.println("Invalid Move");
            return true;
        } else if (maze[i][j] == "T") {
            return false;
        } else if (maze[i][j] == "F") {
            System.out.println("Invalid Move");
            return true;
        } else {
            System.out.println("Invalid Move");
            return true;
        }
    }

}