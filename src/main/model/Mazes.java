package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

//Contains the mazes, initializes mazes using Maze.java
//Main function is to call function in Maze.java, it acts as a method of communication between main and the various
// mazes stored in mazes
public class Mazes implements Writable {

    ArrayList<Integer> arrangement;
    private LinkedList<Maze> mazes;

    //REQUIRES: order is not empty and it is an integer list
    //MODIFIES: this
    //EFFECTS: copys order into arrangement and calls initializeMazes() then sets empty mazes to solved.
    public Mazes(ArrayList<Integer> order) {
        this.arrangement = order;
        initializeMazes();
        setDefaultSolved();
    }

    public Mazes(Map<String, Object> storedData) {
        this.mazes = (LinkedList<Maze>) storedData.get("mazes");
        this.arrangement = (ArrayList<Integer>) storedData.get("arrangement");
    }

    public ArrayList<Integer> getArrangement() {
        return arrangement;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("arrangement", arrangementToJsonArrangement());
        json.put("mazes", mazesToJsonMazes());
        return json;
    }

    private JSONArray arrangementToJsonArrangement() {
        JSONArray jsonArray = new JSONArray();
        for (int i : arrangement) {
            jsonArray.put(i);
        }
        return jsonArray;
    }

    private JSONArray mazesToJsonMazes() {
        JSONArray jsonArray = new JSONArray();
        for (Maze maze : mazes) {
            jsonArray.put(maze.toJson());
        }
        return jsonArray;
    }

    //REQUIRES: arrangement is not empty
    //MODIFIES: this.mazes
    //EFFECTS: places Maze objects into mazes array each maze no. corresponding to number in arrangement.
    private void initializeMazes() {
        mazes = new LinkedList<Maze>();
        Maze mazeTemp;
        int temp;
        for (int i = 0; i < arrangement.size(); i++) {
            temp = arrangement.get(i);
            mazeTemp = new Maze(temp);
            mazes.add(mazeTemp);
        }
    }

    //REQUIRES: mazes have been initialized.
    //MODIFIES: this.mazes[].solved
    //EFFECTS: goes through the mazes and sets empty mazes to solved.
    private void setDefaultSolved() {
        for (int i = 0; i < mazes.size(); i++) {
            if (mazes.get(i).isNotMaze()) {
                mazes.get(i).solved();
            }
        }
    }

    //REQUIRES: mazes has been initialized
    //EFFECTS: returns false if all mazes are solved otherwise true
    public boolean checkAllSolved() {
        int solvedCount = 0;
        for (int i = 0; i < mazes.size(); i++) {
            if (mazes.get(i).isSolved()) {
                solvedCount++;
            }
        }
        if (solvedCount == arrangement.size()) {
            return false;
        } else {
            return true;
        }
    }

    //REQUIRES: i is and int, and 0 <= i <= arrangement.size()
    //MODIFIES: this.mazes
    //EFFECTS: initialize player in given maze number.
    public void initializePlayer(int i) {
        mazes.get(i).initializePlayer();
    }

    //REQUIRES: mazes have been initialized; i is and int, and 0 <= i <= arrangement.size()
    //EFFECTS: returns solved
    public boolean checkSolved(int i) {
        return mazes.get(i).isSolved();
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    // str is one of up,down,right,left
    //MODIFIES: mazes[].player
    //EFFECTS: moves the player 1 move in given direction.
    public void applyMove(int index, String str) {
        mazes.get(index).applyMove(str);
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    // str is one of up,down,right,left
    //EFFECTS: returns false if player can be moved in given spot else throws InvalidInputException
    public boolean possibleMove(int index, String str) throws InvalidInputException {
        return mazes.get(index).possibleMove(str);
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    //EFFECTS: returns a string which is the current array with the player
    public String printMazeWPlayer(int index) {
        return mazes.get(index).printWPlayer();
    }

    //REQUIRES: maze in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    //EFFECTS: returns a string which is the current array
    //Only for testing and debugging
    public String printMaze(int index) {
        return mazes.get(index).printNormal();
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    //MODIFIES: this.mazes[index].solved
    //EFFECTS: returns false if maze has just been solved and player is at endpoint. else true
    public boolean solved(int index) {
        return mazes.get(index).justSolved();
    }

    //REQUIRES: maze in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    //MODIFIES: this.mazes[index].solved
    //EFFECTS: changes solved to false
    public void resetSolved(int index) {
        mazes.get(index).resetSolved();
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    //MODIFIES: this.mazes[index].player
    //EFFECTS: moves player to the end of the maze and runs justSolved()
    //Only for debugging and testing
    public void quickSolve(int index) {
        mazes.get(index).quickSolve();
    }

}