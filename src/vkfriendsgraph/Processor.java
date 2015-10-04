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
        
    public void start(int userId) {
        int[] path = new int[0];
        process(userId, path);
        saveGraph();
    }
    
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

    private void process (int userId, int[] path) {
        User rootUser = new User(userId);
        int[] newPutPath = graph.put(rootUser, path.length - 1, path);
        if (!path.equals(newPutPath) && newPutPath.length != iterations)
            return;
        if (!(rootUser.getDeactivated()=="" && !(rootUser.getDeactivated()=="deleted")))
            return;
        Friends friends = new Friends(userId, friendsCount);
        ArrayList<User> aFriends = friends.getArrayOfUsers();
        if (path.length != iterations) {
            for (int i = 0; i < aFriends.size(); i++) {
                totalCount++;
                //waitRequest();
                //User childUser = new User(aFriends.get(i).getUserId());
                //aFriends.set(i, childUser);
                if (aFriends.get(i).getDeactivated()=="" && !(aFriends.get(i).getDeactivated()=="deleted"))
                    if (aFriends.get(i).getUserId() == 0)
                        return;
                System.out.print(totalCount + ". \t" + i + ": \t");
                for (int c = 0; c < path.length; c++)
                    System.out.print("|\t");
                System.out.println(aFriends.get(i).getUserId() + " \t" + aFriends.get(i).getName());
                int[] newPath = new int[path.length + 1];
                System.arraycopy(path, 0, newPath, 0, path.length);
                newPath[newPath.length - 1] = userId;
                process(aFriends.get(i).getUserId(), newPath);
            }
        }
    }
    
    private void waitRequest() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
