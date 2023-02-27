package ui;

import model.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

//Contains the mazes, initializes mazes using super class, checks and returns values
//Performs actions on the mazes.
public class Mazes {
    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);

    private LinkedList<Maze> mazes = new LinkedList<Maze>();
    ArrayList<Integer> arrangement;

    private int temp;

    public Mazes() {
        start();
        constantRun();
    }

    private void start() {
        arrangement = new ArrayList<Integer>();
        Maze mazeTemp;
        int temp;
        Mazes mazeRef;
        System.out.println(arrangement);
        for (int i = 0; i < 6; i++) {
            do {
                temp = rand.nextInt(6);
            } while (arrangement.contains(temp));
            arrangement.add(temp);
        }

        System.out.println(arrangement);

        for (int i = 0; i < arrangement.size(); i++) {
            temp = arrangement.get(i);
            mazeTemp = new Maze(temp);
            mazes.add(mazeTemp);
        }
        System.out.println(mazes);
    }



    private void constantRun() {
        int index;
        do {
            for (int i = 0; i < mazes.size(); i++) {
                if (mazes.get(i).isNotMaze()) {
                    mazes.get(i).solved();
                }
            }

            int inp;
            do {
                System.out.println("Which maze would you like to enter [1-6] by order of increasing difficulty:");
                System.out.println("Currently only maze 1 and 2 are populated.");
                inp = sc.nextInt();
            } while (checkInp(inp));
            inp--;
            index = arrangement.indexOf(inp);

            mazes.get(index).initializePlayer();
            mazes.get(index).play();

        } while (checkAllSolved());//checks if all mazes are solved.
    }

    //Will change to accommodate how many mazes actually have a maze in them.
    private boolean checkInp(int inp) {
        return (!((inp == 1) || (inp == 2)));
    }

    private boolean checkAllSolved() {
        int solvedCount = 0;
        for (int i = 0; i < mazes.size(); i++) {
            if (mazes.get(i).isSolved()) {
                solvedCount++;
            }
        }
        if (solvedCount == arrangement.size()) {
            return true;
        } else {
            return false;
        }
    }

}