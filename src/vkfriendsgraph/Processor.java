/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author tukaloff
 */
public class Processor {
    
    private int userId;
    private int iterations;
    private int friendsCount;
    private int totalCount = 1;
    private String filePath;
    
    private Graph graph;
    private boolean isFinished = false;
    
    public Processor(int userId, int iterations, int friendsCount) {
        this.userId = userId;
        this.iterations = iterations;
        this.friendsCount = friendsCount;
        graph = new Graph();
    }

    public Processor(String filePath) {
        this.filePath = filePath;
        graph = new Graph();
    }
    
    /**
     * Стартер процессора
     */
    public void start() {
        int[] path = new int[0];
        process(userId, path);
        isFinished = true;
        saveGraph();
    }
    
    /**
     * Сохраняет граф в корневой директории
     */
    public void saveGraph() {
        Utils.saveFile(graph, "Graph.vkg", "");
    }
    
    public void readFromFile() {
        graph = (Graph) Utils.readFile(filePath);
        isFinished = true;
    }
    
    /**
     * Рекурсивный метод выполняющий все необходимые вызовы
     * @param userId - ID пользователя - корня ветки
     * @param path - путь к пользователю - массив UserID
     */
    private void process (int userId, int[] path) {
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
}
