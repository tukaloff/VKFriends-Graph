/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 *
 * @author user
 */
public class Processor {
    
    private int userId;
    private int iterations;
    private int counter;
    
    public Processor(int userId, int iterations) {
        this.userId = userId;
        this.iterations = iterations;
        counter = 1;
    }
        
    public void start(int userId) {
        User rootUser = new User(userId);
        
        Friends friends = new Friends(userId);
        ArrayList<User> aFriends = friends.getArrayOfUsers();
        for (int i = 0; i < aFriends.size(); i++) {
            waitRequest();
            User childUser = new User(aFriends.get(i).getUserId());
            aFriends.set(i, childUser);
        }
        if (counter != iterations) {
            counter++;
            for (int i = 0; i < aFriends.size(); i++) {
                if (aFriends.get(i).getDeactivated()=="" || !(aFriends.get(i).getDeactivated()=="deleted"))
                    if (aFriends.get(i).getUserId() != 0)
                        start(aFriends.get(i).getUserId());
            }
            counter = 1;
        }
    }
    
    private void waitRequest() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
