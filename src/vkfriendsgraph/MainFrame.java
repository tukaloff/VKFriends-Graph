/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static vkfriendsgraph.Utils.resetDimension;

/**
 *
 * @author tukaloff
 */
public class MainFrame extends JFrame {
    
    GraphicPanel panel;
    double width, height;
    Rectangle rect;
    Processor processor;
    
    boolean readOnline;
    
    public MainFrame(double width, double height) {
        super();
        
        readOnline = false;
        
        this.width = width;
        this.height = height;
        this.setSize(Double.valueOf(width).intValue(), Double.valueOf(height).intValue());
        new Thread(new Runnable() {

            @Override
            public void run() {
                int USER_ID = 76141154;//88374578 18725186;// 85800109
                //int USER_ID = Integer.valueOf(1039324);//208);//88374578);//8308498);
                System.out.println(USER_ID);
                String[] path;
                path = new String[] {"response", "uid"};
                int tryConnect = 10;
                String filePath = "Graph.vkg";
                if (readOnline) {
                    processor = new Processor(USER_ID, Properties.getGraphDeep(),4);
                    while (!Properties.isGraphStarted())
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    System.out.println("start");
                    processor.start();
                }
                else {
                    processor = new Processor(filePath);
                    processor.readFromFile();
                }
                System.out.println("Processor finished");
            }
        }).start();
        initComponents();
    }
    
    private void initComponents() {
        rect = new Rectangle(new Point(Double.valueOf(width / 100 * 10).intValue(),
                Double.valueOf(height / 100 * 10).intValue()),
                resetDimension(new Dimension(Double.valueOf(width).intValue(),
                        Double.valueOf(height).intValue()), 80));
        while(processor == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        panel = new GraphicPanel(rect, this.processor);
        panel.setBounds(rect);
        panel.setBackground(new java.awt.Color(89, 125, 163));
        panel.setVisible(true);
        
        JPanel rootPanel = new JPanel(new BorderLayout());
        MenuPanel menuPanel = new MenuPanel(new GridLayout(20, 1));//new JPanel(new FlowLayout());
        menuPanel.setPreferredSize(new Dimension(300, 600));
        menuPanel.setBounds(new Rectangle(new Dimension(300, 600)));
        
        rootPanel.add(menuPanel, BorderLayout.WEST);
        rootPanel.add(panel, BorderLayout.CENTER);
        this.getContentPane().add(rootPanel);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Properties.saveProperties();
            }
        });
    }
}
