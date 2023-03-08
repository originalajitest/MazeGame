package model;

//Contains mazes, initializes them.
//added throwable for NullPointerException, but found another solution.
public class Maze extends Exception {

    private Player user;

    //Store the start point and the end point of the maze;
    private Integer startY;
    private Integer startX;
    private Integer endY;
    private Integer endX;

    private boolean solved;
    private boolean solvedOnce;

    private String[][] maze;
    AssignMaze assign = new AssignMaze();

    //REQUIRES: Maze is a rectangle or a square.
    public Maze(int pos) {
        maze = assign.assignMaze(pos);
        assignPoints();
        solved = false;
        solvedOnce = false;
        readyPrint();
    }

    private String printMaze;
    private String printMazeWPlayer;

    //REQUIRES: pos is a number between 0 and 5 inclusive, Mazes are predefined.


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
    public void readyPrint() {
        printMaze = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (j == startX && i == startY) {
                    printMaze = printMaze + "S ";
                } else if (j == endX && i == endY) {
                    printMaze = printMaze + "E ";
                } else if (maze[i][j] == "T") {
                    printMaze = printMaze + "  ";
                } else {
                    printMaze = printMaze + "0 ";
                }
            }
            printMaze = printMaze + "\n";
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

    private void readyPrintWPlayer() {
        printMazeWPlayer = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if ((j == user.getX()) && (i == user.getY())) {
                    printMazeWPlayer = printMazeWPlayer + "P ";
                } else if (j == startX && i == startY) {
                    printMazeWPlayer = printMazeWPlayer + "S ";
                } else if (j == endX && i == endY) {
                    printMazeWPlayer = printMazeWPlayer + "E ";
                } else if (maze[i][j] == "T") {
                    printMazeWPlayer = printMazeWPlayer + "  ";
                } else {
                    printMazeWPlayer = printMazeWPlayer + "0 ";
                }
            }
            printMazeWPlayer = printMazeWPlayer + "\n";
        }
    }

    public String printWPlayer() {
        readyPrintWPlayer();
        return printMazeWPlayer;
    }

    public String printNormal() {
        return printMaze;
    }

    public void applyMove(String inp) {
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

    public boolean possibleMove(String inp) throws InvalidInputException, IndexOutOfBoundsException {
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

    private boolean landingOkY(int j, int i) throws InvalidInputException, IndexOutOfBoundsException {
        if (i < 0 || i >= maze.length) {
            throw new InvalidInputException("Input Out of Bounds.");
        } else if (maze[i][j] == "T") {
            return false;
        } else {
            throw new InvalidInputException("Invalid Input");
        }
    }

    private boolean landingOkX(int j, int i) throws InvalidInputException, IndexOutOfBoundsException {
        if (j < 0 || j >= maze[0].length) {
            throw new InvalidInputException("Input Out of Bounds.");
        } else if (maze[i][j] == "T") {
            return false;
        } else {
            throw new InvalidInputException("Invalid Input");
        }
    }

    public boolean justSolved() {
        if ((user.getX() == endY) && (user.getY() == endX)) {
            solved = true;
            solvedOnce = true;
            return false;
        } else {
            return true;
        }
    }

    public void resetSolved() {
        solved = false;
    }
}