/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class GraphicPanel extends JPanel {
    
    ArrayList<Object[]> arrayGraph;
    ArrayList<Object[]> arrayShapes;
    boolean isRunning;
    Rectangle rect;
    final double F = (1 + Math.sqrt(5))/ 2;
    double width, height;

    GraphicPanel(Rectangle rect, MainFrame mainFrame) {
        super();
        this.rect = rect;
        width = rect.width;
        height = rect.height;
        isRunning = true;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBounds(rect);
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean graphCreated = false;
                while(!graphCreated) {
                    boolean failed = false;
                    try {
                        arrayGraph = mainFrame.processor.graph.getArrayGraph();
                    } catch (Exception ex) {
                        failed = true;
                        graphCreated = false;
                    }
                    if (!failed) {
                        graphCreated = true;
                    }
                }
                arrayShapes = new ArrayList<>();
                runRepainter();
            }
        }).start();

    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //System.out.println("paintComponent");
        width = this.getWidth();
        height = this.getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        try {
        for(int i = 0; i < arrayShapes.size(); i++) {
            Shape line = (Shape) (arrayShapes.get(i)[1]);
            g2.draw(line);
            /*
            TextLayout tl = new TextLayout((String) (arrayShapes.get(i)[3]), 
                    new Font("Serif", Font.CENTER_BASELINE, 10),
                    g2.getFontRenderContext());
            Shape text = tl.getOutline(new AffineTransform());
            g2.draw(text);*/
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
            g2.drawString((String) (arrayShapes.get(i)[3]), 
                    Double.valueOf(((Point2D.Double)arrayShapes.get(i)[2]).getX()).intValue(),
                    Double.valueOf(((Point2D.Double)arrayShapes.get(i)[2]).getY()).intValue());
            
        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("paintComponent complit");
    }
    
    private void runRepainter() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(isRunning) {
                    try {
                        Thread.sleep(100);
                        arrayShapes.clear();
                        
                        prepairShapes((ArrayList<Object[]>)arrayGraph.get(0)[1], new Point2D.Double(width / 2, 
                                height / 2), 
                                360, 0, height / 3);
                        /*
                        k = 0;
                        test(new Point2D.Double(width / 2, 
                                height / 2), 
                                360, 0, height / 3);
                                */
                        repaint();
                        //isRunning = false;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }).start();
    }
    
    int k;
    
    private void test(Point2D enterPoint, double containerAngle, double parentAngle, double radius) {
        k++;
        if (parentAngle > 360)
            parentAngle -= 360;
        else if (parentAngle < 0)
            parentAngle = 360 - parentAngle;
        double angle = containerAngle / 5;
        double newRadius = radius / F;
        for (int i = 0; i < 6; i++) {
            double newAngle = parentAngle - (containerAngle / 2) + (angle * (i));
            if (newAngle > 360)
                newAngle -= 360;
            else if (newAngle < 0)
                newAngle = 360 - newAngle;
            Point2D pointOnCicle = getPointOnCicle(enterPoint, 
                    newAngle, newRadius);
            Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
            Object[] obj = {newAngle, line, pointOnCicle};
            arrayShapes.add(obj);
            if (k < 3 * 6)
            try {
                //System.out.println(k++);
                test(pointOnCicle, containerAngle, parentAngle, newRadius);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private void prepairShapes(ArrayList<Object[]> innerArray, 
            Point2D enterPoint, double containerAngle, double parentAngle, double radius) {
        System.out.println(parentAngle);
        if (parentAngle > 360)
            parentAngle -= 360;
        else if (parentAngle < 0)
            parentAngle = 360 - parentAngle;
        double angle;
        if (innerArray.size() <= 1) {
            angle = parentAngle;
        }
        else {
            angle = containerAngle / innerArray.size();
        }
        double newRadius = radius / F;
        int k = 0;
        for (int i = 0; i < innerArray.size(); i++) {
            Object[] userF = innerArray.get(i);
            User user = (User) userF[0];
            if (!checkPoint(user.getUserId())) {
                k++;
                double newAngle = parentAngle - (containerAngle / 2) + (angle * (k));
                if (newAngle > 360)
                    newAngle -= 360;
                else if (newAngle < 0)
                    newAngle = 360 - newAngle;
                Point2D pointOnCicle = getPointOnCicle(enterPoint, 
                        newAngle, newRadius);
                Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
                Object[] obj = {user, line, pointOnCicle, user.getName()};
                arrayShapes.add(obj);
                try {
                    //System.out.println(k++);
                    prepairShapes((ArrayList<Object[]>) userF[1], pointOnCicle, 
                            containerAngle / 3 * 2, newAngle, newRadius);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    private boolean checkPoint(int userId) {
        for (Object[] aShape : arrayShapes) {
            User user = (User) aShape[0];
            if (userId == user.getUserId())
                return true;
        }
        return false;
    }
    
    private Point2D getPointOnCicle(Point2D enterPoint, double angle, double radius) {
        angle = angle * Math.PI / 180;
        double x0 = enterPoint.getX();
        double y0 = enterPoint.getY();
        y0 = jnormalizeY(y0);
        double x1 = x0 + (Math.cos(angle) * radius);
        double y1 = y0 + (Math.sin(angle) * radius);
        y1 = normalizeY(y1);
        return new Point2D.Double(x1, y1);
    }
    
    private double normalizeY (double y) {
        return height - y;
    }
    
    private double jnormalizeY (double y) {
        return height - y;
    }

    private Object User(Object get) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
