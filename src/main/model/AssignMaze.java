package model;

public class AssignMaze {

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
    private static final String[][] maze4 = new String[][]{
            {null,null}};
    private static final String[][] maze5 = new String[][]{
            {null,null}};
    private static final String[][] maze6 = new String[][]{
            {null,null}};

    //Empty mazes are initialised as {{null,null}} so the program can find the start and end, they must have that.
    private static final String[][] emptyMaze = new String[][]{
            {null,null}};

    public AssignMaze(){
    }


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
                return mazeClone(maze4);
            case 4:
                return mazeClone(maze5);
            case 5:
                return mazeClone(maze6);
            default:
                return mazeClone(emptyMaze);
        }
    }

    private String[][] mazeClone(String[][] maze) {
        String[][] ans = new String[maze.length][];
        for (int i = 0; i < maze.length; i++) {
            ans[i] = maze[i].clone();
        }
        return ans;
    }

}