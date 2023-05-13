package ui;

import model.Event;
import model.EventLog;
import model.InvalidInputException;
import model.Mazes;
import model.Player;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;

//Runs the main stuff for showing the interface to the user. Deals with inputs for moving player.
public class PickingFrame extends JPanel implements ActionListener {

    private static JsonWriter jsonWriter;
    private static final String data = "./data/saveState.json";

    Mazes mazes;
    ArrayList<Integer> arrange;
    Color color;
    Player player;
    String visibility;
    static int startX;
    static int startY;
    static int endX;
    static int endY;

    private int index;

    JFrame frame;
    private JComboBox<String> inputsCombo;
    private JComboBox<String> colorCombo;
    private JComboBox<String> visCombo;

    protected int scale = 20;
    protected boolean showPath = false;

    private Timer timer;
    private JLabel label;
    private long startTime;
    private final long previousTime;
    private long elapsed;

    private final String[] inputs = {"Maze 1", "Maze 2", "Maze 3", "Maze 4", "Maze 5", "Maze 6", "Maze 7"};
    private final String[] colors = {"black", "blue", "cyan", "gray", "pink", "yellow", "magenta"};
    private final String[] visPick = {"@A", "1", "2", "3", "4", "5", "8", "10"};

    ArrayList<Map<String, Integer>> moves;

    Gra gra;
    boolean notSolved = true;

    boolean hp = false;
    boolean ep = false;
    boolean sp = false;
    boolean op = false;
    boolean yp = false;
    boolean ap = false;

    //Constructor for starting from StartFrame
    public PickingFrame(Mazes mazes, ArrayList<Integer> arrange) {
        this.mazes = mazes;
        this.arrange = arrange;
        previousTime = 0;
        moves = new ArrayList<>();
        try {
            String temp = mazes.getColor();
            Field field = Class.forName("java.awt.Color").getField(temp);
            color = (Color) field.get(null);
        } catch (Exception ex) {
            color = null;
        }
        jsonWriter = new JsonWriter(data);
        createAndShowGUI();
    }

    //Constructor from PickingFrame, adds the time elapsed
    public PickingFrame(Mazes mazes, ArrayList<Integer> arrange, long preTime) {
        this.mazes = mazes;
        this.arrange = arrange;
        previousTime = preTime;
        moves = new ArrayList<>();
        try {
            String temp = mazes.getColor();
            Field field = Class.forName("java.awt.Color").getField(temp);
            color = (Color) field.get(null);
        } catch (Exception ex) {
            color = null;
        }
        jsonWriter = new JsonWriter(data);
        createAndShowGUI();
    }

    //EFFECTS: Runs the first maze picker
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Maze Game");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                System.out.println("GUI closed.");
                System.exit(0);
            }
        });

        //Add content to the window.
        running(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: calls the fn for picking mazes or ends the program with congrats mag
    @SuppressWarnings("methodlength")
    private void running(Container blank) {
        if (mazes.checkAllSolved()) {
            makingFrame(blank);
        } else {
            frame.getContentPane().removeAll();
            JLabel label1 = new JLabel("Congratulations! All mazes complete :D");
            label1.setFont(new Font("Serif", Font.BOLD, 20));
            label1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel label2 = new JLabel("\n Time:");
            label2.setFont(new Font("Serif", Font.BOLD, 20));

            label = new JLabel("00:00:00.000");
            long elapsed = previousTime;
            int minutes = (int) (elapsed / (1000 * 60));
            int seconds = (int) (elapsed / 1000) % 60;
            int millis = (int) (elapsed % 1000);
            label.setText(String.format("%02d:%02d:%03d", minutes, seconds, millis));
            label.setFont(new Font("Serif", Font.BOLD, 20));

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("Done :D"));
            panel.setPreferredSize(new Dimension(400, 130));
            panel.setMaximumSize(new Dimension(400, 130));
            panel.add(label1);
            panel.add(label2);
            panel.add(label);
            frame.add(panel);
            frame.setVisible(true);
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the JFrame for picking which maze to enter, calls the main fn
    @SuppressWarnings("methodlength")
    private void makingFrame(Container blank) {
        JPanel panel = new JPanel();
        JButton b1;
        JButton b2;

        blank.setLayout(new BoxLayout(blank, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createTitledBorder("Picking Maze"));
        panel.setPreferredSize(new Dimension(100, 130));
        panel.setMaximumSize(new Dimension(100, 130));

        ImageIcon quit = new ImageIcon(System.getProperty("user.dir") + "/images/quit.png");
        quit = new ImageIcon(getScaledImage(quit.getImage(), 20, 20));

        inputsCombo = new JComboBox<>(inputs);

        b1 = new JButton("Continue");
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setActionCommand("continue");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        b1.addActionListener(this);

        b2 = new JButton("Quit", quit);
        b2.setFont(font);
        b2.setActionCommand("quit");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        b2.addActionListener(this);

        panel.add(inputsCombo);
        panel.add(b1);
        panel.add(b2);

        blank.add(panel);
    }

    //MODIFIES: this
    //EFFECTS: calls the maze based on the JComboBox selection else quits or saves
    @SuppressWarnings("methodlength")
    public void actionPerformed(ActionEvent e) {
        String selection = (String) inputsCombo.getSelectedItem();
        if ("continue".equals(e.getActionCommand())) {
            switch (selection) {
                case "Maze 1":
                    if (!mazes.checkSolved(arrange.indexOf(0))) {
                        goToMazes(0);
                    }
                    break;
                case "Maze 2":
                    if (!mazes.checkSolved(arrange.indexOf(1))) {
                        goToMazes(1);
                    }
                    break;
                case "Maze 3":
                    if (!mazes.checkSolved(arrange.indexOf(2))) {
                        goToMazes(2);
                    }
                    break;
                case "Maze 4":
                    if (!mazes.checkSolved(arrange.indexOf(3))) {
                        goToMazes(3);
                    }
                    break;
                case "Maze 5":
                    if (!mazes.checkSolved(arrange.indexOf(4))) {
                        goToMazes(4);
                    }
                    break;
                case "Maze 6":
                    if (!mazes.checkSolved(arrange.indexOf(5))) {
                        goToMazes(5);
                    }
                    break;
                case "Maze 7":
                    if (!mazes.checkSolved(arrange.indexOf(6))) {
                        goToMazes(6);
                        scale = 12;
                    }
                    break;
            }
        } else if ("quit".equals(e.getActionCommand())) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } else if ("save".equals(e.getActionCommand())) {
            saveState();
        }
        frame.setVisible(true);
    }

    // EFFECTS: saves the current state of mazes to file
    private void saveState() {
        try {
            jsonWriter.open();
            jsonWriter.write(mazes, elapsed);
            jsonWriter.close();
            System.out.println("Saved to " + data);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + data);
        }
    }

    //Requires: input is an image or ImageIcon, width and height
    //EFFECTS: returns a scaled down images to given width and height
    private static Image getScaledImage(Image src, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

    private float colorTimer = 0f;
    private long lastTime = 0L;

    //MODIFIES: this
    //EFFECTS: structures the frame will all relevant components and the maze below;
    @SuppressWarnings("methodlength")
    public void goToMazes(int inp) {
        try {
            Field field = Class.forName("java.awt.Color").getField(mazes.getColor());
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null;
        }

        label = new JLabel("00:00:00.000");
        timer = new Timer(10, this);
        startTime = System.currentTimeMillis();
        timer.start();


        index = arrange.indexOf(inp);
        String[][] maze = mazes.getMaze(index);
        Container container = frame.getContentPane();
        initializeRequired(index);
        visibility = "@A";

        container.removeAll();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        gra = new Gra(maze);
        gra.repaint(100, 100, gra.getWidth(), gra.getHeight());

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                elapsed = (now - startTime) + previousTime;
                int minutes = (int) (elapsed / (1000 * 60));
                int seconds = (int) (elapsed / 1000) % 60;
                int millis = (int) (elapsed % 1000);
                label.setText(String.format("%02d:%02d:%03d", minutes, seconds, millis));
                long deltaTime = now - lastTime;
                colorTimer -= deltaTime / 1000f;
                if (colorTimer <= 0) {
                    gra.repaint();
                    colorTimer = 0.1f;
                }
                lastTime = now;
            }
        });

        int width = 400;
        int height = 400;
        if (maze[0].length > 12) {
            width = 20 + (maze[0].length * scale) + 100;
        }
        if (maze.length > 8) {
            height = 130 + 30 + (maze.length * scale) + 50;
        }
        if (width > 400) {
            frame.setMinimumSize(new Dimension(width, height));
        } else {
            frame.setMinimumSize(new Dimension(400, 400));
        }

        colorCombo = new JComboBox<>(colors);
        colorCombo.setSelectedIndex(java.util.Arrays.asList(colors).indexOf(mazes.getColor()));
        colorCombo.setMaximumSize(new Dimension(80, 30));
        colorCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        visCombo = new JComboBox<>(visPick);
        visCombo.setSelectedIndex(java.util.Arrays.asList(visPick).indexOf(visibility));
        visCombo.setMaximumSize(new Dimension(80, 30));
        visCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        colorCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = (String) colorCombo.getSelectedItem();
                try {
                    Field field = Class.forName("java.awt.Color").getField(temp);
                    mazes.setColor(temp);
                    color = (Color) field.get(null);
                    gra.repaint();
                    colorCombo.transferFocusBackward();
                } catch (Exception ex) {
                    color = null;
                }
            }
        });

        visCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visibility = (String) visCombo.getSelectedItem();
                gra.repaint();
                visCombo.transferFocus();
            }
        });

        JButton b1 = new JButton("Save Game");
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setActionCommand("save");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon quit = new ImageIcon(System.getProperty("user.dir") + "/images/quit.png");
        quit = new ImageIcon(getScaledImage(quit.getImage(), 19, 19));
        JButton b2 = new JButton("Quit", quit);
        b2.setActionCommand("quit");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);

        b1.addActionListener(this);
        b2.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Maze"));
        panel.setPreferredSize(new Dimension(400, 90));
        panel.setMaximumSize(new Dimension(400, 90));
        panel.setMinimumSize(new Dimension(400, 90));
        panel.add(colorCombo);
        panel.add(b1);
        panel.add(b2);
        panel.add(visCombo);
        panel.add(label);
        frame.add(panel, 0);
        frame.add(gra, 1);

        KeyHandler keyListen = new KeyHandler();

        frame.addKeyListener(keyListen);

        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.requestFocus();

    }

    //MODIFIES: this
    //EFFECTS: calls the constants that may be required and stores them in this class;
    private void initializeRequired(int inp) {
        Map<String, Object> temp = mazes.getReq(inp);
        player = (Player) temp.get("player");
        startX = (int) temp.get("startX");
        startY = (int) temp.get("startY");
        endX = (int) temp.get("endX");
        endY = (int) temp.get("endY");
    }

    //MODIFIES: this and mazes.mazes[index].player
    //EFFECTS: moves the player and adds the new place to the list of positions done before (aka moves)
    // If player at end then calls a new instance of PickingFrame where current maze is solved, sends over time as well
    // Also checks if cheatCode entered correctly
    @SuppressWarnings("methodlength")
    private void move(int code) {
        boolean moved = false;
        cheat(code);
        cheat2(code);
        if (!notSolved) {
            frame.setEnabled(false);
            frame.setVisible(false);
            new PickingFrame(mazes, arrange, elapsed);
        }
        if (code == KeyEvent.VK_RIGHT) {
            try {
                mazes.possibleMove(index, "right");
                mazes.applyMove(index, "right");
                moved = true;
            } catch (InvalidInputException e) {
                //No use here
            }
        } else if (code == KeyEvent.VK_LEFT) {
            try {
                mazes.possibleMove(index, "left");
                mazes.applyMove(index, "left");
                moved = true;
            } catch (InvalidInputException e) {
                //No use here
            }
        } else if (code == KeyEvent.VK_UP) {
            try {
                mazes.possibleMove(index, "up");
                mazes.applyMove(index, "up");
                moved = true;
            } catch (InvalidInputException e) {
                //No use here
            }
        } else if (code == KeyEvent.VK_DOWN) {
            try {
                mazes.possibleMove(index, "down");
                mazes.applyMove(index, "down");
                moved = true;
            } catch (InvalidInputException e) {
                //No use here
            }
        }
        if (moved) {
            Map<String, Integer> temp = new HashMap<>();
            temp.put("posX", player.getX());
            temp.put("posY", player.getY());
            moves.add(temp);
        }
        boolean temp = mazes.solved(index);
        if (!temp && notSolved) {
            long now = System.currentTimeMillis();
            long elapsed = (now - startTime) + previousTime;
            timer.stop();
            notSolved = false;
            frame.repaint();
        }
    }

    //MODIFIES: this (mazes.mazes[index].player)
    //EFFECTS: cheat code ;)
    @SuppressWarnings("methodlength")
    private void cheat(int code) {
        if (code == KeyEvent.VK_M && ap) {
            mazes.quickSolve2(index);
            frame.repaint();
        } else if (code == KeyEvent.VK_A && yp) {
            ap = true;
            yp = false;
        } else if (code == KeyEvent.VK_Y && op) {
            yp = true;
            op = false;
        } else if (code == KeyEvent.VK_O && sp) {
            op = true;
            sp = false;
        } else if (code == KeyEvent.VK_S && ep) {
            sp = true;
            ep = false;
        } else if (code == KeyEvent.VK_E && hp) {
            ep = true;
            hp = false;
        } else if (code == KeyEvent.VK_H) {
            hp = true;
        } else {
            hp = false;
            ep = false;
            sp = false;
            op = false;
            yp = false;
            ap = false;
        }
    }

    boolean sp1 = false;
    boolean hp1 = false;
    boolean op1 = false;
    boolean wp1 = false;
    boolean dp1 = false;
    boolean ap1 = false;
    boolean wp2 = false;
    boolean ap2 = false;

    @SuppressWarnings("methodlength")
    private void cheat2(int code) {
        if (code == KeyEvent.VK_Y && ap2) {
            showPath = true;
            ap2 = false;
        } else if (code == KeyEvent.VK_A && wp2) {
            ap2 = true;
            wp2 = false;
        } else if (code == KeyEvent.VK_W && ap1) {
            wp2 = true;
            ap1 = false;
        } else if (code == KeyEvent.VK_A && dp1) {
            ap1 = true;
            dp1 = false;
        } else if (code == KeyEvent.VK_D && wp1) {
            dp1 = true;
            wp1 = false;
        } else if (code == KeyEvent.VK_W && op1) {
            wp1 = true;
            op1 = false;
        } else if (code == KeyEvent.VK_O && hp1) {
            op1 = true;
            hp1 = false;
        } else if (code == KeyEvent.VK_H && sp1) {
            hp1 = true;
            sp1 = false;
        } else if (code == KeyEvent.VK_S) {
            sp1 = true;
        } else {
            sp1 = false;
            hp1 = false;
            op1 = false;
            wp1 = false;
            dp1 = false;
            ap1 = false;
            wp2 = false;
            ap2 = false;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            showPath = false;
        }
    }

    private int acc = 0;

    //EFFECTS: creates a graphics image of the maze at all the right places.
    private class Gra extends JComponent {
        private static final long serialVersionUID = 1L;
        String[][] maze;
        int plX;
        int plY;
        int fixedDist = 3;

        Gra(String[][] inp) {
            maze = inp;
        }

        //Calls paintComponent and also sets the maze graphics
        @SuppressWarnings("methodlength")
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int temp = frame.getWidth() - 20;
            temp = (temp - (maze[0].length * scale)) / 2;

            int temp2 = frame.getHeight() - 130;
            temp2 = (temp2 - (maze.length * scale)) / 2;

            plX = player.getX();
            plY = player.getY();

            HashSet<Point> beenTo = new HashSet<>();
            ArrayDeque<Point> dq = new ArrayDeque<>();
            recurse(startX, startY, beenTo, dq);
            dq.removeLast();
            dq.removeFirst();
            dq.remove(new Point(plX, plY));
            acc++;
            float s = Math.abs((acc % 50) / 50f - 0.5f);

            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    pickColor(i, j, g);
                    int curX = temp + j * scale;
                    int curY = temp2 + i * scale;
                    if (showPath) {
                        if (dq.contains(new Point(j, i))) {
                            g.setColor(new Color(
                                    s * curX / (float) getWidth(),
                                    (1f - s) * curY / (float) getHeight(),
                                    1f));
                        }
                    }
                    g.fillRect(curX, curY, scale, scale);
                }
            }
        }

        @SuppressWarnings("methodlength")
        private boolean recurse(int startX, int startY, HashSet<Point> beenTo, ArrayDeque<Point> curPath) {
            Point left = new Point(startX - 1, startY);
            Point right = new Point(startX + 1, startY);
            Point up = new Point(startX, startY - 1);
            Point down = new Point(startX, startY + 1);

            if (startX == endX && startY == endY) {
                curPath.addLast(new Point(startX, startY));
                return true;
            } else if (startX < 0 || startX >= maze[0].length || startY < 0 || startY >= maze.length
                    || maze[startY][startX].equals("F")) {
                return false;
            }
            curPath.addLast(new Point(startX, startY));
            boolean found = false;
            if (!beenTo.contains(left)) {
                beenTo.add(left);
                found = recurse(left.xx, left.yy, beenTo, curPath);
            }
            if (!found && !beenTo.contains(right)) {
                beenTo.add(right);
                found = recurse(right.xx, right.yy, beenTo, curPath);
            }
            if (!found && !beenTo.contains(up)) {
                beenTo.add(up);
                found = recurse(up.xx, up.yy, beenTo, curPath);
            }
            if (!found && !beenTo.contains(down)) {
                beenTo.add(down);
                found = recurse(down.xx, down.yy, beenTo, curPath);
            }
            if (found) {
                return true;
            }
            curPath.removeLast();
            return false;
        }

        private class Point {
            int xx;
            int yy;

            public Point(int xx, int yy) {
                this.xx = xx;
                this.yy = yy;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Point point = (Point) o;
                return xx == point.xx && yy == point.yy;
            }

            @Override
            public int hashCode() {
                return Objects.hash(xx, yy);
            }
        }

        //Sets the color based on current position, if visibility is a number, then sets it to that and also path enable
        @SuppressWarnings("methodlength")
        private void pickColor(int i, int j, Graphics g) {
            if (j == plX && i == plY) {
                g.setColor(Color.red);
            } else if (j == startX && i == startY) {
                g.setColor(Color.orange);
            } else if (j == endX && i == endY) {
                g.setColor(Color.green);
            } else if (maze[i][j].equals("F")) {
                g.setColor(color);
            } else {
                g.setColor(Color.white);
            }

            if (!visibility.equals("@A")) {
                try {
                    boolean black = true;
                    int vis = Integer.parseInt(visibility);
                    double dist1 = Math.sqrt(Math.pow((plX - j), 2) + Math.pow((plY - i), 2));
                    double dist2 = Math.sqrt(Math.pow((startX - j), 2) + Math.pow((startY - i), 2));
                    double dist3 = Math.sqrt(Math.pow((endX - j), 2) + Math.pow((endY - i), 2));
                    for (Map<String, Integer> move : moves) {
                        double distMoves = Math.sqrt(Math.pow((move.get("posX") - j), 2)
                                + Math.pow((move.get("posY") - i), 2));
                        int maxVis = Math.min(vis,3);
                        if (distMoves < maxVis) {
                            black = false;
                            break;
                        }
                    }
                    if ((dist1 > (double) vis) && (dist2 > fixedDist) && (dist3 > fixedDist) && black) {
                        g.setColor(Color.black);
                    }
                } catch (NumberFormatException e) {
                    //Ignore the msg, is oly at the error of the math operations.
                }
            }
        }
    }

    //Handles key Inputs used ot move the player
    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
//            System.out.println(e.getKeyCode() + "Key Typed");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            move(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
//            System.out.println(e.getKeyCode() + "Key Released");
        }
    }

    private void printLog(EventLog el) {
        for (Event event : el) {
            System.out.println(event.toString() + "\n");
        }
    }

}