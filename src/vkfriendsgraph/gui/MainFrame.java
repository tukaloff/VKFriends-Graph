/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph.gui;

import vkfriendsgraph.graph.Processor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import vkfriendsgraph.Properties;
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
        
        readOnline = true;
        
        this.width = width;
        this.height = height;
        this.setSize(Double.valueOf(width).intValue(), Double.valueOf(height).intValue());
        Properties.setProcessor(new Processor("Graph.vkg"));
        Properties.getProcessor().readFromFile();
        initComponents();
    }
    
    private void initComponents() {
        rect = new Rectangle(new Point(Double.valueOf(width / 100 * 10).intValue(),
                Double.valueOf(height / 100 * 10).intValue()),
                resetDimension(new Dimension(Double.valueOf(width).intValue(),
                        Double.valueOf(height).intValue()), 80));
        panel = new GraphicPanel(rect);
        panel.setBounds(rect);
        panel.setBackground(new java.awt.Color(0, 183, 74));//89, 125, 163));
        panel.setVisible(true);
        
        JPanel rootPanel = new JPanel(new BorderLayout());
        
        //MenuPanel menuPanel = new MenuPanel(new GridLayout(16, 1));//new JPanel(new FlowLayout());
        MenuPanel menuPanel = new MenuPanel(new GridBagLayout());
        menuPanel.setPreferredSize(new Dimension(300, 600));
        menuPanel.setBounds(new Rectangle(new Dimension(300, 600)));
        menuPanel.setBackground(new java.awt.Color(0, 119, 48));//89, 125, 163));
        
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
