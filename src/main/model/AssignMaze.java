package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//Deals with initializing mazes, will deal with converting from an image to a 2D array.
public class AssignMaze {

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
    private final String maze4ref = "/images/maze4.png";
    private final String maze5ref = "/images/maze5.png";
    private final String maze6ref = "/images/maze6.png";
    private final String maze7ref = "/images/maze7.png";



    private static final String[][] emptyMaze = new String[][]{
            {null,null}};

    private final Color black = Color.BLACK;
    private final Color cyan = Color.cyan;
    private final Color white = Color.white;

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
//                return convToMaze(maze4ref, 41, 41);
                return convToMaze(maze4ref);
            case 4:
//                return convToMaze(maze5ref, 41, 25);
                return convToMaze(maze5ref);
            //Reason this doesn't work is cause of img, edit to so that each box is exact 15 pixels wide.
            //Above has been fixed, worked out math.ceil
            case 5:
//                return convToMaze(maze6ref, 51, 41);
                return convToMaze(maze6ref);
            case 6:
//                return convToMaze(maze7ref, 121, 61);
                return convToMaze(maze7ref);
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
            System.out.println(e.getMessage());
        }
        return emptyMaze;
    }

    private int scale;


    //New version of above function, it takes in any ref and converts it to a maze with no extra inputs
    //Purpose is to allow user to change mazes as desired.
    @SuppressWarnings("methodlength")
    public String[][] convToMaze(String ref) {
        try {
            BufferedImage inp = ImageIO.read(new File(System.getProperty("user.dir") + ref));
            setScale(inp);

            int w = (int) Math.ceil((double) inp.getWidth() / (double) scale);
            int h = (int) Math.ceil((double) inp.getHeight() / (double) scale);

            BufferedImage scaledImg = (BufferedImage) getScaledImage(inp,w,h);
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
            boolean containsNull = false;
            for (int i = 0; i < maze.length; i++) {
                if (java.util.Arrays.asList(maze[i]).contains(null)) {
                    containsNull = true;
                    break;
                }
            }
            if (!containsNull) {
                return addNulls(maze);
            }
            return maze;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return emptyMaze;
    }

    @SuppressWarnings("methodlength")
    private void setScale(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        int samples = 50;
        int jump = 1;
        if (height < 50) {
            samples = height;
        } else {
            jump = height / samples;
        }
        Color first;
        Color temp;
        int count;
        int localI;
        ArrayList<Integer> numbs = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            count = 0;
            localI = i * jump;
            first = new Color(img.getRGB(0,localI));
            for (int j = 0; j < width; j++) {
                temp = new Color(img.getRGB(j,localI));
                if (!temp.equals(first)) {
                    numbs.add(count);
                    break;
                }
                count++;
            }
        }

        int lowest;
        int occurrences;
        ArrayList<Integer> blackList = new ArrayList<>();
        do {
            lowest = (int) Double.POSITIVE_INFINITY;
            for (int a : numbs) {
                if ((a < lowest) && !blackList.contains(a)) {
                    lowest = a;
                }
            }
            occurrences = 0;
            for (int a : numbs) {
                if (a == lowest) {
                    occurrences++;
                }
            }
            blackList.add(lowest);
        } while (occurrences <= 3);
        //Above part finds the lowest size of the blocks, uses the first column. This part ensures that it isn't some
        //grey area (error in the image) for security
        scale = lowest;
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

    private String[][] addNulls(String[][] inpMaze) {
        int width = inpMaze[0].length;
        int totalWidthBlocks = 0;
        for (int i = 0; i < width; i++) {
            if (inpMaze[0][i].equals("F")) {
                totalWidthBlocks++;
            }
        }
        int height = inpMaze.length;
        int totalHeightBlocks = 0;
        for (int i = 0; i < height; i++) {
            if (inpMaze[i][0].equals("F")) {
                totalHeightBlocks++;
            }
        }
        width--;
        height--;
        if ((width == totalWidthBlocks) || (height == totalHeightBlocks)) {
            return predefinedFix(inpMaze, height, width);
        } else {
            return newFix(inpMaze);
        }
    }

    private String[][] predefinedFix(String[][] inpMaze, int height, int width) {
        for (int i = 0; i < width + 1; i++) {
            if (inpMaze[0][i].equals("T")) {
                inpMaze[0][i] = null;
            }
            if (inpMaze[height][i].equals("T")) {
                inpMaze[height][i] = null;
            }
        }
        for (int i = 0; i < height + 1; i++) {
            if (inpMaze[i][0].equals("T")) {
                inpMaze[i][0] = null;
            }
            if (inpMaze[i][width].equals("T")) {
                inpMaze[i][width] = null;
            }
        }
        return inpMaze;
    }

    private String[][] newFix(String[][] inpMaze) {

        return emptyMaze;
    }

}