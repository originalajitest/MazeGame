package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//added throwable for NullPointerException, but found another solution.
//Object that is changes the most and works the most. It contains the maze it is working on,
// a reference to player in the maze and helps establish communication.
public class Maze extends Exception implements Writable {

    //Links to Player class for each maze and stores current position.
    private Player player;

    //Store the start point and the end point of the maze;
    private int startY = -1;
    private int startX = -1;
    private int endY = -1;
    private int endX = -1;

    private boolean solved;
    private boolean solvedOnce;

    private String[][] maze;
    AssignMaze assign = new AssignMaze();

    private String printMaze;
    private String printMazeWPlayer;

    //Getter
    public String[][] getMaze() {
        return maze;
    }

    //Getter
    public Map<String, Object> getReq() {
        Map<String, Object> out = new HashMap<>();
        out.put("player", player);
        out.put("startX", startX);
        out.put("startY", startY);
        out.put("endX", endX);
        out.put("endY", endY);
        return out;
    }

    //EFFECTS: converts this to a JSON object and returns that object.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (player == null) {
            initializePlayer();
        }
        json.put("player", player.toJson());
        json.put("startY", startY);
        json.put("startX", startX);
        json.put("endY", endY);
        json.put("endX", endX);
        json.put("solved", solved);
        json.put("solvedOnce", solvedOnce);
        json.put("maze", mazeToJson());
        return json;
    }

    //EFFECTS: converts the String[][]maze to a JSON object.
    private JSONArray mazeToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < maze.length; i++) {
            jsonArray.put(maze[i]);
        }
        return jsonArray;
    }

    //REQUIRES: pos is an integer
    //MODIFIES: this
    //EFFECTS: gets a maze from AssignMaze.java and stores it in maze. Initializes start and end points.
    // Sets default solved and solvedOnce to false. gets a printable maze ready.
    public Maze(int pos) {
        maze = assign.assignMaze(pos);
        assignPoints();
        solved = false;
        solvedOnce = false;
        readyPrint();
    }

    //MODIFIES: this
    //EFFECTS: converts JSON object to a Maze
    public Maze(Map<String, Object> json) {
        this.endY = (int) json.get("endY");
        this.endX = (int) json.get("endX");
        this.startX = (int) json.get("startX");
        this.startY = (int) json.get("startY");
        this.solved = (boolean) json.get("solved");
        this.solvedOnce = (boolean) json.get("solvedOnce");
        toMazeArray((ArrayList) json.get("maze"));
        this.player = new Player(json.get("player"));
        readyPrint();
    }

    //MODIFIES: this.maze
    //EFFECTS: converts maze JSON object to maze String[][]
    private void toMazeArray(ArrayList json) {
        maze = new String[json.size()][((ArrayList) json.get(0)).size()];
        for (int i = 0; i < json.size(); i++) {
            for (int j = 0; j < ((ArrayList) json.get(0)).size(); j++) {
                Object temp = ((ArrayList) json.get(i)).get(j);
                maze[i][j] = (String) temp;
            }
        }
    }
    
    //REQUIRES: Maze is a 2D array, it has at least two entries (1 row & 2 columns)
    //MODIFIES: this (startX, startY, endX, endY)
    //EFFECTS: initializes start and end points. Labeled by two null 's. Changes these back to "T"
    private void assignPoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == null) {
                    if (startY == -1) {
                        startX = j;
                        startY = i;
                    } else {
                        endX = j;
                        endY = i;
                    }
                    maze[i][j] = "T";
                }
            }
        }
    }

    //REQUIRES: maze is initialized to some maze.
    //MODIFIES: this.printMaze
    //EFFECTS: Produces a sring which is like the maze, without player.
    //used For Debugging as it can show any maze and also used as ref for later code.
    public void readyPrint() {
        printMaze = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (j == startX && i == startY) {
                    printMaze = printMaze + "S ";
                } else if (j == endX && i == endY) {
                    printMaze = printMaze + "E ";
                } else if (maze[i][j].equals("T")) {
                    printMaze = printMaze + "  ";
                } else {
                    printMaze = printMaze + "0 ";
                }
            }
            printMaze = printMaze + "\n";
        }
    }

    //REQUIRES: maze is initializes to some maze.
    //EFFECTS: returns true if maze is 1 row and 2 columns only (its has a start and end only).
    public boolean isNotMaze() {
        return ((maze.length == 1) && (maze[0].length == 2));
    }

    //MODIFIES: this.solved, this.solvedOnce
    //EFFECTS: sets them both to true, used setting status of emptyMazes and for debugging.
    public void solved() {
        solved = true;
        solvedOnce = true;
    }

    //REQUIRES: assignPoints() has been run.
    //MODIFIES: this.player
    //EFFECTS: initializes player to start point.
    public void initializePlayer() {
        player = new Player(startX, startY);
    }

    //EFFECTS: returns solved is true.
    public boolean isSolved() {
        return solved;
    }

    //REQUIRES: both maze and player have been initialized.
    //MODIFIES: this.printMazeWPlayer
    //EFFECTS: sets it to the current maze with P at where the player is. Will change largely during gui stage.
    private void readyPrintWPlayer() {
        printMazeWPlayer = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if ((j == player.getX()) && (i == player.getY())) {
                    printMazeWPlayer = printMazeWPlayer + "P ";
                } else if (j == startX && i == startY) {
                    printMazeWPlayer = printMazeWPlayer + "S ";
                } else if (j == endX && i == endY) {
                    printMazeWPlayer = printMazeWPlayer + "E ";
                } else if (maze[i][j].equals("T")) {
                    printMazeWPlayer = printMazeWPlayer + "  ";
                } else {
                    printMazeWPlayer = printMazeWPlayer + "0 ";
                }
            }
            printMazeWPlayer = printMazeWPlayer + "\n";
        }
    }

    //REQUIRES: printMazeWPlayer is not null.
    //EFFECTS: returns this.printMazeWPlayer
    public String printWPlayer() {
        readyPrintWPlayer();
        return printMazeWPlayer;
    }

    //REQUIRES: printMaze is not null.
    //EFFECTS: returns this.printMaze
    //Only for testing and debugging
    public String printNormal() {
        return printMaze;
    }

    //REQUIRES: inp is a string and one of :up, down, right, left
    //MODIFIES: this.player
    //EFFECTS: moves the player one move in specified direction.
    public void applyMove(String inp) {
        if (inp.equalsIgnoreCase("Up")) {
            player.moveY(-1);
        } else if (inp.equalsIgnoreCase("Down")) {
            player.moveY(1);
        } else if (inp.equalsIgnoreCase("Right")) {
            player.moveX(1);
        } else {
            player.moveX(-1);
        }
    }

    //REQUIRES: inp is a string and one of :up, down, right, left
    //EFFECTS: returns true if the player can be moved one in specified direction, throws exception if not possible
    public boolean possibleMove(String inp) throws InvalidInputException {
        if (inp.equalsIgnoreCase("Up")) {
            return landingOkY(player.getX(), (player.getY() - 1));
        } else if (inp.equalsIgnoreCase("Down")) {
            return landingOkY(player.getX(), (player.getY() + 1));
        } else if (inp.equalsIgnoreCase("Right")) {
            return landingOkX((player.getX() + 1), player.getY());
        } else {
            return landingOkX((player.getX() - 1), player.getY());
        }
    }

    //REQUIRES: both are integers between -1 and (maze dimension +1)
    //EFFECTS: returns false if move is possible, else throws exception with error inside it.
    private boolean landingOkY(int j, int i) throws InvalidInputException {
        if (i < 0 || i >= maze.length) {
            throw new InvalidInputException("Input Out of Bounds.");
        } else if (maze[i][j].equals("T")) {
            return false;
        } else {
            throw new InvalidInputException("Invalid Input");
        }
    }

    //REQUIRES: both are integers between -1 and (maze dimension +1)
    //EFFECTS: returns false if move is possible, else throws exception with error inside it.
    private boolean landingOkX(int j, int i) throws InvalidInputException {
        if (j < 0 || j >= maze[0].length) {
            throw new InvalidInputException("Input Out of Bounds.");
        } else if (maze[i][j].equals("T")) {
            return false;
        } else {
            throw new InvalidInputException("Invalid Input");
        }
    }

    //REQUIRES: this.player has been initialized
    //MODIFIES: this.solved, this.solvedOnce
    //EFFECTS: if player is at end point of maze, sets maze status to solved and returns false
    public boolean justSolved() {
        if ((player.getX() == endX) && (player.getY() == endY)) {
            solved = true;
            solvedOnce = true;
            return false;
        } else {
            return true;
        }
    }

    //EFFECTS: changes solved back to false so it can be done again.
    // Second line is only necessary for the testing phase.
    public void resetSolved() {
        solved = false;
        initializePlayer();
    }

    //EFFECTS: sets player to end points and sets solved to true via justSolved().
    //Only for debugging and testing
    public void quickSolve() {
        player.setXAndY(endY, endX);
        justSolved();
    }

    //EFFECTS: sets player to end points
    //Only for debugging and testing and cheatcode
    public void quickSolve2() {
        player.setXAndY(endX, endY);
    }
}