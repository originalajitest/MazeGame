package ui;

import model.InvalidInputException;
import model.Mazes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
    Add documentation.
    Add user stories (5).

    REQUIRES:   What has to be done before the function is called (pre req)
    MODIFIES:   If objects are being changed (this for current object), or inputs.
    EFFECTS:    Purpose of the function be specific.
*/

//Initialises array of mazes/medals, array which decides which maze is in which position
//As a reference to extra stretch goals also shows which place has the extra games. 1,3,5?
public class Main {

    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);
    static Mazes mazes;

    static ArrayList<Integer> arrangement;

    //EFFECTS: runs the program, when this terminates the program also terminates.
    public static void main(String[] args) {
        System.out.println();
        arrangement = new ArrayList<Integer>();
        int temp;
        for (int i = 0; i < 6; i++) {
            do {
                temp = rand.nextInt(6);
            } while (arrangement.contains(temp));
            arrangement.add(temp);
        }
        mazes = new Mazes(arrangement);
        do {
            int index = mazeNum();
            play(index);
            again(index);
        } while (checkAllSolved());
        System.out.println("Congratulations, all mazes have been completed.");
    }

    //EFFECTS: asks the user for an input on which maze to enter and returns the index of the maze in mazes.
    private static int mazeNum() {
        int index;
        int num;
        String inp;
        do {
            do {
                do {
                    System.out.println("Which maze would you like to enter [1-6] order of increasing difficulty:");
                    System.out.println("Currently only maze 1, 2 and 3 are populated.");
                    inp = sc.next();
                } while (isInt(inp));
                num = Integer.parseInt(inp);
            } while (checkInp(num));
            num--;
            index = arrangement.indexOf(num);
        } while (checkSolved(index));
        mazes.initializePlayer(index);
        return index;
    }

    //REQUIRES: 0<= index < arrangement.size
    //EFFECTS: allows the user to complete the maze via up, down, right, left, once maze completed then leaves the maze.
    private static void play(int index) {
        String move;
        System.out.println(mazes.printMazeWPlayer(index));
        do {
            do {
                do {
                    System.out.println("Enter a move (Up, Down, Right, Left):");
                    move = sc.next();
                } while (validInp(move));
            } while (possibleMove(index, move));
            applyMove(index, move);
            System.out.println(mazes.printMazeWPlayer(index));
        } while (solved(index));
    }

    //REQUIRES: 0<= index < arrangement.size
    //MODIFIES: this.mazes.mazes[index].solved
    //EFFECTS: asks if the user wants to do the maze again later.
    private static void again(int index) {
        System.out.println("To be able to do this maze again enter (Again):");
        String again = sc.next();
        if (again.equalsIgnoreCase("Again")) {
            mazes.resetSolved(index);
            mazes.initializePlayer(index);
        }
    }

    //EFFECT: returns false if input is an integer otherwise true.
    private static boolean isInt(String inp) {
        try {
            Integer.parseInt(inp);
            return false;
        } catch (NumberFormatException e) {
            System.out.println(inp + " is not a valid integer. Please input a valid integer.");
            return true;
        }
    }

    //EFFECTS: returns true if inp is withing 1 and 6 (number of mazes).
    //Will change to accommodate how many mazes actually have a maze in them.
    private static boolean checkInp(int inp) {
        if ((inp > 0) && (inp < 7)) {
            return false;
        } else {
            System.out.println("Input outside specified range.");
            return true;
        }
    }

    //EFFECTS: returns true if the maze in index i has been solved
    private static boolean checkSolved(int i) {
        if (mazes.checkSolved(i)) {
            System.out.println("This maze has already been solved");
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: returns false if all mazes have been solved.
    private static boolean checkAllSolved() {
        return mazes.checkAllSolved();
    }

    //EFFECTS: returns false if inp is one of possible strings (up,down,right,left) ignoring case
    private static boolean validInp(String inp) {
        return !(inp.equalsIgnoreCase("Up") || inp.equalsIgnoreCase("Down")
                || inp.equalsIgnoreCase("Right") || inp.equalsIgnoreCase("Left"));//!!!
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    // str is one of up,down,right,left
    //MODIFIES: mazes.mazes[].player
    //EFFECTS: moves the player 1 move in given direction.
    private static void applyMove(int index, String str) {
        mazes.applyMove(index, str);
    }

    //REQUIRES: player in index has been initialized; index is an int, and 0 <= index <= arrangement.size();
    // str is one of up,down,right,left
    //EFFECTS: returns false if player can be moved in given spot else true with error.
    private static boolean possibleMove(int index, String str) {
        boolean possible;
        try {
            possible = mazes.possibleMove(index, str);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return true;
        }
        return possible;
    }

    //REQUIRES: 0 <= index <= arrangement.size()
    //MODIFIES: mazes.mazes[index].solved
    //EFFECTS: returns false if maze has just been solved and player is at endpoint else true
    private static boolean solved(int index) {
        return mazes.solved(index);
    }
}