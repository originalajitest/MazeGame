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
import java.awt.image.ImageObserver;
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

    protected double height;
    protected double width;
    protected int scale = 20;
    protected boolean showPath = false;
    protected boolean wall = false;
    protected boolean zoomStatus = false;
    protected boolean zoomPossible = false;
    protected int scale2 = 0;
    protected int maxVertVis;
    protected int maxHorVis;

    protected Image imgWall;
    protected Image imgWall1;
    protected Image imgWall2;
    protected Image imgWall3;
    ImageObserver imgObserve;
    protected int wallIndex;

    private Timer timer;
    private JLabel label;
    private long startTime;
    private final long previousTime;
    private long elapsed;

    private final String[] inputs = {"Maze 1", "Maze 2", "Maze 3", "Maze 4", "Maze 5", "Maze 6", "Maze 7", "Maze 8",
            "Maze 9"};
    private final ArrayList<String> colors = new ArrayList<>();
    private final String[] visPick = {"@A", "1", "2", "3", "4", "5", "8", "10"};

    ArrayList<Map<String, Integer>> moves;

    Gra gra;
    boolean notSolved = true;

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

        //Getting height of title bar, this took soo long :'(
        JFrame tempFrame = new JFrame("lol");
        tempFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JPanel tempPanel = new JPanel();
        tempPanel.setPreferredSize(new Dimension(300, 200));
        tempFrame.setContentPane(tempPanel);
        tempFrame.pack();
        int titleBar = tempFrame.getHeight() - tempPanel.getHeight();
        tempFrame.dispose();

        //Gets the size of workable space.
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = environment.getMaximumWindowBounds();
        height = bounds.height - titleBar;
        width = bounds.width - 2;

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                System.out.println("GUI closed.");
                System.exit(0);
            }
        });

        setAbsolutes();

        //Add content to the window.
        running(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void setAbsolutes() {
        colors.add("black");
        colors.add("blue");
        colors.add("cyan");
        colors.add("gray");
        colors.add("pink");
        colors.add("yellow");
        colors.add("magenta");

        imgWall = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/wall.png"))).getImage();
        imgWall1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/wall1.png"))).getImage();
        imgWall2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/wall2.png"))).getImage();
        imgWall3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/wall3.png"))).getImage();

        imgObserve = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        };
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

        ImageIcon quit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/quit.png")));
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
            if (selection.equals("Maze 1") && !mazes.checkSolved(arrange.indexOf(0))) {
                goToMazes(0);
            } else if (selection.equals("Maze 2") && !mazes.checkSolved(arrange.indexOf(1))) {
                goToMazes(1);
            } else if (selection.equals("Maze 3") && !mazes.checkSolved(arrange.indexOf(2))) {
                goToMazes(2);
            } else if (selection.equals("Maze 4") && !mazes.checkSolved(arrange.indexOf(3))) {
                goToMazes(3);
            } else if (selection.equals("Maze 5") && !mazes.checkSolved(arrange.indexOf(4))) {
                goToMazes(4);
            } else if (selection.equals("Maze 6") && !mazes.checkSolved(arrange.indexOf(5))) {
                goToMazes(5);
            } else if (selection.equals("Maze 7") && !mazes.checkSolved(arrange.indexOf(6))) {
                goToMazes(6);
            } else if (selection.equals("Maze 8") && !mazes.checkSolved(arrange.indexOf(7))) {
                goToMazes(7);
            } else if (selection.equals("Maze 9") && !mazes.checkSolved(arrange.indexOf(8))) {
                goToMazes(8);
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

        double hScale = (height-90) / (double) maze.length;
        double wScale = width / (double) maze[0].length;
        if (((hScale) < 20) || ((wScale) < 20)) {
            scale = (int) Math.floor(hScale);
            if (scale > wScale) {
                scale = (int) Math.floor(wScale);
            }
            scale2 = scale;
            zoomPossible = true;
            maxVertVis = (int) ((height - 90) / 20);
            maxHorVis = (int) (width / 20);
        } else {
            colors.add("wall0");
            colors.add("wall1");
            colors.add("wall2");
            colors.add("wall3");
        }

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
                    gra.paintUpdate();
                    gra.repaint();
                    colorTimer = 0.1f;
                }
                lastTime = now;
            }
        });

        int width = 20 + (maze[0].length * scale) + 100;
        int height = 130 + 30 + (maze.length * scale) + 50;
        if (scale != 20) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else if ((width > 400) || (height > 400)) {
            frame.setMinimumSize(new Dimension(width, height));
        } else {
            frame.setMinimumSize(new Dimension(400, 400));
        }

        colorCombo = new JComboBox<>();
        colorCombo.setModel(new DefaultComboBoxModel<>(colors.toArray(new String[0])));
        colorCombo.setSelectedIndex(colors.indexOf(mazes.getColor()));
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
                if (temp.contains("wall")) {
                    wall = true;
                    wallIndex = temp.charAt(4) - '0';
                } else {
                    wall = false;
                    try {
                        Field field = Class.forName("java.awt.Color").getField(temp);
                        mazes.setColor(temp);
                        color = (Color) field.get(null);
                    } catch (Exception ex) {
                        color = null;
                    }
                }
                gra.repaint();
                colorCombo.transferFocusBackward();
            }
        });

        visCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visibility = (String) visCombo.getSelectedItem();
                EventLog.getInstance().logEvent(new Event("On Maze " + inp + " visibility changed to "
                        + visibility));
                gra.repaint();
                visCombo.transferFocus();
            }
        });

        JButton b1 = new JButton("Save Game");
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setActionCommand("save");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon quit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/quit.png")));
        quit = new ImageIcon(getScaledImage(quit.getImage(), 20, 20));
        JButton b2 = new JButton("Quit", quit);
        b2.setActionCommand("quit");
        b2.setFont(font);
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
    private void move(int code) throws InvalidInputException {
        boolean moved = false;
        cheat(code);
        cheat2(code);
        zoom(code);
        if (!notSolved) {
            frame.setEnabled(false);
            frame.setVisible(false);
            new PickingFrame(mazes, arrange, elapsed);
        }
        if (code == KeyEvent.VK_RIGHT) {
            mazes.possibleMove(index, "right");
            mazes.applyMove(index, "right");
            moved = true;
        } else if (code == KeyEvent.VK_LEFT) {
            mazes.possibleMove(index, "left");
            mazes.applyMove(index, "left");
            moved = true;
        } else if (code == KeyEvent.VK_UP) {
            mazes.possibleMove(index, "up");
            mazes.applyMove(index, "up");
            moved = true;
        } else if (code == KeyEvent.VK_DOWN) {
            mazes.possibleMove(index, "down");
            mazes.applyMove(index, "down");
            moved = true;
        }
        if (moved) {
            Map<String, Integer> temp = new HashMap<>();
            temp.put("posX", player.getX());
            temp.put("posY", player.getY());
            moves.add(temp);
            gra.repaint();
        }
        if (notSolved) {
            boolean temp = mazes.solved(index);
            if (!temp) {
                long now = System.currentTimeMillis();
                elapsed = (now - startTime) + previousTime;
                timer.stop();
                notSolved = false;
                frame.repaint();
            }
        }
    }

    private void zoom (int code) {
        if (zoomPossible) {
            if (code == KeyEvent.VK_Z) {
                if (!zoomStatus) {
                    zoomStatus = true;
                    scale = 20;
                    colors.add("wall0");
                    colors.add("wall1");
                    colors.add("wall2");
                    colors.add("wall3");
                    colorCombo.setModel(new DefaultComboBoxModel<>(colors.toArray(new String[0])));
                } else {
                    zoomStatus = false;
                    scale = scale2;
                    colors.remove("wall0");
                    colors.remove("wall1");
                    colors.remove("wall2");
                    colors.remove("wall3");
                    colorCombo.setModel(new DefaultComboBoxModel<>(colors.toArray(new String[0])));
                    colorCombo.setSelectedItem("black");
                }
            }
        }
    }

    private int skipCode = 0;

    //MODIFIES: this (mazes.mazes[index].player)
    //EFFECTS: cheat code ;)
    @SuppressWarnings("methodlength")
    private void cheat(int code) {
        if (code > 64 && code < 90) {
            if (code == KeyEvent.VK_M && skipCode == 6) {
                mazes.quickSolve2(index);
                frame.repaint();
            } else if (code == KeyEvent.VK_A && skipCode == 5) {
                skipCode++;
            } else if (code == KeyEvent.VK_Y && skipCode == 4) {
                skipCode++;
            } else if (code == KeyEvent.VK_O && skipCode == 3) {
                skipCode++;
            } else if (code == KeyEvent.VK_S && skipCode == 2) {
                skipCode++;
            } else if (code == KeyEvent.VK_E && skipCode == 1) {
                skipCode++;
            } else if (code == KeyEvent.VK_H && skipCode == 0) {
                skipCode++;
            } else {
                skipCode = 0;
            }
        }
    }

    private int wayCode = 0;

    @SuppressWarnings("methodlength")
    private void cheat2(int code) {
        if ((code > 64 && code < 90) || code == 27) {
            if (code == KeyEvent.VK_Y && wayCode == 8) {
                showPath = true;
                wayCode = 0;
            } else if (code == KeyEvent.VK_A && wayCode == 7) {
                wayCode++;
            } else if (code == KeyEvent.VK_W && wayCode == 6) {
                wayCode++;
            } else if (code == KeyEvent.VK_A && wayCode == 5) {
                wayCode++;
            } else if (code == KeyEvent.VK_D && wayCode == 4) {
                wayCode++;
            } else if (code == KeyEvent.VK_W && wayCode == 3) {
                wayCode++;
            } else if (code == KeyEvent.VK_O && wayCode == 2) {
                wayCode++;
            } else if (code == KeyEvent.VK_H && wayCode == 1) {
                wayCode++;
            } else if (code == KeyEvent.VK_S && wayCode == 0) {
                wayCode++;
            } else {
                wayCode = 0;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                showPath = false;
            }
        }
    }

    private int acc = 0;
    private long lastTime2 = 0L;

    //EFFECTS: creates a graphics image of the maze at all the right places.
    private class Gra extends JComponent {
        private static final long serialVersionUID = 1L;
        String[][] maze;
        int plX;
        int plY;
        int fixedDist = 3;
        ArrayDeque<Point> dq;
        HashSet<Point> beenTo;
        int plChange = 0;

        Gra(String[][] inp) {
            maze = inp;
            beenTo = new HashSet<>();
            dq = new ArrayDeque<>();
            recurse(startX, startY, beenTo, dq);
            dq.removeLast();
            dq.removeFirst();
        }

        public void paintUpdate() {
            acc++;
            if (acc % 2 == 0) {
                plChange++;
            }
        }

        private boolean contained = false;

        //Calls paintComponent and also sets the maze graphics
        @SuppressWarnings("methodlength")
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            plX = player.getX();
            plY = player.getY();

            Point plPoint = new Point(plX, plY);

            if (dq.contains(plPoint)) {
                dq.remove(plPoint);
                contained = true;
            }
            if (zoomStatus) {
                printZoomMaze(g);
            } else {
                printMaze(g);
            }

            if (contained) {
                dq.add(plPoint);
                contained = false;
            }
            long now = System.currentTimeMillis();
            elapsed = (now - startTime) + previousTime;
            long deltaTime = now - lastTime2;
            if (deltaTime != 0) {
                long frameRate = 1000 / deltaTime;
                System.out.println(frameRate);
                lastTime2 = now;
            }
        }

        private void paintWall(Graphics g, int curX, int curY) {
            if (wallIndex == 0) {
                g.drawImage(imgWall, curX, curY, imgObserve);
            } else if (wallIndex == 1) {
                g.drawImage(imgWall1, curX, curY, imgObserve);
            } else if (wallIndex == 2) {
                g.drawImage(imgWall2, curX, curY, imgObserve);
            } else {
                g.drawImage(imgWall3, curX, curY, imgObserve);
            }

        }

        private void printMaze(Graphics g) {
            int temp = frame.getWidth() - 20;
            temp = (temp - (maze[0].length * scale)) / 2;

            int temp2 = frame.getHeight() - 130;
            temp2 = (temp2 - (maze.length * scale)) / 2;

            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    printAll(g,i,j,temp,temp2);
                }
            }
        }

        private int curXLow = 0;
        private int curYLow = 0;

        private void printZoomMaze(Graphics g) {
            int temp = setStartX();
            int temp2 = setStartY();

            for (int i = curYLow; i < maze.length; i++) {
                for (int j = curXLow; j < maze[0].length; j++) {
                    if (j >= (maxHorVis + curXLow)) break;
                    printAll(g,i,j,temp,temp2);
                }
                if (i >= (maxVertVis + curYLow)) break;
            }
        }

        private void printAll(Graphics g, int i, int j, int temp, int temp2) {
            float s = Math.abs((acc % 50) / 50f - 0.5f);
            pickColor(i, j, g);
            int curX = temp + j * scale;
            int curY = temp2 + i * scale;
            if (showPath) {
                if (dq.contains(new Point(j, i))) {
                    float red = Math.abs((1f - s) * curX / (float) getWidth() - 1f);
                    float green = Math.abs((1f - s) * curY / (float) getHeight() - 1f);
                    float blue = Math.abs((red + green) / 2 - 1f);
                    g.setColor(new Color(red, green, blue));
                }
            }
            if (j == plX && i == plY) {
                renderPlayer(g, curX, curY);
            } else {
                if (wall) {
                    if (g.getColor().equals(color) && maze[i][j] == "F" && !vis(i,j)) {
                        paintWall(g, curX, curY);
                    } else {
                        g.fillRect(curX, curY, scale, scale);
                    }
                } else {
                    g.fillRect(curX, curY, scale, scale);
                }
            }
        }

        private int setStartX() {
            int val;
            if (maze[0].length <= maxHorVis) {
                val = frame.getWidth() - 20;
                val = (val - (maze[0].length * scale)) / 2;
            } else {
                if ((curXLow > plX) || (plX >= (maxHorVis + curXLow))) {
                    if (plX < curXLow) {
                        curXLow = curXLow - maxHorVis;
                    } else {
                        curXLow = curXLow + maxHorVis;
                    }
                    if ((maxHorVis + curXLow) > (maze[0].length - 1)) {
                        curXLow = maze[0].length - maxHorVis;
                    } else if (curXLow < 0) {
                        curXLow = 0;
                    }
                }
                val = - (curXLow * scale);
            }
            return val;
        }

        private int setStartY() {
            int val;
            if (maze.length <= maxVertVis) {
                val = frame.getHeight() - 130;
                val = (val - (maze.length * scale)) / 2;
            } else {
                if ((curYLow > plY) || (plY >= (maxVertVis + curYLow))) {
                    if (plY < curYLow) {
                        curYLow = curYLow - maxVertVis;
                    } else {
                        curYLow = curYLow + maxVertVis;
                    }
                    if ((maxVertVis + curYLow) > (maze.length - 1)) {
                        curYLow = maze.length - maxVertVis;
                    } else if (curYLow < 0) {
                        curYLow = 0;
                    }
                }
                val = - (curYLow * scale);
            }
            return val;
        }

        private void renderPlayer(Graphics g, int curX, int curY) {
            if (contained && showPath) {
                float s = Math.abs((acc % 50) / 50f - 0.5f);
                float red = Math.abs((1f - s) * curX / (float) getWidth() - 1f);
                float green = Math.abs((1f - s) * curY / (float) getHeight() - 1f);
                float blue = Math.abs((red + green) / 2 - 1f);
                g.setColor(new Color(red, green,blue));
                g.fillRect(curX, curY, scale, scale);
            } else {
                g.setColor(Color.white);
                g.fillRect(curX, curY, scale, scale);
            }

            g.setColor(Color.RED);
            if (plChange % 8 == 0) {
                g.fillRect(curX, curY, scale, scale);
            } else if ((plChange % 8 == 1) || (plChange % 8 == 7)) {
                g.fillRect(curX + 1, curY + 1, scale - 2, scale - 2);
            } else if ((plChange % 8 == 2) || (plChange % 8 == 6)) {
                g.fillRect(curX + 2, curY + 2, scale - 4, scale - 4);
            } else if ((plChange % 8 == 3) || (plChange % 8 == 5)) {
                g.fillRect(curX + 3, curY + 3, scale - 6, scale - 6);
            } else {
                g.fillRect(curX + 4, curY + 4, scale - 8, scale - 8);
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
                //This code will never run due to how the mazes are structured
            } else if (startX < 0 || startX >= maze[0].length || startY < 0 || startY >= maze.length
                    || maze[startY][startX].equals("F")) {
                return false;
                //This code should also never run as mazes have start's and end's initialized from nulls' and are set
                // to "T" once found, they are always within boundaries of the 2D Maze array
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
                    boolean black = vis(i,j);
                    double dist1 = Math.sqrt(Math.pow((plX - j), 2) + Math.pow((plY - i), 2));
                    double dist2 = Math.sqrt(Math.pow((startX - j), 2) + Math.pow((startY - i), 2));
                    double dist3 = Math.sqrt(Math.pow((endX - j), 2) + Math.pow((endY - i), 2));
                    int vis = Integer.parseInt(visibility);
                    if ((dist1 > (double) vis) && (dist2 > fixedDist) && (dist3 > fixedDist) && black) {
                        g.setColor(Color.black);
                    }
                } catch (NumberFormatException e) {
                    //Ignore the msg, is only at the error of the math operations.
                }
            }
        }

        private boolean vis(int i, int j) {
            if (visibility=="@A") return false;
            int vis = Integer.parseInt(visibility);
            if (Math.sqrt(Math.pow((plX - j),2) + Math.pow((plY - i),2)) < vis) {
                return false;
            }
            for (Map<String, Integer> move : moves) {
                double distMoves = Math.sqrt(Math.pow((move.get("posX") - j), 2)
                        + Math.pow((move.get("posY") - i), 2));
                int maxVis = Math.min(vis,3);
                if (distMoves < maxVis) {
                    return false;
                }
            }
            return true;
        }

    }

    //Handles key Inputs used to move the player and everything else
    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
//            System.out.println(e.getKeyCode() + "Key Typed");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            try {
                move(e.getKeyCode());
            } catch (InvalidInputException exception) {
                //Ignore
            }
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