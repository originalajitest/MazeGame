package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

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

        System.out.println(maze6);

        try {
            BufferedImage inp = ImageIO.read(new File(System.getProperty("user.dir") + maze6ref));
            assign.getScaledImage(inp,51,41);
        } catch (IOException e) {
            fail("Shouldn't fail");
        }

        String[][] empty = assign.assignMaze(9);
        String[][] maze = assign.convToMaze("",21,21);
        System.out.println(maze);
        System.out.println(empty);
    }

    @Test
    void testScaling() {
        String maze6ref = "/images/maze6.png";
        String maze6TestRef = "/images/maze6Test.png";
        try {
            BufferedImage maze6inp = ImageIO.read(new File(System.getProperty("user.dir") + maze6ref));
            BufferedImage maze6Test = ImageIO.read(new File(System.getProperty("user.dir") + maze6TestRef));
            BufferedImage maze6 = (BufferedImage) assign.getScaledImage(maze6inp, 51, 41);
            for (int i = 0; i < maze6Test.getHeight(); i++) {
                for (int j = 0; j < maze6Test.getWidth(); j++) {
                    assertEquals(maze6Test.getRGB(j,i),maze6.getRGB(j,i));
                }
            }
        } catch (IOException e) {
            fail();
        }

    }

    @Test
    void test2() {
        String load = "/images/load.png";
        String[][] empty = assign.assignMaze(9);
        String[][] maze = assign.convToMaze(load,20,20);
        try {
            BufferedImage inp = ImageIO.read(new File(System.getProperty("user.dir") + load));
            Image img = assign.getScaledImage(inp, 20, 20);
        } catch (IOException e) {
            fail();
        }
        System.out.println(maze);
        System.out.println(empty);
    }
}
