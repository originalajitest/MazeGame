package model;

import org.junit.jupiter.api.*;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Maze4Test {

    Mazes mazes;
    ArrayList<Integer> arrange;
    JsonReader jsonReader;
    String data = "./data/test2Maze.json";

    @BeforeEach
    void runBefore() {
        arrange = new ArrayList<>();
        arrange.add(3);
        arrange.add(4);
        mazes = new Mazes(arrange);
    }

    @Test
    void testMaze4() {
        Mazes newMazes;
        try {
            jsonReader = new JsonReader(data);
            Map<String, Object> stored = jsonReader.read();
            newMazes = new Mazes(stored);
            String[][] savedMaze = newMazes.getMaze(1);
            String[][] maze = mazes.getMaze(0);
            assertEquals(maze.length,savedMaze.length);
            assertEquals(maze[0].length,savedMaze[0].length);
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    assertEquals(maze[i][j],savedMaze[i][j]);
                }
            }
        } catch (IOException e) {
            fail();
        }
    }
}
