package com.lu.gademo.trace.server.test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TestProgressImg extends JFrame {

    private final JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestProgressImg frame = new TestProgressImg();
                    frame.setVisible(true);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            frame.dispose();
                        }

                    }, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TestProgressImg() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 90, 60);
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
