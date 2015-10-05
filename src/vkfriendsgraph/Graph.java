/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Graph implements Serializable {
    
    private ArrayList<Object[]> mainGraph = new ArrayList<>();
    private ArrayList<Object[]> index = new ArrayList<>();
    private int reads = 0, indexReads = 0;
    
    public Graph() {
        
    }
    
    /**
     * Помещает объект в указанное место
     * @param user - объект User
     * @param level - указывает уровень помещения
     * @param path - путь помещения - массив ID пользователей
     * @return возвращает путь к пользователю. Может быть изменён, 
     * если пользователь уже был помещён ранее
     */
    public int[] put(User user, int level, int[] path) {
        ArrayList<Object[]> friendsList = getListByLevel(level, this.mainGraph, path);
        if (!checkIndex(user.getUserId(), level, path)){
            ArrayList<Object[]> newEmptyList = new ArrayList<>();
            Object[] uObj = {user, newEmptyList};
            friendsList.add(uObj);
            addIndex(user.getUserId(), level, path);
            showReads();
            return path;
        }
        else {
            Object[] reqUser = (Object[])getIndex(user.getUserId());
            if(((int[])reqUser[2]).length == 0) {
                friendsList.add(mainGraph.get(0));
            }
            else {
                ArrayList<Object[]> reqFriendsList = getListByLevel((int)reqUser[1], this.mainGraph, (int[])reqUser[2]);
                for(int i = 0; i < reqFriendsList.size(); i++, reads++) {
                    if(((int)reqUser[0]) == ((User)reqFriendsList.get(i)[0]).getUserId()) {
                        friendsList.add(reqFriendsList.get(i));
                        //ArrayList<Object[]> newEmptyList = new ArrayList<>();
                        //Object[] uObj = {user, newEmptyList};
                        //friendsList.add(uObj);
                        showReads();
                        return (int[])reqUser[2];
                    }
                }
            }
            showReads();
            return (int[])reqUser[2];
        }
    }
    
    /**
     * Выводит количество чтений массивов (debug)
     */
    private void showReads() {
        System.out.println("Index reads: \t" + indexReads + ". \tTotal reads: \t" + reads);
        indexReads = reads = 0;
    }
    
    /**
     * Рекурсивный метод.
     * Возвращает массив пользователей, куда требуется поместить новый элемент
     * по указанному пути
     * @param level - уровень поиска
     * @param list - массив поиска
     * @param path - путь поиска
     * @return искомый массив
     */
    private ArrayList<Object[]> getListByLevel(int level, ArrayList<Object[]> list, int[] path) {
        for (int i = 0; i <= level; i++) {
            reads++;
            if (i == level) {
                return list;
            }
            else {
                for(int j = 0; j < list.size(); j++) {
                    reads++;
                    if(((User)list.get(j)[0]).getUserId() == path[0]) {
                        if (path.length == 1) {
                            return (ArrayList<Object[]>) list.get(j)[1];
                        }
                        else {
                            int[] newPath = new int[path.length - 1];
                            for(int p = 0; p < path.length - 1; p++) {
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
    
    /**
     * Возвращает массив, который хранит граф.
     * @return массив объектов User
     */
    public ArrayList<Object[]> getArrayGraph() {
        return this.mainGraph;
    }

    /**
     * Проверяет наличие пользователя в индексной таблице.
     * Предназначен для реализации замыкания графа
     * @param userId - ID пользователя
     * @param level - уровень поиска
     * @param path - путь
     * @return true, если пользователь был найден; false - если нет
     */
    private boolean checkIndex(int userId, int level, int[] path) {
        for (int i = 0; i < index.size(); i ++) {
            reads++;
            indexReads++;
            if (userId == (int)index.get(i)[0])
                return true;
        }
        return false;
    }
    
    /**
     * Добавляет нового пользователя в индексную таблицу
     * @param userId - ID пользователя
     * @param level - уровень
     * @param path - путь
     */
    private void addIndex(int userId, int level, int[] path) {
        Object[] ind = {userId, level, path};
        index.add(ind);
        //System.out.println("uid=" + userId);
        //System.out.println("level=" + level);
        //System.out.println("path=" + Arrays.toString(path));
    }
    
    /**
     * Возвращает строку в индексной таблице по ID пользователя
     * @param userId - ID пользователя
     * @return массив объектов строки (UserID, level, path)
     */
    private Object[] getIndex(int userId) {
        for(int i = 0; i < index.size(); i++) {
            if((int)index.get(i)[0] == userId) {
                return index.get(i);
            }
        }
        return null;
    }
}
