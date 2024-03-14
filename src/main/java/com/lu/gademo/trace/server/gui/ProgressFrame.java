package com.lu.gademo.trace.server.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProgressFrame extends JFrame {

    private final JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProgressFrame frame = new ProgressFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ProgressFrame() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = dimension.width;
        int height = dimension.height;
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds((width - 90) / 2, (height - 60) / 2, 90, 60);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        ImageIcon icon = new ImageIcon("img/progressImg.gif");
        // JButton btn = new JButton(new
        // ImageIcon(icon.getImage().getScaledInstance(300, 225, Image.SCALE_FAST)));
        JButton btn = new JButton(icon);
        btn.setBorderPainted(false);
        contentPane.add(btn, BorderLayout.CENTER);
    }

}
