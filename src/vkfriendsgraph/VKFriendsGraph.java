/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
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
        
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainFrame frame = new MainFrame(newDim.width, newDim.height);
                frame.setSize(newDim);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
    
}
