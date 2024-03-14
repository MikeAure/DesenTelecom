package com.lu.gademo.trace.server.gui;

import com.lu.gademo.trace.model.TraceUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TraceServer extends JFrame {

    private final JPanel contentPane;
    private final JLabel lblRcCount;
    private final JLabel lblRvCount;
    private final JLabel lblTime;
    private final JLabel lblDate;
    private final JLabel lblIpPort;
    private final JSeparator rvSeparator;
    private final JPanel rvCanvas;
    private final JTextArea logArea;
    private final JScrollPane scrollPane;
    private final JSeparator rcSeparator;
    private final JPanel rcCanvas;
    private final JPanel rcPanel;
    private final JPanel rvPanel;
    private final JLabel rcDistbLbl;
    private final JLabel rvDistbLbl;
    private final JPanel logPanel;
    private final HoverEventReceiver fatherReceiver;
    public HashMap<TraceUser, String> userLocation;

    /**
     * Create the frame.
     */
    public TraceServer(ProgressFrame progressFrame, int port) throws UnknownHostException {
        setName("Trace Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen dimension: " + dimension);
        int width = dimension.width;
        int height = dimension.height;
        setBounds((width - 450) / 2, (height - 300) / 2, 450, 300);

//		setVisible(true);
        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setName("Trace Server");
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        userLocation = new HashMap<TraceUser, String>();

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(Color.GRAY);
        tabbedPane.setBounds(0, 0, 319, 271);

        rcPanel = new JPanel();
        rcPanel.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab("  RC  ", null, rcPanel, null);
        rcPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Online:");
        lblNewLabel.setBounds(0, 10, 54, 15);
        rcPanel.add(lblNewLabel);

        lblRcCount = new JLabel("0");
        lblRcCount.setBounds(74, 10, 255, 15);
        rcPanel.add(lblRcCount);

        rcSeparator = new JSeparator();
        rcSeparator.setBounds(0, 31, 421, 2);
        rcPanel.add(rcSeparator);

        rcCanvas = new JPanel();
        rcCanvas.setName("rc");

        fatherReceiver = new HoverEventReceiver() {
            @Override
            public void showMeName(MyButton btn, String Name, JPanel panel) {
                ArrayList<String> subareaInfo = findCoor(Name);
                try {
                    btn.toReplaceBackground("img/" + Name + "sub.png");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                btn.setLblContent("起始经度：" + subareaInfo.get(0), "起始纬度：" + subareaInfo.get(1),
                        "终止经度：" + subareaInfo.get(2), "终止纬度：" + subareaInfo.get(3), "用户数：12");
                System.err.print("起始经度：" + subareaInfo.get(0) + "起始纬度：" + subareaInfo.get(1) + "终止经度："
                        + subareaInfo.get(2) + "终止纬度：" + subareaInfo.get(3));
                if (panel.getName().equals("rc")) {
                    btn.updateUserCount("消费者数：" + traverseForAmount(0, Name));
                } else if (panel.getName().equals("rv")) {
                    btn.updateUserCount("司机数：" + traverseForAmount(1, Name));
                }
            }
        };
        rcCanvas.setBounds(10, 56, 304, 158);
        rcPanel.add(rcCanvas);

        rcCanvas.setLayout(null);
        cropImageAndSetButtons("img/output.png", 8, rcCanvas);

        rcDistbLbl = new JLabel("Distribution:");
        rcDistbLbl.setBounds(0, 35, 409, 15);
        rcPanel.add(rcDistbLbl);

        rvPanel = new JPanel();
        rvPanel.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab("  RV  ", null, rvPanel, null);
        rvPanel.setLayout(null);

        JLabel lblOnline = new JLabel("Online:");
        lblOnline.setBounds(0, 10, 54, 15);
        rvPanel.add(lblOnline);

        lblRvCount = new JLabel("0");
        lblRvCount.setBounds(74, 10, 255, 15);
        rvPanel.add(lblRvCount);

        rvSeparator = new JSeparator();
        rvSeparator.setBounds(0, 31, 0, 2);
        rvPanel.add(rvSeparator);

        rvCanvas = new JPanel();
        rvCanvas.setName("rv");
        rvCanvas.setBounds(0, 51, 329, 171);
        rvPanel.add(rvCanvas);

        rvCanvas.setLayout(null);
        cropImageAndSetButtons("img/output.png", 8, rvCanvas);

        rvDistbLbl = new JLabel("Distribution:");
        rvDistbLbl.setBounds(0, 35, 419, 15);
        rvPanel.add(rvDistbLbl);

        logPanel = new JPanel();
        logPanel.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab(" Log ", null, logPanel, null);
        logPanel.setLayout(null);

        logArea = new JTextArea();
        logArea.setLineWrap(true);
        logArea.setEditable(false);

        scrollPane = new JScrollPane(logArea);
        scrollPane.setBackground(Color.LIGHT_GRAY);
        scrollPane.setBounds(0, 0, 0, 0);
        logPanel.add(scrollPane);

        tabbedPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("resized!");

                rcSeparator.setBounds(rcSeparator.getX(), rcSeparator.getY(), rcPanel.getWidth(),
                        rcSeparator.getHeight());
                rvSeparator.setBounds(rcSeparator.getBounds());

                System.out.println("before:" + rcPanel.getHeight());
                rcCanvas.setBounds(rcCanvas.getX(), rcCanvas.getY(), rcPanel.getWidth() - 20, rcPanel.getWidth() - 20);
                rvCanvas.setBounds(rcCanvas.getBounds());
                setBounds(getX(), getY(), getWidth(),
                        50 + rcPanel.getY() + rcCanvas.getY() + rcCanvas.getHeight() + 10);
                System.out.println("rcPanel.y: " + rcPanel.getY() + "\nrcCanvas.y: " + rcCanvas.getY());

                scrollPane.setBounds(0, 0, logPanel.getWidth(), logPanel.getHeight());
                System.out.println("\nafter:" + rcPanel.getHeight());
                System.err.print(rcCanvas.getWidth() + "," + rcCanvas.getHeight() + "\n");
            }
        });

        JButton btnNewButton = new JButton("Exit");

        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnNewButton.setName("Exit");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        JLabel lblNewLabel_4 = new JLabel("System Time:");
        lblNewLabel_4.setBackground(Color.WHITE);
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_4.setForeground(Color.WHITE);

        lblTime = new JLabel("12:34:56");
        lblTime.setForeground(Color.WHITE);
        lblTime.setHorizontalAlignment(SwingConstants.CENTER);

        lblDate = new JLabel("d/m/y");
        lblDate.setForeground(Color.WHITE);
        lblDate.setHorizontalAlignment(SwingConstants.CENTER);

        lblIpPort = new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        lblIpPort.setForeground(Color.WHITE);
        lblIpPort.setHorizontalAlignment(SwingConstants.CENTER);


        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(lblDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblTime, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblIpPort, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblNewLabel_4, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                                .addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                        .addContainerGap()));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup().addGap(88).addComponent(lblNewLabel_4)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblTime)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblDate)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblIpPort)
                        .addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE).addComponent(btnNewButton)
                        .addContainerGap())
                .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE));
        contentPane.setLayout(gl_contentPane);
        updateTime();
        progressFrame.dispose();
        setVisible(true);
    }

    public String traverseForAmount(int type, String name) {
        int amount = 0;
        for (TraceUser user : userLocation.keySet()) {
            if (userLocation.get(user).equals(name)) {
                if (type == 0 && user.getRoleId() == 1)
                    amount++;
                if (type == 1 && user.getRoleId() == 2)
                    amount++;
            }
        }
        return amount + "";
    }

    public void appendLog(StringBuffer logText) {
        long systemMillis = System.currentTimeMillis();
        SimpleDateFormat df_logTime = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss ");
        logArea.append(df_logTime.format(systemMillis) + logText.toString() + "\n\n");
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    public void setOnlineRc(int number) {
        this.lblRcCount.setText(number + "");
    }

    public void setOnlineRv(int number) {
        this.lblRvCount.setText(number + "");
    }

    public void updateTime() {
        new javax.swing.Timer(200, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                long systemMillis = System.currentTimeMillis();
                SimpleDateFormat df_time = new SimpleDateFormat("hh:mm:ss");
                lblTime.setText(df_time.format(systemMillis));
                SimpleDateFormat df_date = new SimpleDateFormat("yyyy/MM/dd");
                lblDate.setText(df_date.format(systemMillis));
            }

        }).start();
    }

    public ArrayList<String> findCoor(String name) {
        double lgtdStart = 108.77, lattdStart = 34.43;
        double lgtdEnd = 109.12, lattdEnd = 34.14;
        ArrayList<String> coors = new ArrayList<String>();
        int x = Integer.parseInt(name.charAt(0) + "");
        int y = Integer.parseInt(name.charAt(1) + "");
        System.out.println("x,y:" + x + y);
        double subLgtdStart = lgtdStart + ((lgtdEnd - lgtdStart) / 8) * x;
        double subLgtdEnd = lgtdStart + ((lgtdEnd - lgtdStart) / 8) * (x + 1);
        double subLattdStart = lattdStart + ((lattdEnd - lattdStart) / 8) * y;
        double subLattdEnd = lattdStart + ((lattdEnd - lattdStart) / 8) * (y + 1);
        coors.add(subLgtdStart + "");
        coors.add(subLattdStart + "");
        coors.add(subLgtdEnd + "");
        coors.add(subLattdEnd + "");
        return coors;
    }

    public void cropImageAndSetButtons(String url, int x, JPanel panel) {// crop image to x * x
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
                    btn.setName("" + j + i);
                    btn.setLocation(j * subSideLen, i * subSideLen);
                    btn.setHoverEventReceiver(fatherReceiver);
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
                            btn.receiver.showMeName(btn, btn.getName(), panel);
                            if (!flag) {
                                flag = true;
                                // btn.toReplaceBackground("img/lpld.png");
                                // btn.setBorderPainted(true);
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
                                // btn.lbl.repaint();
                                btn.repaint();
                                new Thread(() -> {
                                    timer.scheduleAtFixedRate(new TimerTask() {

                                        @Override
                                        public void run() {
                                            btn.setSize(btn.getWidth() + 4, btn.getHeight() + 4);
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
                                            if (btn.getWidth() - subSideLen > 80 || isMouseExited) {
                                                cancel();
                                                System.out.println(
                                                        btn.lblStartX.getWidth() + "," + btn.lblStartY.getWidth());
                                                flag = false;
                                            }
                                        }

                                    }, 0, 5);
                                }).start();

                            }

//							System.out.println(btn.getX() + ",Entered:" + e.getX() + "," + e.getY());
//							btn.setSize(btn.getWidth() + 50, btn.getHeight() + 50);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            isMouseExited = true;
                            flag = false;
                            btn.setSize(subSideLen, subSideLen);
//							btn.setBorderPainted(false);
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
//							System.out.println(btn.getX() + ",Exit:" + e.getX() + "," + e.getY());
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
        HoverEventReceiver receiver;

        public MyButton(String srcUrl) throws IOException {
            super();
            initLabels();
            img = ImageIO.read(new File(srcUrl));
            setSize(img.getWidth(), img.getHeight());
            setRolloverEnabled(false);
//			setBorderPainted(false);
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

        public void setHoverEventReceiver(HoverEventReceiver receiver) {
            this.receiver = receiver;
        }

    }

    class HoverEventReceiver {
        public HoverEventReceiver() {

        }

        public void showMeName(MyButton btn, String name, JPanel panel) {
            // 初始化之后重写这个方法，依次设置背景，label
        }
    }

}
