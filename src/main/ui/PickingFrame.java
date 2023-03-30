package ui;

import model.Mazes;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PickingFrame extends JFrame implements ActionListener {

    private static JsonWriter jsonWriter;
    private static final String data = "./data/saveState.json";

    static Mazes mazes;
    static ArrayList<Integer> arrange = new ArrayList<Integer>();

    JFrame frame;
    private JComboBox inputsCombo;

    private static final int VGAP = 15;
    private String[] inputs = {"Maze 1", "Maze 2", "Maze 3"};

    public PickingFrame(Mazes mazes, ArrayList<Integer> arrange) {
        this.mazes = mazes;
        this.arrange = arrange;
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
        panel.setPreferredSize(new Dimension(200, 130));
        panel.setMaximumSize(new Dimension(200, 130));

        ImageIcon quit = new ImageIcon(System.getProperty("user.dir") + "/images/quit.png");
        quit = new ImageIcon(getScaledImage(quit.getImage(), 20, 20));

        inputsCombo = new JComboBox(inputs);
//        inputsCombo.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String choice = (String) inputsCombo.getSelectedItem();
//                if (e.getActionCommand() == "continue") {
//                    System.out.println(choice);
//                } else if (e.getActionCommand() == "quit") {
//                    System.exit(0);
//                }
//            }
//        });


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
        panel.add(Box.createVerticalStrut(VGAP));
        panel.add(b1);
        panel.add(Box.createVerticalStrut(VGAP));
        panel.add(b2);

        blank.add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "continue") {
            System.out.println(inputsCombo.getSelectedItem());
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

}
