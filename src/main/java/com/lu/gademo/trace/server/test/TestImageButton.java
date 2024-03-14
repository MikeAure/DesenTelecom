package com.lu.gademo.trace.server.test;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TestImageButton extends JFrame {

    private final JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestImageButton frame = new TestImageButton();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     *
     * @throws IOException
     */
    public TestImageButton() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        cropImageAndSetButton("img/output.png", 8, contentPane);
    }

    public void cropImageAndSetButton(String url, int x, JPanel panel) {// crop image to x * x
        File file = new File(url);
        try {
            BufferedImage image = ImageIO.read(file);// the image must be square.
            int sideLen = image.getWidth() - image.getWidth() % x;
            int subSideLen = sideLen / x;
            /**
             * @ignore x→ y↓ 00 10 20 30 01 11 21 31
             *
             */
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < x; j++) {
                    BufferedImage subImg = image.getSubimage(j * subSideLen, i * subSideLen, subSideLen, subSideLen);
                    File output = new File("img/" + j + i + ".png");
                    ImageIO.write(subImg, "png", output);
                    MyButton btn = new MyButton("img/" + j + i + ".png");
                    btn.setLocation(j * subSideLen, i * subSideLen);
                    final String bgUrl = "img/" + j + i + ".png";
                    btn.setMouseListener(new MouseListener() {

                        boolean flag = false;
                        boolean isMouseExited = false;

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            // TODO Auto-generated method stub
                            Timer timer = new Timer();
                            isMouseExited = false;
                            if (!flag) {
                                flag = true;
                                try {
                                    btn.toReplaceBackground("img/lpld.png");
                                    btn.setBorderPainted(true);
                                    btn.setLblContent("起始经度：26.234234", "起始纬度：123.234234", "终止经度：26.234234",
                                            "终止纬度：123.234234", "用户数：12");
                                    btn.add(btn.lblStartX);
                                    btn.add(btn.lblStartY);
                                    btn.add(btn.lblEndX);
                                    btn.add(btn.lblEndY);
                                    btn.add(btn.lblUserCount);
                                    btn.lblStartX.setBounds(0, 0, 80, 10);
                                    btn.lblStartY.setBounds(0, 0, 80, 10);
                                    btn.lblEndX.setBounds(0, 0, 80, 10);
                                    btn.lblEndY.setBounds(0, 0, 80, 10);
                                    btn.lblUserCount.setBounds(0, 0, 80, 10);
//									btn.lbl.repaint();
                                    btn.repaint();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                new Thread(() -> {
                                    timer.scheduleAtFixedRate(new TimerTask() {

                                        @Override
                                        public void run() {
                                            btn.setSize(btn.getWidth() + 2, btn.getHeight() + 2);
                                            btn.lblStartX.setSize(btn.lblStartX.getWidth() + 2,
                                                    btn.lblStartX.getHeight());
                                            btn.lblStartY.setBounds(btn.lblStartY.getX(), btn.lblStartY.getY() + 1,
                                                    btn.lblStartX.getWidth() + 2, btn.lblStartY.getHeight());
                                            btn.lblEndX.setBounds(btn.lblEndX.getX(), btn.lblEndX.getY() + 2,
                                                    btn.lblEndX.getWidth() + 2, btn.lblEndX.getHeight());
                                            btn.lblEndY.setBounds(btn.lblEndY.getX(), btn.lblEndY.getY() + 3,
                                                    btn.lblEndY.getWidth() + 2, btn.lblEndY.getHeight());
                                            btn.lblUserCount.setBounds(btn.lblUserCount.getX(),
                                                    btn.lblUserCount.getY() + 4, btn.lblUserCount.getWidth() + 2,
                                                    btn.lblUserCount.getHeight());
                                            if (btn.getWidth() - subSideLen > 50 || isMouseExited) {
                                                cancel();
                                                System.out.println(
                                                        btn.lblStartX.getWidth() + "," + btn.lblStartY.getWidth());
                                                flag = false;
                                            }
                                        }

                                    }, 0, 5);
                                }).start();

                            }

                            System.out.println(btn.getX() + ",Entered:" + e.getX() + "," + e.getY());
//							btn.setSize(btn.getWidth() + 50, btn.getHeight() + 50);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            isMouseExited = true;
                            flag = false;
                            btn.setSize(subSideLen, subSideLen);
                            btn.setBorderPainted(false);
                            btn.remove(btn.lblStartX);
                            btn.remove(btn.lblStartY);
                            btn.remove(btn.lblEndX);
                            btn.remove(btn.lblEndY);
                            btn.remove(btn.lblUserCount);
                            try {
                                btn.toReplaceBackground(bgUrl);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            System.out.println(btn.getX() + ",Exit:" + e.getX() + "," + e.getY());
                        }


                    });
                    panel.add(btn);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class MyButton extends JButton {

        BufferedImage img;
        JLabel lblStartX;
        JLabel lblStartY;
        JLabel lblEndX;
        JLabel lblEndY;
        JLabel lblUserCount;

        public MyButton(String srcUrl) throws IOException {
            super();
            initLabels();
            img = ImageIO.read(new File(srcUrl));
            setSize(img.getWidth(), img.getHeight());
            setRolloverEnabled(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setLayout(null);
        }

        public void initLabels() {
            lblStartX = new JLabel();
            lblStartY = new JLabel();
            lblEndX = new JLabel();
            lblEndY = new JLabel();
            lblUserCount = new JLabel();
            lblStartX.setForeground(Color.BLUE);
            lblStartX.setFont(new Font("微软雅黑", Font.BOLD, 8));
            lblStartY.setForeground(Color.BLUE);
            lblStartY.setFont(new Font("微软雅黑", Font.BOLD, 8));
            lblEndX.setForeground(Color.BLUE);
            lblEndX.setFont(new Font("微软雅黑", Font.BOLD, 8));
            lblEndY.setForeground(Color.BLUE);
            lblEndY.setFont(new Font("微软雅黑", Font.BOLD, 8));
            lblUserCount.setForeground(Color.BLUE);
            lblUserCount.setFont(new Font("微软雅黑", Font.BOLD, 8));
//			lblStartX.set
        }

        public void setLblContent(String startX, String startY, String endX, String endY, String userCount) {
            lblStartX.setText(startX);
            lblStartY.setText(startY);
            lblEndX.setText(endX);
            lblEndY.setText(endY);
            lblUserCount.setText(userCount);
        }

        public void updateUserCount(String count) {
            lblUserCount.setText(count);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        public void toReplaceBackground(String url) throws IOException {
            img = ImageIO.read(new File(url));
            repaint();
        }

        public void setMouseListener(MouseListener l) {
            this.addMouseListener(l);
        }

    }
}
