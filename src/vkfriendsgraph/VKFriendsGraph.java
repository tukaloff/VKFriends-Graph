/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.swing.JFrame;
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
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension newDim = resetDimension(dim, 70);
        
        Properties.loadProperties();
        if (Properties.getAccessToken() == null || Properties.getAccessToken() == "") {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    JFrame frame = new JFrame();
                    AutorizFXPanel authPanel = new AutorizFXPanel();
                    frame.add(authPanel);
                    frame.setSize(800, 610);
                    frame.setResizable(false);
                    frame.setLocationByPlatform(true);
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
                                    frame.dispose();
                                    exit = true;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
        
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
                        MainFrame frame = new MainFrame(newDim.width, newDim.height);
                        frame.setSize(newDim);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                    }
                }).start();
            }
        });
        
    }
    
}
