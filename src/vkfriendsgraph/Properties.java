/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 *
 * @author user
 */
public class Properties implements Serializable {
    
    private static double scale = 1;
    double pScale = 1;
    private static int mensCount;
    int pMensCount;
    private static Point2D centerPoint = new Point2D.Double(200, 200);
    Point2D pCenterPoint;
    private static int graphDeep;
    int pGraphDeep;
    private static boolean isGraphStarted;
    
    private static double width;
    private static double height;
    
    private static double rotationAngle = 0;
    
    private static Processor processor;
    
    private Properties() {
        pScale = scale;
        pMensCount = mensCount;
        pCenterPoint = centerPoint;
        pGraphDeep = graphDeep;
    }
    
    public static void setDefault() {
        scale = 1;
        centerPoint = new Point2D.Double(width / 2, height / 2);
    }
    
    public static void saveProperties() {
        Utils.saveFile(new Properties(), "Properties.vkp", "");
        System.out.println(Properties.class.getName() + ": Saved");
    }
    
    public static void loadProperties() {
        try {
            Properties prop = (Properties) Utils.readFile("Properties.vkp");
            scale = prop.pScale;
            centerPoint = prop.pCenterPoint;
            graphDeep = prop.pGraphDeep;
            System.out.println(Properties.class.getName() + ": Loaded");
        } catch (Exception e) {
            System.out.println(Properties.class.getName() + ": " 
                    + e.getMessage());
        }
    }
    
    public static double getScale() {
        return scale;
    }
    
    public static void setScale(double scale) {
        Properties.scale = scale;
    }
    
    public static int getMensCount() {
        return mensCount;
    }
    
    public static void setMensCount(int count) {
        Properties.mensCount = count;
    }
    
    public static Point2D getCenterPoint() {
        return centerPoint;
    }
    
    public static void setCenterPoint(Point2D point) {
        Properties.centerPoint = point;
    }
    
    public static double getWidth() {
        return width;
    }
    
    public static void setWidth(double width) {
        Properties.width = width;
    }
    
    public static double getHeight() {
        return height;
    }
    
    public static void setHeight(double height) {
        Properties.height = height;
    }
    
    public static int getGraphDeep() {
        return graphDeep;
    }
    
    public static void setGraphDeep(int deep) {
        Properties.graphDeep = deep;
    }
    
    public static boolean isGraphStarted() {
        return isGraphStarted;
    }
    
    public static void setGraphStarted(boolean isGraphStarted) {
        Properties.isGraphStarted = isGraphStarted;
    }
    
    public static Processor getProcessor() {
        return Properties.processor;
    }
    
    public static void setProcessor(Processor proc) {
        Properties.processor = proc;
    }
    
    public static double getRotationAngle() {
        return rotationAngle;
    }
    
    public static void getRotationAngle(double rotationAngle) {
        Properties.rotationAngle = rotationAngle;
    }
}
