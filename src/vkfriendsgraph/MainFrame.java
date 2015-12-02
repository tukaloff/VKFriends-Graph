/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;
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
                int USER_ID = 76141154;//88374578 18725186;//
                //int USER_ID = Integer.valueOf(1039324);//208);//88374578);//8308498);
                System.out.println(USER_ID);
                String[] path;
                path = new String[] {"response", "uid"};
                int tryConnect = 10;
                String filePath = "Graph.vkg";
                if (readOnline) {
                    processor = new Processor(USER_ID, 3, 4);
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
        while(processor == null)
            continue;
        panel = new GraphicPanel(rect, this.processor);
        
        panel.setBounds(rect);
        System.out.println("Panel: " + panel.getLocation().x + ", " + panel.getLocation().y);
        panel.setBackground(new java.awt.Color(50, 150, 50));
        panel.setVisible(true);
        this.getContentPane().add(panel);
    }
}
