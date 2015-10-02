/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Graph {
    
    private ArrayList<Object[]> mainGraph = new ArrayList<>();
    private ArrayList<Object[]> index = new ArrayList<>();
    
    public Graph() {
        
    }
    
    public int[] put(User user, int level, int[] path) {
        ArrayList<Object[]> friendsList = getListByLevel(level, this.mainGraph, path);
        if (!checkIndex(user.getUserId(), level, path)){
            ArrayList<Object[]> newEmptyList = new ArrayList<>();
            Object[] uObj = {user, newEmptyList};
            friendsList.add(uObj);
            return path;
        }
        else {
            Object[] reqUser = (Object[])getIndex(user.getUserId());
            if(((int[])reqUser[2]).length == 0) {
                friendsList.add(mainGraph.get(0));
            }
            else {
                ArrayList<Object[]> reqFriendsList = getListByLevel((int)reqUser[1], this.mainGraph, (int[])reqUser[2]);
                for(int i = 0; i < reqFriendsList.size(); i++) {
                    if(((int)reqUser[0]) == ((User)reqFriendsList.get(i)[0]).getUserId())
                        friendsList.add(reqFriendsList.get(i));
                }
            }
            return (int[])reqUser[2];
        }
    }
    
    private ArrayList<Object[]> getListByLevel(int level, ArrayList<Object[]> list, int[] path) {
        for (int i = 0; i <= level; i++) {
            if (i == level) {
                return list;
            }
            else {
                for(int j = 0; j < list.size(); j++) {
                    if(((User)list.get(j)[0]).getUserId() == path[0]) {
                        if (path.length == 1) {
                            return (ArrayList<Object[]>) list.get(j)[1];
                        }
                        else {
                            int[] newPath = new int[path.length - 1];
                            for(int p = 0; p < path.length; p++) {
                                newPath[p] = path[p + 1];
                            }
                            return getListByLevel(level - 1, (ArrayList<Object[]>) list.get(j)[1], newPath);
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }
    
    public ArrayList<Object[]> getArrayGraph() {
        return this.mainGraph;
    }

    private boolean checkIndex(int userId, int level, int[] path) {
        for (int i = 0; i < index.size(); i ++) {
            if (userId == (int)index.get(i)[0])
                return true;
        }
        return false;
    }
    
    private void addIndex(int userId, int level, int[] path) {
        Object[] ind = {userId, level, path};
        index.add(ind);
    }
    
    private Object[] getIndex(int userId) {
        for(int i = 0; i < index.size(); i++) {
            if((int)index.get(i)[0] == userId) {
                return index.get(i);
            }
        }
        return null;
    }
}
