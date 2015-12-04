/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ViewportLayout;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author user
 */
public class MenuPanel extends JPanel{
    
    private boolean isRunning;
    private double scale;
    private int mensCount;
    
    JLabel lblNScale;
    JLabel lblScale;
    private JLabel lblNMensCount;
    private JLabel lblMensCount;
    private JLabel lblDeep;
    private JTextField tfDeep;
    
    public MenuPanel(LayoutManager layout) {
        super(layout);
        isRunning = true;
        initComponents();
        runPropertiesUpdater();
    }

    private void runPropertiesUpdater() {
        Thread runner = new Thread(new PropertiesListener());
        runner.start();
    }

    private void initComponents() {
        
        JButton btnDefaults = new JButton("По умолчанию");
        btnDefaults.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println(this.getClass() + ": " + ae.getActionCommand());
                Properties.setDefault();
            }
        });
        this.add(btnDefaults);
        
        JButton btnStart = new JButton("Старт");
        btnStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println(this.getClass() + ": " + ae.getActionCommand());
                Properties.setGraphStarted(true);
            }
        });
        this.add(btnStart);
        
        float fontScale = 14;
        lblNScale = new JLabel("Масштаб: ");
        lblNScale.setFont(lblNScale.getFont().deriveFont(fontScale));
        //lblNScale.set(0, 0, 20, this.getHeight() / 4 * 2);
        lblScale = new JLabel(Double.toString(scale));
        lblScale.setFont(lblScale.getFont().deriveFont(fontScale));
        JPanel panelScale = new JPanel(new GridLayout(1, 2));//FlowLayout());
        panelScale.add(lblNScale);
        panelScale.add(lblScale);
        this.add(panelScale);
        
        lblNMensCount = new JLabel("Количество пользователей: ");
        lblNMensCount.setFont(lblNMensCount.getFont().deriveFont(fontScale));
        lblMensCount = new JLabel(Integer.toString(mensCount));
        lblMensCount.setFont(lblMensCount.getFont().deriveFont(fontScale));
        JPanel panelMensCount = new JPanel(new GridLayout(1, 2));
        panelMensCount.add(lblNMensCount);
        panelMensCount.add(lblMensCount);
        this.add(panelMensCount);
        
        lblDeep = new JLabel("Глубина графа: ");
        lblDeep.setFont(lblNMensCount.getFont().deriveFont(fontScale));
        System.out.println(Properties.getGraphDeep());
        tfDeep = new JTextField(Integer.toString(Properties.getGraphDeep()));
        tfDeep.setFont(tfDeep.getFont().deriveFont(fontScale));
        tfDeep.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
                if (ke.getKeyChar() == '\n')
                    try {
                        Properties.setGraphDeep(Double.valueOf(tfDeep.getText()).intValue());
                    } catch (Exception e) {
                        tfDeep.setText("");
                    }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
        JPanel panelDeep = new JPanel(new GridLayout(1, 2));
        panelDeep.add(lblDeep);
        panelDeep.add(tfDeep);
        this.add(panelDeep);
        
        
    }
    
    private class PropertiesListener implements Runnable {

        @Override
        public void run() {
            while(isRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                checkScaleChanged();
                checkMensCount();
            }
        }
        
    }
    
    private void checkScaleChanged() {
        if (Properties.getScale() != scale) {
            scale = Properties.getScale();
            lblScale.setText(Double.toString((double)Math.floorDiv((int) (scale * 1000), 1) / 1000));
        }
    }
    
    private void checkMensCount() {
        if (Properties.getMensCount() != mensCount) {
            mensCount = Properties.getMensCount();
            lblMensCount.setText(Integer.toString(mensCount));
        }
    }
}
