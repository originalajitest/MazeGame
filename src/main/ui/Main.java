package ui;

import model.InvalidInputException;
import model.Mazes;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
    Add documentation.
    Add user stories (5).

    REQUIRES:   What has to be done before the function is called (pre req)
    MODIFIES:   If objects are being changed (this for current object), or inputs.
    EFFECTS:    Purpose of the function be specific.
*/

//Initialises array of mazes/medals, array which decides which maze is in which position
//As a reference to extra stretch goals also shows which place has the extra games. 1,3,5?

/*
    Ok...
    So...
    An initial screen with options to new, load, and quit.
    Then go to a simple screen to pick which maze to enter
    Then go to maze, maze is entirely static and user moves above
    When user reach end, pop-up for finishing
    Then go back to picking screen.
    Complete till all mazes done then quit
    Maze has a settings logo above to save and quit currently.
 */
public class Main {

    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);
    static Mazes mazes;

    static ArrayList<Integer> arrangement = new ArrayList<Integer>();

    static boolean keepGoing = true;

    private static final String JSON_STORE = "./data/saveState.json";
    private static JsonWriter jsonWriter;
    private static JsonReader jsonReader;

    //REQUIRES: first input is one of l, n, q
    //EFFECTS: runs the program, when this terminates the program also terminates.
//    @SuppressWarnings("methodlength") //use full whenever
    public static void main(String[] args) {
//        System.out.println("Pick one of the following");
//        System.out.println("\t g - Use Gui");
//        System.out.println("\t c - Use Cli");
//        String inp = sc.next();
//
//        if (inp.equalsIgnoreCase("g")) {
//            StartFrame begin = new StartFrame(JSON_STORE);
//        } else if (inp.equalsIgnoreCase("c")) {
//            runCLI();
//        }
        runCLI();
    }

    //EFFECTS: runs the CLI version, moved cause thought had to get GUI but told to make new main.
    private static void runCLI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        loadOrNew();
        while (keepGoing) {
            if (checkAllSolved()) {
                int index = mazeNum();
                play(index);
                if (!keepGoing) {
                    break;
                }
                again(index);
                isSaving();
                isLeaving();
            }
            if (!checkAllSolved()) {
                keepGoing = false;
            }
        }
        if (!checkAllSolved() && (arrangement != null)) {
            System.out.println("Congratulations, all mazes have been completed.");
        }
    }

    //MODIFIES: this
    //EFFECTS: asks user if they want to start a new game, load a game or quit and performs respective functions.
    private static void loadOrNew() {
        String inp;
        System.out.println("\nSelect from:");
        System.out.println("\tl -> load game state from file");
        System.out.println("\tn -> new game");
        System.out.println("\tq -> quit");
        inp = sc.next();
        if (inp.equalsIgnoreCase("n")) {
            defaultInitialize();
        } else if (inp.equalsIgnoreCase("l")) {
            loadState();
        } else {
            keepGoing = false;
        }
    }

    // MODIFIES: this
    // EFFECTS: loads saveState from file to mazes.
    private static void loadState() {
        try {
            Map<String, Object> storedData = jsonReader.read();
            mazes = new Mazes(storedData);
            arrangement = mazes.getArrangement();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            keepGoing = false;
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the current state of mazes to file
    private static void saveState() {
        try {
            jsonWriter.open();
            jsonWriter.write(mazes);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: initialization for a new game, puts random numbers in arrangement, initializes mazes using arrangement.
    private static void defaultInitialize() {
        int temp;
        for (int i = 0; i < 6; i++) {
            do {
                temp = rand.nextInt(6);
            } while (arrangement.contains(temp));
            arrangement.add(temp);
        }
        mazes = new Mazes(arrangement);
        initializePlayers();
    }

    //MODIFIES: this.mazes.mazes().player
    //EFFECTS: goes into each Maze in mazes.mazes and initializes player in each.
    private static void initializePlayers() {
        for (int i = 0; i < arrangement.size(); i++) {
            mazes.initializePlayer(i);
        }
    }

    //EFFECTS: asks user if they want to save the game, if so runs saveState();
    private static void isSaving() {
        System.out.println("Enter s to save game state to file");
        String inp = sc.next();
        if (inp.equalsIgnoreCase("s")) {
            saveState();
        }
    }

    //EFFECTS: asks user if they want to quit the game, if so then sets keepGoing to false.
    private static void isLeaving() {
        System.out.println("Enter q to quit");
        String inp = sc.next();
        if (inp.equalsIgnoreCase("q")) {
            keepGoing = false;
        }
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
            isSaving();
            isLeaving();
            if (!keepGoing) {
                break;
            }
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