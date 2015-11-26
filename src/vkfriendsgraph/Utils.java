/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Dimension;

/**
 *
 * @author user
 */
public class Utils {
    
    public static Dimension resetDimension(Dimension dim, int perc) {
        double width = dim.getWidth();
        double height = dim.getHeight();
        width = (width / 100) * perc;
        height = (height / 100) * perc;
        dim.setSize(width, height);
        return dim;
    }
}
