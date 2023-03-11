package model;

import model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

//Testes for each class, gaps identified.
class MazesTest {

    ArrayList<Integer> arrangement;
    Mazes mazeRef;

    @BeforeEach
    void runBefore() {
        arrangement = new ArrayList<Integer>();
        arrangement.add(0);
        arrangement.add(1);
        arrangement.add(2);
        arrangement.add(3);
        arrangement.add(4);
        arrangement.add(5);
        arrangement.add(6);//This one returns the default maze, just in-case necessary, it will never be called again.

        mazeRef = new Mazes(arrangement);

    }



    @Test
    void testPrinting() {
        mazeRef.initializePlayer(0);
        String maze = mazeRef.printMazeWPlayer(0);
        String mazeCompare = "0 P 0 \n0   0 \n0   E \n";
        assertTrue(mazeCompare.equals(maze));
        mazeRef.initializePlayer(1);
        String maze2 = mazeRef.printMazeWPlayer(1);
        String mazeCompare2 = "P 0 0 0 \n        \n0   0   \n0   0 E \n";
        assertTrue(mazeCompare2.equals(maze2));
        //Testing print normal
        String mazeNormal = mazeRef.printMaze(1);
        String mazeNor = "S 0 0 0 \n        \n0   0   \n0   0 E \n";
        assertTrue(mazeNor.equals(mazeNormal));
    }


    @Test
    void testBasicSolved() {
        assertFalse(mazeRef.checkSolved(0));
        assertFalse(mazeRef.checkSolved(1));
        assertFalse(mazeRef.checkSolved(2));
        assertTrue(mazeRef.checkSolved(3));
        assertTrue(mazeRef.checkSolved(4));
        assertTrue(mazeRef.checkSolved(5));
        assertTrue(mazeRef.checkSolved(6));
        //Testing to check if solved after completion later.
    }

    @Test
    void testSolved() {
        assertFalse(mazeRef.checkSolved(0));
        mazeRef.initializePlayer(0);
        assertFalse(!mazeRef.solved(0));
        mazeRef.quickSolve(0);
        assertTrue(mazeRef.checkSolved(0));
        mazeRef.initializePlayer(1);
        mazeRef.quickSolve(1);
        assertTrue(mazeRef.checkSolved(1));
        assertFalse(!mazeRef.checkAllSolved());
        mazeRef.initializePlayer(2);
        mazeRef.quickSolve(2);
        assertTrue(mazeRef.checkSolved(2));
        assertTrue(mazeRef.checkSolved(3));
        assertTrue(!mazeRef.checkAllSolved());
    }

    @Test
    void testPossibleAndApplyMove() {
        mazeRef.initializePlayer(0);
        try{
            mazeRef.possibleMove(0, "up");
        } catch (InvalidInputException e){
            assertEquals("Input Out of Bounds.", e.getMessage());
        }
        try{
            mazeRef.possibleMove(0, "left");
        } catch (InvalidInputException e){
            assertEquals("Invalid Input", e.getMessage());
        }
        try{
            mazeRef.possibleMove(0, "down");
            mazeRef.applyMove(0, "Down");
        } catch (InvalidInputException e){
            fail("Should not fail.");
        }

        mazeRef.initializePlayer(1);
        try{
            mazeRef.possibleMove(1, "left");
        } catch (InvalidInputException e){
            assertEquals("Input Out of Bounds.", e.getMessage());
        }
        try{
            assertTrue(!mazeRef.possibleMove(1, "Down"));
            mazeRef.applyMove(1, "down");
            mazeRef.applyMove(1, "up");
            mazeRef.applyMove(1, "down");
        } catch (InvalidInputException e){
            fail("Should not fail.");
        }
        //Testing to print where S is present
        String maze1 = mazeRef.printMazeWPlayer(1);
        String mazeCompare2 = "S 0 0 0 \nP       \n0   0   \n0   0 E \n";
        assertEquals(maze1, mazeCompare2);
        try{
            mazeRef.possibleMove(1, "Down");
        } catch (InvalidInputException e){
            assertEquals("Invalid Input", e.getMessage());
        }
        try{
            mazeRef.possibleMove(1, "Right");
            mazeRef.applyMove(1,"Right");
            mazeRef.applyMove(1,"left");
        } catch (InvalidInputException e){
            fail("Should not fail.");
        }
    }

    @Test
    void testResetSolved() {
        mazeRef.initializePlayer(0);
        assertFalse(!mazeRef.solved(0));
        mazeRef.quickSolve(0);
        assertTrue(!mazeRef.solved(0));
        mazeRef.resetSolved(0);
        assertFalse(!mazeRef.solved(0));
    }
}