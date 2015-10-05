/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author user
 */
class VK_XMLParser {
    
    private Node resultNode;
    private ArrayList<Node> resultArray = new ArrayList<>();
    private ArrayList<String[]> sArray = new ArrayList<>();

    /**
     * Конструктор для XML-документа
     * @param xml - XML-документ
     * @param path - путь к возвращаемому значению
     */
    public VK_XMLParser(Document xml, String[] path) {
        readRecursive(xml.getChildNodes(), path);
    }
    
    /**
     * Конструктор для XML-ветки
     * @param xml - XML-ветка
     * @param path - путь к возвращаемому значению
     */
    public VK_XMLParser(Node node, String[] path) throws NullPointerException{
        readRecursive(node.getChildNodes(), path);
    }
    
    /**
     * Получает сохранённое значение из массива
     * @param position - позиция элемента в массиве
     * @return объект String
     */
    public String getValue(int position) {
        return sArray.get(position)[1];
        //return resultArray.get(position).getNodeValue();
    }
    
    /**
     * Возвращает величину массива созранённых значений
     * @return длина массива
     */
    public int getLenght() {
        return sArray.size();
    }
    
    /**
     * Рекурсивный метод.
     * Читает XML-дерево, находит искомую ветку и сохраняет значение
     * @param childNodes - XML-ветка поиска
     * @param path - путь поиска
     */
    private void readRecursive(NodeList childNodes, String[] path) {
        for (int i = 0; i < path.length; i++) {
            if (i + 1 == path.length) {
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node lastNode = (Node)childNodes.item(j);
                    String name = lastNode.getNodeName();
                    //System.out.println(name);
                    if (lastNode.getNodeName() == path[i]) {
                        String value = lastNode.getTextContent();
                        //System.out.println(value);
                        sArray.add(new String[]{name, value});
                    };
                }
            } else {
                for (int j = 0; j < childNodes.getLength(); j++) {
                    String nodeName = childNodes.item(j).getNodeName();
                    //System.out.println(nodeName + "/" + path[i]);
                    if (nodeName == path[i]) {
                        String[] innerPath = new String[path.length - 1];
                        for (int p = 1; p < path.length; p++) {
                            innerPath[p - 1] = path[p];
                        }
                        readRecursive(childNodes.item(j).getChildNodes(), innerPath);
                    }
                }
            }
        }
    }
    
    /**
     * Метод поиска значения в XML-документе
     * @deprecated Не работает
     * @param xml XML-документ
     */
    public void failVK_XMLParser(Document xml) {
        System.out.println(xml.getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
        ArrayList<Object[]> parsingArray = new ArrayList<>();
        parsingArray.add(new Object[]{"root", xml.getChildNodes()});
        //parsingArray.add();
        //for (Object paElement : parsingArray) {
        for (int a = 0; a < parsingArray.size(); a++) {
            Object paElement = parsingArray.get(a)[1];
            //System.out.println(paElement.getClass().getName());
            if (paElement.getClass().getName() == "com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl"
                    || paElement.getClass().getName() == "com.sun.org.apache.xerces.internal.dom.DeferredElementImpl"
                    || paElement.getClass().getName() == "NodeList") {
                for (int i = 0; i < ((NodeList)paElement).getLength(); i++) {
                    Object[] element = new Object[2];
                    //ArrayList<Object> element = new ArrayList<>();
                    element[0] = ((NodeList)paElement).item(i).getNodeName();
                    if (String.valueOf(((String)element[0]).charAt(0)) != "#") {
                        try {
                            element[1] = ((NodeList)paElement).item(i).getChildNodes();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            element[1] = ((NodeList)paElement).item(i).getNodeValue();
                        }
                        parsingArray.add(element);
                    }
                }
            }
            ArrayList<Object> element = new ArrayList<>();
        }
    }
}
