package ui;

import model.Mazes;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class StartFrame extends JPanel implements ActionListener {
    protected static JButton b1;
    protected static JButton b2;
    protected static JButton b3;

    static Random rand = new Random();
    static Mazes mazes;
    static ArrayList<Integer> arrangement = new ArrayList<Integer>();

    private static final String data = "./data/saveState.json";
    private static JsonReader jsonReader;

    JFrame frame;

    public static void main(String[] args) {
        new StartFrame();
    }

    public StartFrame() {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @SuppressWarnings("methodlength")
    private void idkInsertName(Container pane) {

        int w = 20;
        int h = 20;

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        ImageIcon load = new ImageIcon(System.getProperty("user.dir") + "/images/load.png");
        load = new ImageIcon(getScaledImage(load.getImage(), w, h));
        ImageIcon newGame = new ImageIcon(System.getProperty("user.dir") + "/images/new.png");
        newGame = new ImageIcon(getScaledImage(newGame.getImage(), w, h));
        ImageIcon quit = new ImageIcon(System.getProperty("user.dir") + "/images/quit.png");
        quit = new ImageIcon(getScaledImage(quit.getImage(), w, h));

        JPanel panel = new JPanel();

        b1 = new JButton("Load Game", load);
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
//        b1.setMnemonic(KeyEvent.VK_D);
//        b1.setActionCommand("disable");
        b1.setActionCommand("load");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JsonReader reader = new JsonReader(data);
        try {
            Map<String, Object> data = reader.read();
            b1.setEnabled(true);
        } catch (IOException e) {
            b1.setEnabled(false);
        }


        b2 = new JButton("New Game", newGame);
        b2.setFont(font);
//        b2.setForeground(new Color(0xffffdd));
//        b2.setMnemonic(KeyEvent.VK_M);
        b2.setActionCommand("newGame");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);

        b3 = new JButton("Quit", quit);
        b3.setFont(font);
//        b3.setMnemonic(KeyEvent.VK_E);
        b3.setActionCommand("quit");
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Listen for actions on buttons 1 - 3.
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);

        b1.setToolTipText("Load the previously saved game");
        b2.setToolTipText("Start a new game");
        b3.setToolTipText("Quit the program");

        //Add Components to this container, using the default FlowLayout.

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);

        panel.setPreferredSize(new Dimension(200, 130));
        panel.setMaximumSize(new Dimension(200, 130));
        panel.setBorder(BorderFactory.createTitledBorder("Main Menu"));

        pane.add(panel);

    }

    private static Image getScaledImage(Image src, int w, int h) {
        BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "load") {
            loadState();
        } else if (e.getActionCommand() == "newGame") {
            defaultInitialize();
        } else if (e.getActionCommand() == "quit") {
            System.exit(0);
        }

        new PickingFrame(mazes, arrangement);
//        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("ButtonHtmlDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        idkInsertName(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: loads saveState from file to mazes.
    private static void loadState() {
        try {
            jsonReader = new JsonReader(data);
            Map<String, Object> storedData = jsonReader.read();
            mazes = new Mazes(storedData);
            arrangement = mazes.getArrangement();
            System.out.println("Loaded from " + data);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + data);
            System.exit(0);
        }
    }

    private static void defaultInitialize() {
        int temp;
        for (int i = 0; i < 6; i++) {
            do {
                temp = rand.nextInt(6);
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

}