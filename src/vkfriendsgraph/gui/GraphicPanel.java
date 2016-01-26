/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph.gui;

import vkfriendsgraph.graph.Graph;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import vkfriendsgraph.Properties;
import vkfriendsgraph.User;

/**
 *
 * @author tukaloff
 */
public class GraphicPanel extends JPanel {
    
    ArrayList<Object[]> arrayGraph;
    ArrayList<Object[]> arrayShapes;
    boolean isRunning;
    Rectangle rect;
    
    double width, height;
    double growingCoef = 0.5;
    double radius;
    Graph graph;
    
    double diffX = 0;
    double diffY = 0;
    
    int fps = 50;
    
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    GraphicPanel(Rectangle rect) {
        super();
        GraphicPanelMouseListener mouseListener = new GraphicPanelMouseListener();
        
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(mouseListener);
        isRunning = true;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBounds(rect);
        //new Thread(new GraphListener()).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    try {
                        long msStart = new GregorianCalendar().get(GregorianCalendar.MILLISECOND);
                        repaint();
                        long msEnd = new GregorianCalendar().get(GregorianCalendar.MILLISECOND);
                        int diff = (int)((msEnd - msStart) < 0 ? 0 : (msEnd - msStart));
                        Thread.sleep((1000 - diff) / fps);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GraphicPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
    
    private void doubleBuffering(Graphics2D g) {
        Image img1 = Properties.getProcessor().getImage();
        if (!(img1 == null)) {
            BufferedImage bi = new BufferedImage(img1.getWidth(null), img1.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr =  bi.createGraphics();
            bGr.drawImage(img1, 0, 0, null);
            g.drawImage(img1, 0, 0, null);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        //this.setBackground(new java.awt.Color(0, 183, 74));//89, 125, 163));
        Properties.setWidth(this.getWidth());
        Properties.setHeight(this.getHeight());
        doubleBuffering((Graphics2D) g);
    }

    private class GraphicPanelMouseListener 
        implements MouseListener, MouseMotionListener, MouseWheelListener {

        @Override
        public void mouseClicked(MouseEvent me) {
            User user = Properties.getProcessor().findClickedUser(me.getX(), me.getY(), 10);
            if (user == null) {
                System.out.println("Не найдено");
            }
            else {
                System.out.println(user.getName());
                try {
                    Desktop.getDesktop().browse(new URI("https://vk.com/id" + user.getUserId()));
                } catch (IOException ex) {
                    Logger.getLogger(GraphicPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(GraphicPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            diffX = Properties.getCenterPoint().getX() - me.getX();
            diffY = Properties.getCenterPoint().getY() - me.getY();
        }

        @Override
        public void mouseReleased(MouseEvent me) {

        }

        @Override
        public void mouseEntered(MouseEvent me) {

        }

        @Override
        public void mouseExited(MouseEvent me) {

        }

        @Override
        public void mouseDragged(MouseEvent me) {
            Properties.setCenterPoint(new Point2D.Double(me.getX() + diffX, me.getY() + diffY));
        }

        @Override
        public void mouseMoved(MouseEvent me) {

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mwe) {
            double x3;
            double y3;
            if (Properties.getScale() != 1 || Properties.getScale() != -1) {
                if (mwe.getWheelRotation() < 0) {//увеличение
                    Properties.setScale(Properties.getScale() * 1.2);
                    x3 = mwe.getX() + (Properties.getCenterPoint().getX() - mwe.getX()) * 1.2;
                    y3 = mwe.getY() + (Properties.getCenterPoint().getY() - mwe.getY()) * 1.2;
                }
                else {//уменьшение
                    Properties.setScale(Properties.getScale() / 1.2);
                    x3 = (Properties.getCenterPoint().getX() - mwe.getX()) / 1.2 + mwe.getX();
                    y3 = (Properties.getCenterPoint().getY() - mwe.getY()) / 1.2 + mwe.getY();
                }
                Properties.setCenterPoint(new Point2D.Double(x3, y3));
            }
        }
    }
}
