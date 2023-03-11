package model;

import model.Maze;

import java.util.ArrayList;
import java.util.LinkedList;

//Contains the mazes, initializes mazes using super class, checks and returns values
//Performs actions on the mazes.
public class Mazes {

    ArrayList<Integer> arrangement;
    private LinkedList<Maze> mazes;

    //REQUIRES: order is not empty and it is an integer list
    public Mazes(ArrayList<Integer> order) {
        this.arrangement = order;
        initializeMazes();
        setDefaultSolved();
    }

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

    private void setDefaultSolved() {
        for (int i = 0; i < mazes.size(); i++) {
            if (mazes.get(i).isNotMaze()) {
                mazes.get(i).solved();
            }
        }
    }


    //RE
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

    public void initializePlayer(int i) {
        mazes.get(i).initializePlayer();
    }

    public boolean checkSolved(int i) {
        return mazes.get(i).isSolved();
    }

    public void applyMove(int index, String str) {
        mazes.get(index).applyMove(str);
    }

    public boolean possibleMove(int index, String str) throws InvalidInputException {
        return mazes.get(index).possibleMove(str);
    }

    public String printMazeWPlayer(int index) {
        return mazes.get(index).printWPlayer();
    }

    //Only for testing and debugging
    public String printMaze(int index) {
        return mazes.get(index).printNormal();
    }

    public boolean solved(int index) {
        return mazes.get(index).justSolved();
    }

    public void resetSolved(int index) {
        mazes.get(index).resetSolved();
    }

    //Only for debugging and testing
    public void quickSolve(int index) {
        mazes.get(index).quickSolve();
    }

}