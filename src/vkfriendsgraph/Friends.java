/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.File;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author tukaloff
 */
public class Friends implements VKObject{
    
    private int userId;
    private ArrayList<ArrayList<String>> params;
    private Node xml;
    private ArrayList<User> friends;
    private final String ORDER_HINTS = "hints";
    private int count;
    private boolean isError;
    
    /**
     * Конструктор Подготавливает параметры для GET-запроса к API,
     * получает и парсит XML-документ
     * @param userId - ID пользователя, для которого нужно вернуть 
     * список друзей
     * @param count - количество возвращаемых друзей
     */
    public Friends(int userId, int count) {
        isError = false;
        this.userId = userId;
        this.count = count;
        prepairParams();
        Connection connection = new Connection(METHOD_FRIENDS_GET, userId, FORMAT_XML, 10, params);
        xml = connection.getXML().getFirstChild();
        //System.out.println(xml.getTextContent());
        try {
            if (xml.getNodeName().equals("error")) {
                isError = true;
                return;
            }
            parse(xml, 2);
        } catch (Exception ex) {
            System.out.println(xml);
            System.out.println(ex.getMessage());
        }
        //saveXml();
    }
    
    public boolean isError() {
        return isError;
    }

    /**
     * Реализует метод интерфейса.
     * Парсит XML. Сохраняет в объекте класса 
     * массив с объектами User
     * @param node - XML-ветка
     * @param parent - идентификатор вызова. Не используется
     */
    @Override
    public void parse(Node node, int parent) {
        friends = new ArrayList<>();
        NodeList users = xml.getChildNodes();
        for(int i = 0; i < users.getLength(); i++) {
            if (users.item(i).getNodeName() != "#text")
                friends.add(new User((Node)users.item(i)));
        }
    }
    
    /**
     * Возвращает массив пользователей - друзей указанного пользователя
     * @return Массив User
     */
    public ArrayList<User> getArrayOfUsers() {
        return friends;
    }

    /**
     * Реализует метод интерфейса.
     * Подготавливает параметры для GET-запроса
     * в массив поле-значения
     */
    @Override
    public void prepairParams() throws IndexOutOfBoundsException {
        ArrayList<String> alUser_id = new ArrayList<>();
        alUser_id.add("user_id");
        alUser_id.add(String.valueOf(userId));
        ArrayList<String> alOrder = new ArrayList<>();
        alOrder.add("order");
        alOrder.add(ORDER_HINTS);
        ArrayList<String> alCount = new ArrayList<>();
        alCount.add("count");
        alCount.add(String.valueOf(count));
        ArrayList<String> alFields = new ArrayList<>();
        alFields.add("fields");
        alFields.add("online");
        alFields.add("last_seen");
        alFields.add("photo_50");
        alFields.add("photo_200");
        alFields.add("photo_200_orig");
        params = new ArrayList<>();
        params.add(alUser_id);
        params.add(alOrder);
        if (count != 0)
            params.add(alCount);
        params.add(alFields);
    }

    /**
     * Реализует метод интерфейса.
     * Сохраняет XML-документ в файл в папке /friends
     */
    @Override
    public void saveXml() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File("friends/friendsOfuser_" + userId + ".xml"));
            transformer.transform(source, result);
            //System.out.println("Saved: friendsOfuser_" + userId + ".xml");
        } catch (TransformerConfigurationException ex) {
            System.out.println(ex.getMessage());
        } catch (TransformerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Реализует метод интерфейса.
     * Нет реализации
     * @return 
     */
    @Override
    public Document loadXml() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
