package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Deals with initializing mazes, will deal with converting from an image to a 2D array.
public class AssignMaze {

    //Will all be changed to references later during GUI phase
    //Empty mazes are initialised as {{null,null}} so the program can find the start and end, they must have that.
    private static final String[][] maze1 = new String[][]{
            {"F", null, "F"},
            {"F", "T", "F"},
            {"F", "T", null}};
    private static final String[][] maze2 = new String[][]{
            {null, "F", "F", "F"},
            {"T", "T", "T", "T"},
            {"F", "T", "F", "T"},
            {"F", "T", "F", null}};
    private static final String[][] maze3 = new String[][]{
            {null, "T", "T", "T", "F", "T"},
            {"T", "F", "F", "T", "F", "T"},
            {"T", "T", "F", "T", "F", "T"},
            {"F", "T", "F", "T", "T", "T"},
            {"T", "T", "T", "F", "F", "F"},
            {"T", "F", "T", "T", "T", null}};
    private String maze4ref = "/images/maze4.png";
    private String maze5ref = "/images/maze5.png";
    private String maze6ref = "/images/maze6.png";
    private String maze7ref = "/images/maze7.png";



    private static final String[][] emptyMaze = new String[][]{
            {null,null}};

    private Color black = Color.BLACK;
    private Color cyan = Color.cyan;
    private Color white = Color.white;

    public AssignMaze(){
    }


    //REQUIRES: pos is an int;
    //EFFECTS: returns the maze which corresponds to the number, all other mazes are returned to be empty {{null,null}}
    //Currently this is only returning the mazes, but later it will also deal with turning
    // the maze images to code readable images
    public String[][] assignMaze(int pos) {
        switch (pos) {
            case 0:
                return mazeClone(maze1);
            case 1:
                return mazeClone(maze2);
            case 2:
                return mazeClone(maze3);
            case 3:
                return convToMaze(maze4ref, 41, 41);
            case 4:
                return convToMaze(maze5ref, 41, 25);
            case 5:
                return convToMaze(maze6ref, 51, 41);
            case 6:
                return convToMaze(maze7ref, 121, 61);
            default:
                return mazeClone(emptyMaze);
        }
    }

    //REQUIRES: maze is not an empty array
    //EFFECTS: Performs a deep copy and sends it back to assignMaze,
    // currently only relevant for testing, might be useful later.
    private String[][] mazeClone(String[][] maze) {
        String[][] ans = new String[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            ans[i] = maze[i].clone();
        }
        return ans;
    }

    //REQUIRES: ref is a link to a maze image, w is number of columns and h is the height
    //EFFECTS: returns @D string array based on input maze via ref
    public String[][] convToMaze(String ref, int w, int h) {
        try {
            BufferedImage inp = ImageIO.read(new File(System.getProperty("user.dir") + ref));
            BufferedImage scaledImg = (BufferedImage) getScaledImage(inp,w,h);
//            ImageIO.write(scaledImg, "png", new File(System.getProperty("user.dir")
//                    + "/images/maze6Test.png"));
            String[][] maze = new String[h][w];
            Color c;
            for (int i = 0; i < scaledImg.getHeight(); i++) {
                for (int j = 0; j < scaledImg.getWidth(); j++) {
                    c = new Color(scaledImg.getRGB(j,i));
                    if (c.equals(cyan)) {
                        maze[i][j] = null;
                    } else if (c.equals(black)) {
                        maze[i][j] = "F";
                    } else if (c.equals(white)) {
                        maze[i][j] = "T";
                    }
                }
            }
            return maze;
        } catch (IOException e) {
            e.getMessage();
        }
        return emptyMaze;
    }

    //Requires: input is an image or ImageIcon, width and height
    //EFFECTS: returns a scaled down images to given width and height
    private Image getScaledImage(Image src, int w, int h) {
        BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

}