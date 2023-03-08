package ui;

import model.InvalidInputException;
import model.Mazes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
    Move println's from Model to UI. Cna use String and + and store locally then call.
    Anything which stores a lot of data should be in model.
    All scanner inputs should be in main.

    Add tests
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

    private static void again(int index) {
        System.out.println("To be able to do this maze again enter (Again):");
        String again = sc.next();
        if (again.equalsIgnoreCase("Again")) {
            mazes.resetSolved(index);
            mazes.initializePlayer(index);
        }
    }

    private static boolean isInt(String inp) {
        try {
            Integer.parseInt(inp);
            return false;
        } catch (NumberFormatException e) {
            System.out.println(inp + " is not a valid integer. Please input a valid integer.");
            return true;
        }
    }

    //Will change to accommodate how many mazes actually have a maze in them.
    private static boolean checkInp(int inp) {
        if ((inp > 0) && (inp < 7)) {
            return false;
        } else {
            System.out.println("Input outside specified range.");
            return true;
        }
    }

    private static boolean checkSolved(int i) {
        if (mazes.checkSolved(i)) {
            System.out.println("This maze has already been solved");
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkAllSolved() {
        return mazes.checkAllSolved();
    }

    private static boolean validInp(String inp) {
        return !(inp.equalsIgnoreCase("Up") || inp.equalsIgnoreCase("Down")
                || inp.equalsIgnoreCase("Right") || inp.equalsIgnoreCase("Left"));//!!!
    }

    private static void applyMove(int index, String str) {
        mazes.applyMove(index, str);
    }

    private static boolean possibleMove(int index, String str) {
        boolean possible;
        try {
            possible = mazes.possibleMove(index, str);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return true;
        }
        return possible;
    }

    private static boolean solved(int index) {
        return mazes.solved(index);
    }
}