/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Processor {
    
    private int userId;
    private int iterations;
    private int friendsCount;
    Graph graph;
    private int totalCount = 1;
    
    public Processor(int userId, int iterations, int friendsCount) {
        this.userId = userId;
        this.iterations = iterations;
        this.friendsCount = friendsCount;
        graph = new Graph();
    }
    
    /**
     * Стартер процессора
     * @param userId - ID пользователя,
     * с которого начинается построение графа
     */
    public void start(int userId) {
        int[] path = new int[0];
        process(userId, path);
        saveGraph();
    }
    
    /**
     * Сохраняет граф в корневой директории
     */
    public void saveGraph() {
        try {
            FileOutputStream fos = new FileOutputStream("Graph.vkg");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(graph.getArrayGraph());
            oos.flush();
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Рекурсивный метод выполняющий все необходимые вызовы
     * @param userId - ID пользователя - корня ветки
     * @param path - путь к пользователю - массив UserID
     */
    private void process (int userId, int[] path) {
        /**
         * Получаем пользователя. Выходим, если пользователь
         * находится на нижнем уровне или если пользователь
         * помечен, как deleted
         */
        User rootUser = new User(userId);
        int[] newPutPath;
        //if (path.length == 0) {
        //    newPutPath = graph.put(rootUser, path.length, path);
        //}
        //} else {
            newPutPath = graph.put(rootUser, path.length, path);
        //}
        if (!path.equals(newPutPath) && newPutPath.length != iterations)
            return;
        if (!(rootUser.getDeactivated()=="" && !(rootUser.getDeactivated()=="deleted")))
            return;
        /**
         * Получаем друзей пользователя.
         * Для каждого друга рекурсивно вызываем метод process
         * пока не достигнем нижнего уровня (iterations)
         */
        Friends friends = new Friends(userId, friendsCount);
        ArrayList<User> aFriends = friends.getArrayOfUsers();
        if (path.length != iterations) {
            for (int i = 0; i < aFriends.size(); i++) {
                totalCount++;
                waitRequest();
                //User childUser = new User(aFriends.get(i).getUserId());
                //aFriends.set(i, childUser);
                if (aFriends.get(i).getDeactivated()=="" && !(aFriends.get(i).getDeactivated()=="deleted"))
                    if (aFriends.get(i).getUserId() == 0)
                        return;
                /*
                System.out.print(totalCount + ". \t" + i + ": \t");
                for (int c = 0; c < path.length; c++)
                    System.out.print("|\t");
                System.out.println(aFriends.get(i).getUserId() + " \t" + aFriends.get(i).getName());
                */
                int[] newPath = new int[path.length + 1];
                System.arraycopy(path, 0, newPath, 0, path.length);
                newPath[newPath.length - 1] = userId;
                process(aFriends.get(i).getUserId(), newPath);
            }
        }
    }
    
    /**
     * Метод ожидания. Предназначен для соблюдения регламента
     * обращений к API VK
     */
    private void waitRequest() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
