package ui;

import model.InvalidInputException;
import model.Mazes;
import model.Player;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class PickingFrame extends JPanel implements ActionListener {

    private static JsonWriter jsonWriter;
    private static final String data = "./data/saveState.json";

    static Mazes mazes;
    static ArrayList<Integer> arrange = new ArrayList<Integer>();
    static Color color;
    static Player player;
    static int startX;
    static int startY;
    static int endX;
    static int endY;

    private int index;

    JFrame frame;
    private JComboBox inputsCombo;
    private JComboBox colorCombo;

    private static final int VGAP = 15;
    private String[] inputs = {"Maze 1", "Maze 2", "Maze 3"};
    protected int scale = 30;

    private String[] colors = {"black", "blue", "cyan", "gray", "pink", "yellow", "magenta"};

    public PickingFrame(Mazes mazes, ArrayList<Integer> arrange) {
        this.mazes = mazes;
        this.arrange = arrange;
        jsonWriter = new JsonWriter(data);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("New Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        running(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private void running(Container blank) {
        do {
            makingFrame(blank);
        } while (!mazes.checkAllSolved());
    }

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

        inputsCombo = new JComboBox(inputs);

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
        panel.add(Box.createHorizontalStrut(VGAP));
        panel.add(b1);
        panel.add(Box.createHorizontalStrut(VGAP));
        panel.add(b2);

        blank.add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        String selection = inputsCombo.getSelectedItem().toString();
        if (e.getActionCommand() == "continue") {
            if (selection == "Maze 1") {
                if (!mazes.checkSolved(arrange.indexOf(0))) {
                    goToMazes(0);
                }
            } else if (selection == "Maze 2") {
                if (!mazes.checkSolved(arrange.indexOf(1))) {
                    goToMazes(1);
                }
            } else if (selection == "Maze 3") {
                if (!mazes.checkSolved(arrange.indexOf(2))) {
                    goToMazes(2);
                }
            }
        } else if (e.getActionCommand() == "quit") {
            System.exit(0);
        } else if (e.getActionCommand() == "save") {
            saveState();
        }
        frame.setVisible(true);
    }

    // EFFECTS: saves the current state of mazes to file
    private static void saveState() {
        try {
            jsonWriter.open();
            jsonWriter.write(mazes);
            jsonWriter.close();
            System.out.println("Saved to " + data);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + data);
        }
    }

    private static Image getScaledImage(Image src, int w, int h) {
        BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return result;
    }

    @SuppressWarnings("methodlength")
    public void goToMazes(int inp) {
        try {
            Field field = Class.forName("java.awt.Color").getField(mazes.getColor());
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = null;
        }

        index = arrange.indexOf(inp);
        String[][] maze = mazes.getMaze(index);
        Container container = frame.getContentPane();
        initializeRequired(index);

        container.removeAll();
        container.repaint();

        colorCombo = new JComboBox(colors);
        colorCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = (String) colorCombo.getSelectedItem();
                try {
                    Field field = Class.forName("java.awt.Color").getField(temp);
                    color = (Color)field.get(null);
                    mazes.setColor(temp);
                } catch (Exception ex) {
                    color = null;
                }
            }
        });

        JButton b1 = new JButton("Save Game");
        Font font = b1.getFont().deriveFont(Font.PLAIN);
        b1.setFont(font);
        b1.setActionCommand("save");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon quit = new ImageIcon(System.getProperty("user.dir") + "/images/quit.png");
        quit = new ImageIcon(getScaledImage(quit.getImage(), 20, 20));
        JButton b2 = new JButton("Quit", quit);
        b2.setActionCommand("quit");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);

        b1.addActionListener(this);
        b2.addActionListener(this);

        frame.add(colorCombo);
        frame.add(b1);
        frame.add(b2);
        frame.add(new Gra(maze));

        KeyHandler keyListen = new KeyHandler();

        frame.addKeyListener(keyListen);

        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.requestFocus();

    }

    private void initializeRequired(int inp) {
        Map<String, Object> temp = mazes.getReq(inp);
        player = (Player) temp.get("player");
        startX = (int) temp.get("startX");
        startY = (int) temp.get("startY");
        endX = (int) temp.get("endX");
        endY = (int) temp.get("endY");
    }

    @SuppressWarnings("methodlength")
    private void move(int code) {
        if (code == KeyEvent.VK_RIGHT) {
            try {
                mazes.possibleMove(index, "right");
                mazes.applyMove(index,"right");
            } catch (InvalidInputException e) {
                e.getMessage();
            }
        } else if (code == KeyEvent.VK_LEFT) {
            try {
                mazes.possibleMove(index, "left");
                mazes.applyMove(index,"left");
            } catch (InvalidInputException e) {
                e.getMessage();
            }
        } else if (code == KeyEvent.VK_UP) {
            try {
                mazes.possibleMove(index, "up");
                mazes.applyMove(index,"up");
            } catch (InvalidInputException e) {
                e.getMessage();
            }
        } else if (code == KeyEvent.VK_DOWN) {
            try {
                mazes.possibleMove(index, "down");
                mazes.applyMove(index,"down");
            } catch (InvalidInputException e) {
                e.getMessage();
            }
        }
        frame.repaint();
        boolean temp = mazes.solved(index);
        if (!temp) {
            new PickingFrame(mazes, arrange);
        }
//        else {
//            frame.getContentPane().removeAll();
//            JLabel label = new JLabel("Congratulations! All mazes complete :D");
//            label.setFont(new Font("Serif", Font.PLAIN, 15));
//        }
    }

    private class Gra extends JComponent {
        private static final long serialVersionUID = 1L;
        String[][] maze;

        Gra(String[][] inp) {
            maze = inp;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
                    if (j == player.getX() && i == player.getY()) {
                        g.setColor(Color.red);
                    } else if (j == startX && i == startY) {
                        g.setColor(Color.orange);
                    } else if (j == endX && i == endY) {
                        g.setColor(Color.green);
                    } else if (maze[i][j] == "F") {
                        g.setColor(color);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(j * scale, i * scale, scale, scale);
                }
            }
            System.out.println("Point A");
        }
    }

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

}
