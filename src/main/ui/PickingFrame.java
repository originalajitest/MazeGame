package ui;

import model.Mazes;
import model.Player;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class PickingFrame extends JPanel implements ActionListener {

    private static JsonWriter jsonWriter;
    private static final String data = "./data/saveState.json";

    static Mazes mazes;
    static ArrayList<Integer> arrange = new ArrayList<Integer>();
    static String color;
    static Player player;
    static int startX;
    static int startY;
    static int endX;
    static int endY;

    JFrame frame;
    private JComboBox inputsCombo;
    private Graphics gra;

    private static final int VGAP = 15;
    private String[] inputs = {"Maze 1", "Maze 2", "Maze 3"};
    private int scale = 30;

    public PickingFrame(Mazes mazes, ArrayList<Integer> arrange) {
        this.mazes = mazes;
        this.arrange = arrange;
        createAndShowGUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gra = g;
    }

    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("New Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        running(frame.getContentPane());

        //Display the window.
        frame.pack();
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
                goToMazes(0);
            } else if (selection == "Maze 2") {
                goToMazes(1);
            } else if (selection == "Maze 3") {
                goToMazes(2);
            }
        } else {
            System.exit(0);
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
        color = mazes.getColor();
        int index = arrange.get(inp);
        String[][] maze = mazes.getMaze(index);
        JPanel panel = new JPanel();
        JButton b1;
        JButton b2;
        Container container = frame.getContentPane();
        initializeRequired(index);

        container.removeAll();
        container.repaint();

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (j == player.getX() && i == player.getY()) {
                    gra.setColor(Color.red);
                    gra.fillRect(j * scale, i * scale, scale, scale);
                } else if (j == startX && i == startY) {
                    gra.setColor(Color.orange);
                    gra.fillRect(j * scale, i * scale, scale, scale);
                } else if (j == endX && i == endY) {
                    gra.setColor(Color.green);
                    gra.fillRect(j * scale, i * scale, scale, scale);
                } else if (maze[i][j] == "F") {
                    gra.setColor(Color.decode(color));
                    gra.fillRect(j * scale, i * scale, scale, scale);
//                }
//                gra.setColor(Color.decode(color));
//                gra.fillRect();
                }
            }
        }

    }

    private void initializeRequired(int inp) {
        Map<String, Object> temp = mazes.getReq(inp);
        player = (Player) temp.get("player");
        startX = (int) temp.get("startX");
        startY = (int) temp.get("startY");
        endX = (int) temp.get("endX");
        endY = (int) temp.get("endY");
    }





}
