package model;

import org.junit.jupiter.api.*;

public class AssignMazeTest {
    AssignMaze assign;

    @BeforeEach
    void renBefore() {
        assign = new AssignMaze();
    }

    @Test
    void test1() {
        assign.assignMaze(4);
        assign.convToMaze("hi.txt",6,6);
        String maze6ref = "/images/maze6.png";
        String[][] maze6 = assign.convToMaze(maze6ref, 51, 41);
    }
}
