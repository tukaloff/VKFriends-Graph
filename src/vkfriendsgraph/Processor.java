/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tukaloff
 */


public class Processor {
    
    private int userId;
    private int iterations;
    private int friendsCount;
    private String filePath;
    
    private Graph graph;
    private boolean isFinished = false;
    private boolean isWorking;
    private ArrayList<Object[]> arrayShapes = new ArrayList<>();
    private Image imgGraph;
    
    private boolean repaint;
    
    public Processor(int userId, int friendsCount) {
        this.userId = userId;
        this.friendsCount = friendsCount;
    }

    public Processor(String filePath) {
        this.filePath = filePath;
        graph = new Graph();
    }
    
    /**
     * Стартер процессора
     */
    public void start() {
        isFinished = false;
        isWorking = true;
        graph = new Graph();
        iterations = Properties.getGraphDeep();
        int[] path = new int[0];
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    process(userId, path);
                    saveGraph();
                    isFinished = true;
                    isWorking = false;
                } catch (CloneNotSupportedException ex) {
                    System.out.println(this.getClass() 
                            + ": processorStart: " + ex.getMessage());
                }
            }

        }).start();
        readGraph();
    }
    
    public void readGraph() {
        new Thread(new GraphReader()).start();
    }
    
    /**
     * Сохраняет граф в корневой директории
     */
    public void saveGraph() {
        Utils.saveFile(graph, "Graph.vkg", "");
    }
    
    public void readFromFile() {
        isWorking = true;
        graph = (Graph) Utils.readFile(filePath);
        isWorking = false;
        isFinished = true;
        readGraph();
    }
    
    /*
    private void paintImage() {
        repaint = true;
        BufferedImage bi = new BufferedImage(Double.valueOf(Properties.getWidth()).intValue(), 
                Double.valueOf(Properties.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(new java.awt.Color(0, 183, 74));//89, 125, 163));
        g.fill(new Rectangle2D.Double(0, 0, bi.getWidth(), bi.getHeight()));
        for (int i = 0; i < arrayShapes.size(); i++) {
            Shape line = (Shape) (arrayShapes.get(i)[1]);
            if ((boolean)arrayShapes.get(i)[2])
                g.setColor(Color.blue);
            else
                g.setColor(Color.black);
            g.setStroke(new BasicStroke((float) 1.3));
            g.draw(line);
            g.setColor(Color.black);
        }
        for (int i = 0; i < arrayShapes.size(); i++) {
            UserPainter userPainter = (UserPainter) (arrayShapes.get(i)[0]);
            userPainter.paint(g);
        }
        bi.flush();
        imgGraph = bi;//.getScaledInstance(bi.getWidth(), bi.getHeight(), Image.SCALE_DEFAULT);
        imgGraph.flush();
        repaint = false;
    }
    */
    
    private void paintImage() {
        repaint = true;
        
        BufferedImage bi = new BufferedImage(Double.valueOf(Properties.getWidth()).intValue(), 
                Double.valueOf(Properties.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(new java.awt.Color(0, 183, 74));//89, 125, 163));
        g.fill(new Rectangle2D.Double(0, 0, bi.getWidth(), bi.getHeight()));
        
        g.drawImage(paintLinesLayer(), 0, 0, null);
        g.drawImage(paintUsersLayer(), 0, 0, null);
        
        bi.flush();
        imgGraph = bi;
        imgGraph.flush();
        
        repaint = false;
    }
    
    private BufferedImage paintLinesLayer() {
        BufferedImage bi = new BufferedImage(Double.valueOf(Properties.getWidth()).intValue(), 
                Double.valueOf(Properties.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < arrayShapes.size(); i++) {
            Shape line = (Shape) (arrayShapes.get(i)[1]);
            if ((boolean)arrayShapes.get(i)[2])
                g.setColor(Color.blue);
            else
                g.setColor(Color.black);
            g.setStroke(new BasicStroke((float) 1.3));
            g.draw(line);
            g.setColor(Color.black);
        }
        bi.flush();
        return bi;
    }
    
    private BufferedImage paintUsersLayer() {
        BufferedImage bi = new BufferedImage(Double.valueOf(Properties.getWidth()).intValue(), 
                Double.valueOf(Properties.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < arrayShapes.size(); i++) {
            UserPainter userPainter = (UserPainter) (arrayShapes.get(i)[0]);
            userPainter.paint(g);
        }
        bi.flush();
        return bi;
    }
    
    public Image getImage() {
        return imgGraph;
    }
    
    /**
     * Рекурсивный метод выполняющий все необходимые вызовы
     * @param userId - ID пользователя - корня ветки
     * @param path - путь к пользователю - массив UserID
     */
    private void process (int userId, int[] path) throws CloneNotSupportedException {
        Friends friends = new Friends(userId, friendsCount);
        if (friends.isError()) {
            return;
        }
        ArrayList<User> aFriends = friends.getArrayOfUsers();
        if (path.length == iterations) {
            int[] newPath = new int[path.length + 1];
            System.arraycopy(path, 0, newPath, 0, path.length);
            newPath[newPath.length - 1] = userId;
            for (int i = 0; i < aFriends.size(); i++) {
                waitRequest();
                if (aFriends.get(i).getDeactivated()=="" && !(aFriends.get(i).getDeactivated()=="deleted"))
                    if (aFriends.get(i).getUserId() == 0) {
                        graph.put(aFriends.get(i), newPath.length, newPath);
                        continue;
                    }
                User friend = new User(aFriends.get(i).getUserId());
                graph.put(friend, newPath.length, newPath);
            }
            return;
        }
        if (path.length == 0) {
            User me = new User(userId);
            graph.put(me, 0, path);
        }
        ArrayList<int[]> aPath = new ArrayList<>();
        int[] newPath = new int[path.length + 1];
        newPath[newPath.length - 1] = userId;
        System.arraycopy(path, 0, newPath, 0, path.length);
        for (int i = 0; i < aFriends.size(); i++) {
            waitRequest();
            if (aFriends.get(i).getDeactivated()=="" && !(aFriends.get(i).getDeactivated()=="deleted"))
                if (aFriends.get(i).getUserId() == 0) {
                    aPath.add(path);
                    graph.put(aFriends.get(i), newPath.length, newPath);
                    continue;
                }
            User friend = new User(aFriends.get(i).getUserId());
            int[] friendPath = graph.put(friend, newPath.length, newPath);
            if (newPath == friendPath) {
                aPath.add(friendPath);
            }
            else {
                aPath.add(null);
            }
        }
        for (int i = 0; i < aFriends.size(); i ++) {
            if (path.length == 0) {
                process(aFriends.get(i).getUserId(),
                        aPath.get(i));
            } else if (aPath.get(i) == null) {
                
            } else if (aPath.get(i)[aPath.get(i).length - 1] == path[path.length - 1]) {
                
            } else {
                process(aFriends.get(i).getUserId(),
                        aPath.get(i));
            }
        }
        
    }
    
    /**
     * Метод ожидания. Предназначен для соблюдения регламента
     * обращений к API VK
     */
    private void waitRequest() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Graph getGraph() {
        return graph;
    }

    boolean isFinished() {
        return isFinished;
    }
    
    boolean isWorking() {
        return isWorking;
    }

    public User findClickedUser(int x, int y, int wait) {
        User user = null;
        int k = 0;
        for (int i = 0; i < arrayShapes.size(); i++) {
            Ellipse2D ell = ((UserPainter)arrayShapes.get(i)[0]).getEllipse();
            try {
                if (ell.contains(new Point2D.Double(x, y)))
                    return ((UserPainter)arrayShapes.get(i)[0]).getUser();
            } catch (Exception e) {
                if (k++ < wait) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    return null;
                }
                i = 0;
            }
        }
        return user;
    }
    
    private class GraphReader implements Runnable {

        @Override
        public void run() {
            int lastSize = graph.getUsersCount();
            while(/*!isFinished*/true) {
                //if (lastSize != graph.getUsersCount()) {
                    try {
                        long msStart = new GregorianCalendar().get(GregorianCalendar.MILLISECOND);
                        double radius = Properties.getHeight() / 2;
                        radius *= Properties.getScale();
                        ArrayList<Object[]> innerArray = graph.getArrayGraph();
                        Thread.sleep(10);
                        arrayShapes.clear();
                        prepairShapes(innerArray, Properties.getCenterPoint(),
                            360, 360, radius, true);
                        paintImage();
                        long msEnd = new GregorianCalendar().get(GregorianCalendar.MILLISECOND);
                        int diff = (int)((msEnd - msStart) < 0 ? 0 : (msEnd - msStart));
                    } catch (Exception ex) {
                        System.out.println(this.getClass() + ": " + ex.getMessage());
                    }
                    lastSize = graph.getUsersCount();
                //}
            }
        }
    }
    
    private void prepairShapes(ArrayList<Object[]> innerArray, 
            Point2D enterPoint, double containerAngle, double parentAngle, double radius, boolean isMe) {
        if (isMe) {
            User me = (User) innerArray.get(0)[0];
            Line2D line = new Line2D.Double(enterPoint, enterPoint);
            UserPainter userPainter = new UserPainter(enterPoint, me);
            Object[] obj = {userPainter, line, false, innerArray.get(0)[1]};
            //notRepeatUsers.add(obj);
            arrayShapes.add(obj);
            prepairShapes((ArrayList<Object[]>)graph.getArrayGraph().get(0)[1], Properties.getCenterPoint(),
                                    360, 360, radius, false);
            return;
        }
        parentAngle = normalizeAngle(parentAngle);
        containerAngle = normalizeAngle(containerAngle);
        int friendsCnt;
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
        double newRadius = radius / Utils.F;
        friendsCnt = 0;
        for (int i = 0; i < innerArray.size(); i++) {
            User user = (User) innerArray.get(i)[0];
            if (!checkPoint(user.getUserId())) {
                double newAngle = parentAngle - ((int)containerAngle / 2) + angle * (friendsCnt) 
                        + Properties.getRotationAngle();
                newAngle = normalizeAngle(newAngle);
                Point2D pointOnCicle = getPointOnCicle(enterPoint, newAngle, newRadius);
                Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
                UserPainter userPainter = new UserPainter(pointOnCicle, user);
                Object[] obj = {userPainter, line, false, innerArray.get(i)[1], newAngle};
                notRepeatUsers.add(obj);
                arrayShapes.add(obj);
                friendsCnt++;
            } else {
                Point2D pointOnCicle = findPoint(user.getUserId());
                Line2D line = new Line2D.Double(enterPoint, pointOnCicle);
                UserPainter userPainter = new UserPainter(pointOnCicle, user);
                Object[] obj = {userPainter, line, true, innerArray.get(i)[1], angle};
                arrayShapes.add(obj);
            }
        }
        
        for (int i = 0; i < notRepeatUsers.size(); i++) {
            try {
                prepairShapes((ArrayList<Object[]>)notRepeatUsers.get(i)[3], 
                        (Point2D)((UserPainter)notRepeatUsers.get(i)[0]).getCenter(),
                        containerAngle / 3 * 2, 
                        (double)notRepeatUsers.get(i)[4], 
                        newRadius, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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
    
    private boolean checkPoint(int userId) {
        for (Object[] aShape : arrayShapes) {
            User user = (User) ((UserPainter)aShape[0]).getUser();
            if (userId == user.getUserId())
                return true;
        }
        return false;
    }
    
    private Point2D findPoint(int userId) {
        for (Object[] aShape : arrayShapes) {
            User user = (User) ((UserPainter)aShape[0]).getUser();
            if (userId == user.getUserId())
                return (Point2D) ((UserPainter)aShape[0]).getCenter();
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
    
    private double normalizeY (double y) {
        return Properties.getHeight() - y;
    }
    
    private double jnormalizeY (double y) {
        return Properties.getHeight() - y;
    }    
}
