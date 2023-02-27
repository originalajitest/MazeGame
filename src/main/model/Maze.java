package model;

import java.util.Scanner;

//Contains mazes, initializes them.
//added throwable for NullPointerException, but found another solution.
public class Maze extends Throwable {

    private Player user;

    //Store the start point and the end point of the maze;
    private Integer startX;
    private Integer startY;
    private Integer endX;
    private Integer endY;

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
            {null,null}};
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
                    if (startX == null) {
                        startX = i;
                        startY = j;
                    } else {
                        endX = i;
                        endY = j;
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
                if (i == startX && j == startY) {
                    System.out.print("S ");
                } else if (i == endX && j == endY) {
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

    public void play() {
        String move;
        do {
            printMazeWPlayer();
            do {
                System.out.println("Enter a move (Up, Down, Right Left):");
                move = sc.next();
            } while (validInp(move));
            applyMove(move);
        } while ((user.getX() == endX) && (user.getY() == endY));
    }

    private void printMazeWPlayer() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if ((i == user.getX()) && (j == user.getY())) {
                    System.out.print("P ");
                } else if (i == startX && j == startY) {
                    System.out.print("S ");
                } else if (i == endX && j == endY) {
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
        return (inp.equalsIgnoreCase("Up") || inp.equalsIgnoreCase("Down")
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

}