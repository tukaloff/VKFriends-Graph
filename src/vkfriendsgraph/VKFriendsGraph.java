/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class VKFriendsGraph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(new GregorianCalendar().toInstant());
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(new GregorianCalendar().toInstant());
        
        int USER_ID = Integer.valueOf(args[0]);
        //int USER_ID = Integer.valueOf(8308498);
        System.out.println(USER_ID);
        String[] path = new String[] {"response", "uid"};
        int tryConnect = 10;
        //VK_XMLParser parser = new VK_XMLParser(new Connection(USER_ID, ".xml", tryConnect).getXML(), path);
        Processor processor = new Processor(USER_ID, 2);
        processor.start(USER_ID);
        //new VK_JSONParser(new Connection(USER_ID, "", 10).getJSON());
    }
    
}
