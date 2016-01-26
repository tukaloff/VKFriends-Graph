/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import vkfriendsgraph.gui.MainFrame;
import vkfriendsgraph.gui.AutorizFXPanel;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import static vkfriendsgraph.Utils.resetDimension;

/**
 *
 * @author tukaloff
 */
public class VKFriendsGraph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setUIManager();
        //Utils.saveFile("asd", "asd.txt", "");
        Properties.loadProperties();
        if (Properties.getAccessToken() == null || Properties.getAccessToken() == "") {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    JFrame frame = new JFrame();
                    AutorizFXPanel authPanel = new AutorizFXPanel();
                    frame.add(authPanel);
                    frame.setSize(800, 640);
                    frame.setResizable(false);
                    frame.setLocationByPlatform(true);
                    frame.addWindowListener(new WindowListener() {

                        @Override
                        public void windowOpened(WindowEvent we) {
                        }

                        @Override
                        public void windowClosing(WindowEvent we) {
                            System.out.println("invoke");
                            invoke();
                            frame.dispose();
                        }

                        @Override
                        public void windowClosed(WindowEvent we) {
                        }

                        @Override
                        public void windowIconified(WindowEvent we) {
                        }

                        @Override
                        public void windowDeiconified(WindowEvent we) {
                        }

                        @Override
                        public void windowActivated(WindowEvent we) {
                        }

                        @Override
                        public void windowDeactivated(WindowEvent we) {
                        }
                    });
                    frame.setVisible(true);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            authPanel.initFX();
                            System.out.println("");
                        }
                    });
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            boolean exit = false;
                            while(!exit) {
                                if (!(Properties.getAccessToken() == null || Properties.getAccessToken() == "")) {
                                    System.out.println("asd");
                                    frame.setVisible(false);
                                    //frame.dispose();
                                    exit = true;
                                }
                            }
                        }
                    });//.start();
                }
            });
        } 
        else {
            invoke();
        }
    }
    
    private static void invoke() {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        boolean exit = false;
                        while(!exit) {
                            if (!(Properties.getAccessToken() == null || Properties.getAccessToken() == "")) {
                                System.out.println("asd");
                                exit = true;
                            }
                        }
                        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                        Dimension newDim = resetDimension(dim, 70);
                        MainFrame frame = new MainFrame(newDim.width, newDim.height);
                        //frame.setSize(newDim);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                    }
                }).start();
            }
        });
    }

    private static void setUIManager() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VKFriendsGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(VKFriendsGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(VKFriendsGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(VKFriendsGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
