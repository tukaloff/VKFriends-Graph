/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import vkfriendsgraph.graph.Processor;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
        //super(layout);
        super();
        isRunning = true;
        initComponents();
        runPropertiesUpdater();
    }

    private void runPropertiesUpdater() {
        Thread runner = new Thread(new PropertiesListener());
        runner.start();
    }

    private void initComponents() {
        
        JPanel me = new JPanel();
        User uMe = new User(Properties.getMyID());
        me.add(new JLabel(uMe.getName(), new ImageIcon(uMe.getPhoto50()), JLabel.LEFT));
        me.setSize(this.getWidth(), 100);
        this.add(me);
        
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
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String[] path;
                        path = new String[] {"response", "uid"};
                        int tryConnect = 10;
                        String filePath = "Graph.vkg";
                        Properties.setProcessor(new Processor(Properties.getMyID(), 4));
                        System.out.println("start");
                        Properties.getProcessor().start();
                        System.out.println("Properties.getProcessor().start();");
                    }
                }).start();
            }
        });
        this.add(btnStart);
        
        float fontScale = 14;
        lblNScale = new JLabel("Масштаб: ");
        lblNScale.setFont(lblNScale.getFont().deriveFont(fontScale));
        //lblNScale.set(0, 0, 20, this.getHeight() / 4 * 2);
        lblScale = new JLabel(Double.toString(scale));
        lblScale.setFont(lblScale.getFont().deriveFont(fontScale));
        JPanel panelScale = new JPanel(new GridLayout(1, 2));
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
        //System.out.println(Properties.getGraphDeep());
        
        JSlider slider = new JSlider(1, 20);
        slider.setValue(Properties.getGraphDeep());
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                int value = slider.getValue();
                System.out.println(value);
                Properties.setGraphDeep(value);
            }
        });
        
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
        
        JPanel thiss = this;
        
        JButton buttonForgetToken = new JButton("Выйти");
        buttonForgetToken.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("forgot");
                Properties.setAccesToken(null);
                me.removeAll();
                thiss.remove(me);
            }
        });
        
        JPanel panelDeep = new JPanel(new GridLayout(1, 2));
        panelDeep.add(lblDeep);
        //panelDeep.add(tfDeep);
        
        this.add(panelDeep);
        this.add(slider);
        this.add(buttonForgetToken);
        
        
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
