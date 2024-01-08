package ui;

import model.Event;
import model.EventLog;
import model.ImageFilter;
import model.Mazes;
import persistence.JsonReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

//This is where the user picks where to go into a new game or an old saved game state.
public class StartFrame extends JPanel implements ActionListener {
    protected static JButton b1;
    protected static JButton b2;
    protected static JButton b3;
    protected static JButton b4;
    protected static JButton openButton;
    protected static JButton saveButton;

    static Random rand = new Random();
    static Mazes mazes;
    static ArrayList<Integer> arrangement = new ArrayList<>();

    private static final String data = "saveState.json";
    static JsonReader jsonReader;

    JFrame frame;
    JFileChooser fc;
    JTextArea log;
    BufferedImage img;
    File file;
    JLabel picImg;

    final String[] mazesStr = {"Maze 4", "Maze 5", "Maze 6", "Maze 7", "Maze 8", "Maze 9"};
    final String[] mazeRefs = {"maze4.png", "maze5.png", "maze6.png", "maze7.png", "maze8.png", "maze9.png"};
    final String[] mazeOriginals = {"/original/maze4.png", "/original/maze5.png",
            "/original/maze6.png", "/original/maze7.png"};
    JComboBox<String> mazesCombo;

    Long time;

    //Calls the class
    public static void main(String[] args) {
        new StartFrame();
    }

    //Calls the run method
    public StartFrame() {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    //Arranges the layout of the frame with respective buttons to pick new quit or load.
    @SuppressWarnings("methodlength")
    private void idkInsertName(Container pane) {

        int w = 20;
        int h = 20;

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        ImageIcon load = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/load.png")));
        load = new ImageIcon(getScaledImage(load.getImage(), w, h));
        ImageIcon newGame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/new.png")));
        newGame = new ImageIcon(getScaledImage(newGame.getImage(), w, h));
        ImageIcon quit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/quit.png")));
        quit = new ImageIcon(getScaledImage(quit.getImage(), w, h));
        ImageIcon edit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/edit.png")));
        edit = new ImageIcon(getScaledImage(edit.getImage(), w, h));

        JPanel panel = new JPanel();

        b1 = new JButton("Load Game", load);
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setActionCommand("load");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JsonReader reader = new JsonReader(data);
        try {
            reader.read();//Checks if there is a save state to read from.
            b1.setEnabled(true);
        } catch (IOException e) {
            b1.setEnabled(false);
        }


        b2 = new JButton("New Game", newGame);
        b2.setFont(font);
        b2.setActionCommand("newGame");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);

        b3 = new JButton("Quit", quit);
        b3.setFont(font);
        b3.setActionCommand("quit");
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);

        b4 = new JButton("Change Mazes", edit);
        b4.setFont(font);
        b4.setActionCommand("edit");
        b4.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Listen for actions on buttons 1 - 3.
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        b1.setToolTipText("Load the previously saved game");
        b2.setToolTipText("Start a new game");
        b3.setToolTipText("Quit the program");
        b4.setToolTipText("Change the mazes to play different mazes");

        //Add Components to this container, using the default BoxLayout.
        panel.add(b1);
        panel.add(b2);
        panel.add(b4);
        panel.add(b3);

        panel.setPreferredSize(new Dimension(200, 170));
        panel.setMaximumSize(new Dimension(200, 170));
        panel.setBorder(BorderFactory.createTitledBorder("Main Menu"));

        pane.add(panel);
    }

    //Requires: input is an image or ImageIcon, width and height
    //EFFECTS: returns a scaled down images to given width and height
    private static Image getScaledImage(Image src, int w, int h) {
        BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

    //Requires: input is an image or ImageIcon, width and height
    //EFFECTS: returns a scaled down images to given width and height
    private static Image getScaledImage(Image src) {
        double maxWidth = 200.0;
        double width = ((BufferedImage) src).getWidth();
        double height = ((BufferedImage) src).getHeight();
        int divisor = (int) Math.ceil(width / maxWidth);
        int w = (int) width / divisor;
        int h = (int) height / divisor;
        BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

    //EFFECTS: deals with button clicks, quit if quit, else load new or old state then pass information forward to
    // PickingFrame.
    @SuppressWarnings("methodlength")
    public void actionPerformed(ActionEvent e) {
        boolean move = false;
        if (e.getActionCommand().equals("load")) {
            loadState();
            move = true;

        } else if (e.getActionCommand().equals("newGame")) {
            defaultInitialize();
            move = true;

        } else if (e.getActionCommand().equals("edit")) {
            frame.setEnabled(false);
            frame.setVisible(false);
            new StartFrame(3);

        } else if (e.getActionCommand().equals("quit")) {
            System.exit(0);

        } else if (e.getActionCommand().equals("open")) {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + ".\n");
                EventLog.getInstance().logEvent(new Event("Opening: " + file.getName() + "."));
                saveButton.setEnabled(true);
                try {
                    img = ImageIO.read(file);
                    img = (BufferedImage) getScaledImage(img);
                    System.out.println("made it an img");
                    picImg.setIcon(new ImageIcon(img));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                log.append("Open command cancelled by user.\n");
                EventLog.getInstance().logEvent(new Event("Open command cancelled by user."));
            }
            log.setCaretPosition(log.getDocument().getLength());

        } else if (e.getActionCommand().equals("replace")) {
            String replace = (String) mazesCombo.getSelectedItem();
            int index = java.util.Arrays.asList(mazesStr).indexOf(replace);
            try {
                URI root = Objects.requireNonNull(getClass().getClassLoader().getResource("")).toURI();
                File destination = new File(loc(root), mazeRefs[index]);
                Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | URISyntaxException ex) {
                System.out.println(ex.getMessage());
            }
            img = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
            picImg.setIcon(new ImageIcon(img));
            saveButton.setEnabled(false);
            log.append(replace + " replaced with user image.\n");
            log.setCaretPosition(log.getDocument().getLength());
            EventLog.getInstance().logEvent(new Event(replace + " replaced with user image."));

        } else if (e.getActionCommand().equals("empty")) {
            String replace = (String) mazesCombo.getSelectedItem();
            int index = java.util.Arrays.asList(mazesStr).indexOf(replace);
            try {
                URI root = Objects.requireNonNull(getClass().getClassLoader().getResource("")).toURI();
                File destination = new File(loc(root), mazeRefs[index]);
                File blank = loc(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/blank.png")).toURI());
                Files.copy(blank.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | URISyntaxException ex) {
                System.out.println(ex.getMessage());
            }
            log.append(replace + " set to Blank Maze.\n");
            log.setCaretPosition(log.getDocument().getLength());
            EventLog.getInstance().logEvent(new Event(replace + " set to Blank Maze."));

        } else if (e.getActionCommand().equals("original")) {
            String replace = (String) mazesCombo.getSelectedItem();
            int index = java.util.Arrays.asList(mazesStr).indexOf(replace);
            try {
                URI root = Objects.requireNonNull(getClass().getClassLoader().getResource("")).toURI();
                File destination = new File(loc(root), mazeRefs[index]);
                File original = new File(loc(root), mazeOriginals[index]);
                Files.copy(original.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | URISyntaxException ex) {
                System.out.println(ex.getMessage());
            }
            log.append(replace + " set to Original " + replace + ".\n");
            log.setCaretPosition(log.getDocument().getLength());
            EventLog.getInstance().logEvent(new Event(replace + " set to Original " + replace + "."));

        }
        if (move) {
            frame.setEnabled(false);
            frame.setVisible(false);
            if (time != null) {
                new PickingFrame(mazes, arrangement, time);
            } else {
                new PickingFrame(mazes, arrangement);
            }
        }
    }

    private File loc(URI root) {
        return Paths.get(root).toFile();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    //EFFECTS: calls method to arrange the JFrame
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        idkInsertName(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: loads saveState from file to mazes.
    private void loadState() {
        try {
            jsonReader = new JsonReader(data);
            Map<String, Object> storedData = jsonReader.read();
            mazes = new Mazes(storedData);
            arrangement = mazes.getArrangement();
            time = Long.valueOf((Integer) storedData.get("time"));
            System.out.println("Loaded from " + data);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + data);
            System.exit(0);
        }
    }

    //MODIFIES: this
    //EFFECTS: starts a mazes instance and stores it in a list, a new game
    private static void defaultInitialize() {
        int temp;
        for (int i = 0; i < 9; i++) {
            do {
                temp = rand.nextInt(9);
            } while (arrangement.contains(temp));
            arrangement.add(temp);
        }
        mazes = new Mazes(arrangement);
        initializePlayers();
    }

    //MODIFIES: this.mazes.mazes().player
    //EFFECTS: goes into each Maze in mazes.mazes and initializes player in each.
    private static void initializePlayers() {
        for (int i = 0; i < arrangement.size(); i++) {
            mazes.initializePlayer(i);
        }
    }

    @SuppressWarnings("methodlength")
    public StartFrame(int temp) {

        frame = new JFrame("Changing Mazes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        GridLayout layout = new GridLayout(0,3);
        container.setLayout(layout);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        int w = 20;
        int h = 20;
        int ph = 160;
        Dimension buDimension = new Dimension(180, 30);

        ImageIcon newGame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/new.png")));
        newGame = new ImageIcon(getScaledImage(newGame.getImage(), w, h));
        ImageIcon open = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/open.png")));
        open = new ImageIcon(getScaledImage(open.getImage(), w, h));
        ImageIcon quit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/quit.png")));
        quit = new ImageIcon(getScaledImage(quit.getImage(), w, h));

        b1 = new JButton("Continue to Game", newGame);
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setPreferredSize(buDimension);
        b1.setActionCommand("newGame");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        b1.setToolTipText("Start Game with mazes with changes made.");

        b3 = new JButton("Quit", quit);
        b3.setFont(font);
        b3.setPreferredSize(buDimension);
        b3.setActionCommand("quit");
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);
        b3.setToolTipText("Quit the program");


        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new ImageFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.changeToParentDirectory();

        img = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);

        picImg = new JLabel(new ImageIcon(img));

        openButton = new JButton("Select Maze Image", open);
        openButton.setFont(font);
        openButton.setPreferredSize(buDimension);
        openButton.setActionCommand("open");
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openButton.setToolTipText("<html><pre>Select Image file to replace a maze with.\n"
                + "For it to be recognized properly, the walls and paths in the image must be in a perfect grid"
                + " pattern.\n"
                + "Recommended website: https://keesiemeijer.github.io/maze-generator/#generate\n"
                + "For predefined start & end points, the points should be indicated by cyan (RGB: 000 255 255)"
                + " (HEX: 00FFFF).\n"
                + "Otherwise if the maze is surrounded by black walls with two spaced for the start & end, they"
                + " will be picked.\n"
                + "Otherwise, the program will assign the start point to the first true path from top left and a"
                + " possible random end point.\n"
                + "In the event the maze is not from the above link, there is no guarantee that it will be recognized"
                + " properly.</pre></html>");

        javax.swing.ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        saveButton = new JButton("Replace Selected Maze");
        saveButton.setFont(font);
        saveButton.setPreferredSize(buDimension);
        saveButton.setActionCommand("replace");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setEnabled(false);
        saveButton.setToolTipText("Replaces the original maze in the selection above with the input maze image.");

        b2 = new JButton("Set Blank");
        b2.setFont(font);
        b2.setPreferredSize(buDimension);
        b2.setActionCommand("empty");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        b2.setToolTipText("Replaces the maze in the selection with a quick to solve maze.");

        b4 = new JButton("Set Original");
        b4.setFont(font);
        b4.setPreferredSize(buDimension);
        b4.setActionCommand("original");
        b4.setAlignmentX(Component.CENTER_ALIGNMENT);
        b4.setToolTipText("Replaces the maze in the selection with it's original version as coded by the programmer.");

        JLabel text = new JLabel("<html><pre>Maze to edit: \t </pre></html>");

        b1.addActionListener(this);
        b3.addActionListener(this);
        openButton.addActionListener(this);
        saveButton.addActionListener(this);
        b2.addActionListener(this);
        b4.addActionListener(this);

        mazesCombo = new JComboBox<>(mazesStr);
        mazesCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mazesCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = (String) mazesCombo.getSelectedItem();
                b4.setEnabled(!"Maze 8".equals(temp) && !"Maze 9".equals(temp));
            }
        });

        panel1.add(openButton);
        panel1.add(picImg);
        panel1.setPreferredSize(new Dimension(170, ph));

        panel2.add(text);
        panel2.add(mazesCombo);
        panel2.add(saveButton);
        panel2.add(b2);
        panel2.add(b4);
        panel2.setPreferredSize(new Dimension(170, ph));

        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.add(logScrollPane);
        panel3.add(b1);
        panel3.add(Box.createVerticalStrut(10));
        panel3.add(b3);
        panel3.setPreferredSize(new Dimension(250, ph));


        container.add(panel1,0);
        container.add(panel2,1);
        container.add(panel3,2);


        frame.pack();
        frame.setVisible(true);
    }

}