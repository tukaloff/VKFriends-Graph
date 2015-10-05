/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

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
        /*
        System.out.println(new GregorianCalendar().toInstant());
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(new GregorianCalendar().toInstant());
        */
        int USER_ID = Integer.valueOf(args[0]);
        //int USER_ID = Integer.valueOf(1039324);//208);//88374578);//8308498);
        System.out.println(USER_ID);
        String[] path;
        path = new String[] {"response", "uid"};
        int tryConnect = 10;
        Processor processor = new Processor(USER_ID, 2, 0);
        processor.start(USER_ID);
    }
    
}
