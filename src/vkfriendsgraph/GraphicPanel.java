/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    double growingCoef = 0.5;
    double scale = 1;
    double radius;
    
    double diffX = 0;
    double diffY = 0;
    
    Point2D startPoint;

    GraphicPanel(Rectangle rect, MainFrame mainFrame) {
        super();
        
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                System.out.println("pressed");
                diffX = startPoint.getX() - me.getX();
                diffY = startPoint.getY() - me.getY();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                System.out.println("released");
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        });
        
        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent me) {
                startPoint = new Point2D.Double(me.getX() + diffX, me.getY() + diffY);
            }

            @Override
            public void mouseMoved(MouseEvent me) {
                
            }
        });
        
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                
                System.out.println("mwe: " + mwe.paramString());
                double x3;
                double y3;
                if (scale != 1 || scale != -1) {
                    if (mwe.getWheelRotation() < 0) {//увеличение
                        scale *= 1.2;
                        x3 = mwe.getX() + (startPoint.getX() - mwe.getX()) * 1.2;
                        y3 = mwe.getY() + (startPoint.getY() - mwe.getY()) * 1.2;
                    }
                    else {//уменьшение
                        scale /= 1.2;
                        x3 = (startPoint.getX() - mwe.getX()) / 1.2 + mwe.getX();
                        y3 = (startPoint.getY() - mwe.getY()) / 1.2 + mwe.getY();
                    }
                    startPoint = new Point2D.Double(x3, y3);
                }
            }
        });
        /*
        this.rect = rect;
        width = rect.width;
        height = rect.height;
        */
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
        radius = height / 2;
        Graphics2D g2 = (Graphics2D) g;
        FontRenderContext context = g2.getFontRenderContext();
        if (10 * (scale / 1.2) < 40)
            g2.setFont(g2.getFont().deriveFont((float)(10 * (scale / 1.2))));
        else 
            g2.setFont(g2.getFont().deriveFont((float)(40)));
        Font font = g2.getFont();

        g2.setColor(Color.black);
        try {
            for(int i = 0; i < arrayShapes.size(); i++) {
                Shape line = (Shape) (arrayShapes.get(i)[1]);
                if ((boolean)arrayShapes.get(i)[6])
                    g2.setColor(Color.blue);
                else
                    g2.setColor(Color.black);
                g2.setStroke(new BasicStroke((float) 1.3));
                g2.draw(line);
                g2.setColor(Color.black);
                /*
                TextLayout tl = new TextLayout((String) (arrayShapes.get(i)[3]), 
                        new Font("Serif", Font.CENTER_BASELINE, 10),
                        g2.getFontRenderContext());
                Shape text = tl.getOutline(new AffineTransform());
                g2.draw(text);*/
                //g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                /*
                g2.drawString((String) (arrayShapes.get(i)[3]), //Integer.toString(Double.valueOf((double) (arrayShapes.get(i)[3])).intValue()), 
                        Double.valueOf(((Point2D.Double)arrayShapes.get(i)[2]).getX()).intValue(),
                        Double.valueOf(((Point2D.Double)arrayShapes.get(i)[2]).getY()).intValue());
                */
            }
            
            for(int i = 0; i < arrayShapes.size(); i++) {
                Point2D point = (Point2D) arrayShapes.get(i)[2];
                String firstName = ((User) arrayShapes.get(i)[0]).getFirstName();
                String lastName = ((User) arrayShapes.get(i)[0]).getLastName();
                Rectangle2D fNameRect = font.getStringBounds(firstName, context);
                Rectangle2D lNameRect = font.getStringBounds(lastName, context);
                double fullWidth = (fNameRect.getWidth() > lNameRect.getWidth() ? fNameRect.getWidth() : lNameRect.getWidth());
                double fullHeight = fNameRect.getHeight()/* - fNameRect.getY()*/ + lNameRect.getHeight()/* - lNameRect.getY()*/;
                
                Ellipse2D ellipse = new Ellipse2D.Double(point.getX() - (fullWidth > fullHeight ? fullWidth / 2 : fullHeight / 2), 
                        point.getY() - (fullWidth > fullHeight ? fullWidth / 2 : fullHeight / 2), 
                        (fullWidth > fullHeight ? fullWidth : fullHeight), (fullWidth > fullHeight ? fullWidth : fullHeight));
                
                Color lastColor = g2.getColor();
                
                g2.setPaint(new GradientPaint(new Point2D.Double(ellipse.getMinX(), ellipse.getMinY()), 
                        new Color(100, 150, 100), 
                        new Point2D.Double(ellipse.getMaxX(), ellipse.getMaxY()), 
                        new Color(100, 150, 150)));
                g2.fill(ellipse);
                g2.setColor(lastColor);
                g2.draw(ellipse);
                                
                g2.drawString(firstName, (float)(point.getX() - fNameRect.getWidth() / 2), 
                        (float)(point.getY() - fullHeight / 2 - fNameRect.getY()));
                g2.drawString(lastName, (float)(point.getX() - lNameRect.getWidth() / 2), 
                        (float)(point.getY() + fullHeight / 2 - lNameRect.getHeight() - lNameRect.getY()));
                
                
            }
            
        } catch (Exception e) {
            System.out.println("123" + e.getMessage());
        }
        //System.out.println("paintComponent complit");
    }
    
    private void runRepainter() {
        width = this.getWidth();
        height = this.getHeight();
        radius = height / 2;
        startPoint = new Point2D.Double(width / 2, height / 2);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(isRunning) {
                    try {
                        int k = 0;
                        Thread.sleep(100);
                        arrayShapes.clear();
                        double radius = height / 2;
                        radius *= scale;
                        //radius -= radius * growingCoef;
                        growingCoef = growingCoef / 1.005;
                        //radius += radius * growingCoef;
                        if (arrayGraph.size() > 0 && height > 0) {
                            ArrayList<Object[]> innerArray = arrayGraph;//(ArrayList<Object[]>)arrayGraph.get(0)[1];
                            prepairShapes(innerArray, startPoint,//new Point2D.Double(width / 2, height / 2), 
                                    360, 360, radius, true);
                        }
                        /*
                        k = 0;
                        test(new Point2D.Double(width / 2, 
                                height / 2), 
                                360, 0, height / 3);
                                */
                        repaint();
                        //isRunning = false;
                    } catch (Exception ex) {
                        System.out.println("147" + ex.getMessage());
                    }
                }
            }
        }).start();
    }
    
    int k = 0;
    
    private void prepairShapes(ArrayList<Object[]> innerArray, 
            Point2D enterPoint, double containerAngle, double parentAngle, double radius, boolean isMe) {
        if (isMe) {
            User me = (User) innerArray.get(0)[0];
            Line2D line = new Line2D.Double(enterPoint, enterPoint);
            Object[] obj = {me, line, enterPoint, me.getName(), innerArray.get(0)[1], 0, false};
            //notRepeatUsers.add(obj);
            arrayShapes.add(obj);
            prepairShapes((ArrayList<Object[]>)arrayGraph.get(0)[1], startPoint,//new Point2D.Double(width / 2, height / 2), 
                                    360, 360, radius, false);
            return;
        }
        parentAngle = normalizeAngle(parentAngle);
        containerAngle = normalizeAngle(containerAngle);
        int friendsCount = 0;
        double angle;
        if (innerArray.size() == 0) {
            angle = 0;
        } else if (innerArray.size() == 1){
            angle = containerAngle / 2;
        } else {
            if (containerAngle < 360) {
                angle = containerAngle / (innerArray.size() - 1);
            } else {
                angle = containerAngle / innerArray.size();
            }
        }
        ArrayList<Object[]> notRepeatUsers = new ArrayList<>();
        double newRadius = radius / F;
        friendsCount = 0;
        for (int i = 0; i < innerArray.size(); i++) {
            User user = (User) innerArray.get(i)[0];
            if (!checkPoint(user.getUserId())) {
                double newAngle = parentAngle - ((int)containerAngle / 2) + angle * (friendsCount);
                newAngle = normalizeAngle(newAngle);
                Point2D pointOnCicle = getPointOnCicle(enterPoint, newAngle, newRadius);
                Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
                Object[] obj = {user, line, pointOnCicle, user.getName(), innerArray.get(i)[1], newAngle, false};
                notRepeatUsers.add(obj);
                arrayShapes.add(obj);
                friendsCount++;
            } else {
                Point2D pointOnCicle = findPoint(user.getUserId());
                Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
                Object[] obj = {user, line, pointOnCicle, user.getName(), innerArray.get(i)[1], angle, true};
                arrayShapes.add(obj);
            }
        }
        
        for (int i = 0; i < notRepeatUsers.size(); i++) {
            try {
                prepairShapes((ArrayList<Object[]>)notRepeatUsers.get(i)[4], 
                        (Point2D)notRepeatUsers.get(i)[2],
                        containerAngle / 3 * 2, 
                        (double)notRepeatUsers.get(i)[5], 
                        newRadius, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
    
    private Point2D findPoint(int userId) {
        for (Object[] aShape : arrayShapes) {
            User user = (User) aShape[0];
            if (userId == user.getUserId())
                return (Point2D) aShape[2];
        }
        return null;
    }
    
    private Point2D getPointOnCicle(Point2D enterPoint, double angle, double radius) {
        double gradusAngle = angle * Math.PI / 180;
        double x0 = enterPoint.getX();
        double y0 = enterPoint.getY();
        y0 = jnormalizeY(y0);
        double x1 = x0 + (Math.cos(gradusAngle) * radius);
        double y1 = y0 + (Math.sin(gradusAngle) * radius);
        y1 = normalizeY(y1);
        return new Point2D.Double(x1, y1);
    }
    
    private double getAngleByDiagonal(double a, double d) {
        double radianAngle = Math.asin(a / d);
        return radianAngle / Math.PI * 180;
    }
    
    private double getDiagonalByPoints(Point2D a, Point2D b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
    
    private double normalizeY (double y) {
        return height - y;
    }
    
    private double jnormalizeY (double y) {
        return height - y;
    }    

    private double normalizeAngle(double angle) {
        if (angle > 360) {
            angle -= 360;
            normalizeAngle(angle);
        } else if (angle < 0) {
            angle = 360 + angle;
            return angle;
        }
        return angle;
    }
}
