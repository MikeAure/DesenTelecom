package com.lu.gademo.trace.server.test;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class TestTransparentTable extends JFrame {

    private final JPanel contentPane;
    private final JTable table;

    /**
     * Create the frame.
     */
    public TestTransparentTable() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("D:\\test1.jpg")), 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                }
            }
        };

        Object[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
        Object[][] contents = new Object[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                contents[i][j] = i;
            }
        }

        DefaultTableModel model = new DefaultTableModel(contents, columns);

        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = (getWidth() / 2 - (getWidth() / 2) % 8);
                panel.setBounds(getX(), getY(), width, width);
                System.out.println("width:" + getWidth() + "," + "height:" + getHeight());
                for (int i = 0; i < table.getColumnCount(); i++) {
                    table.getColumnModel().getColumn(i).setPreferredWidth(getWidth() / table.getColumnCount());
                    table.getColumnModel().getColumn(i).setMaxWidth(getWidth() / table.getColumnCount());
                    table.getColumnModel().getColumn(i).setMinWidth(getWidth() / table.getColumnCount());
                }
                table.setRowHeight(getWidth() / table.getColumnCount());
                System.out.println(table.getRowHeight() + "," + table.getColumnModel().getColumn(0).getWidth());
            }
        });

        // DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        MyCellRenderer renderer = new MyCellRenderer();
        renderer.setOpaque(false);


        panel.setOpaque(false);

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addGap(104).addComponent(panel, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)));
        gl_contentPane
                .setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
                        gl_contentPane.createSequentialGroup().addContainerGap()
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(457, Short.MAX_VALUE)));
        table = new JTable(model);
        panel.add(table);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setRowMargin(0);
        table.setIgnoreRepaint(true);
        table.setAutoscrolls(false);
        table.setBorder(null);
        table.setPreferredSize(new Dimension(600, 600));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setOpaque(false);
        table.getTableHeader().setVisible(false);
        table.setDefaultRenderer(Object.class, renderer);

        table.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                System.out.println("x,y:" + e.getX() + "," + e.getY());

            }

        });
        contentPane.setLayout(gl_contentPane);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestTransparentTable frame = new TestTransparentTable();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyCellRenderer extends JLabel implements TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            // TODO Auto-generated method stub
//			setOpacity(0.2f);
            setText(value.toString());
            setBackground(Color.DARK_GRAY);
//			setToolTipText("asdf");
            setForeground(Color.blue);
            return this;
        }
    }

    class MyCellEditor extends DefaultCellEditor {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private final JLabel btn;
        private String id = "";

        public MyCellEditor(JCheckBox checkBox) {
            super(checkBox);
            btn = new JLabel();
            btn.setOpaque(true);
            btn.addMouseListener(new MouseListener() {

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
                    System.out.println("asdfasdfadsf");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

            });
            // TODO Auto-generated constructor stub
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                                                    boolean leaf, int row) {
            // TODO Auto-generated method stub
            id = value.toString();
            return btn;
        }

        public Object getCellEditorValue() {
            return id;
        }

    }
}
