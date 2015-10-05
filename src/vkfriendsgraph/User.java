/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author user
 */
class User implements VKObject, Serializable {
    
    private int userId;
    private ArrayList<ArrayList<String>> params;
    private Node xml;
    private String firstName;
    private String lastName;
    private String deactivated;
    
    /**
     * Конструктор Подготавливает параметры для GET-запроса к API,
     * получает и парсит XML-документ
     * @param userId - ID пользователя
     */
    public User(int userId) {
        this.userId = userId;
        prepairParams();
        Connection connection = new Connection(METHOD_USER_GET, userId, FORMAT_XML, 10, params);
        xml = connection.getXML().getFirstChild().getLastChild();
        parse(xml, XML_PARENT_MAIN_USER);
        //saveXml();
    }
    
    /**
     * Возвращает имя пользователя
     * @return Имя + Фамилия 
     */
    public String getName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Конструктор создаёт объект класса на основе
     * XML-записи
     * @param node - XML-ветка
     */
    public User(Node node) {
        //System.out.println(node);
        xml = node;
        try {
            parse(node, XML_PARENT_FRIEND);
        } catch (Exception ex) {
            System.out.println(xml);
            System.out.println(ex.getMessage());
        }
        //saveXml();
    }
    
    /**
     * Реализует метод интерфейса.
     * Парсит XML
     * @param node - XML-ветка
     * @param parent - идентификатор вызова. Не используется
     * @throws IndexOutOfBoundsException - должен сообщить
     * об ошибке чтения массива, возвращаемого классом-обработчиком
     * XML-документа
     */
    @Override
    public void parse(Node node, int parent) throws IndexOutOfBoundsException {
        String[] path;
        if (parent == XML_PARENT_MAIN_USER) {
            
        }
        //System.out.println(node.getTextContent());
        VK_XMLParser xmlParser;
        path = new String[] {"user", "first_name"};
        xmlParser = new VK_XMLParser(node, path);
        firstName = xmlParser.getValue(0);
        
        path = new String[] {"user", "last_name"};
        xmlParser = new VK_XMLParser(node, path);
        lastName = xmlParser.getValue(0);
        
        path = new String[] {"user", "uid"};
        xmlParser = new VK_XMLParser(node, path);
        userId = Integer.valueOf(xmlParser.getValue(0));
        
        path = new String[] {"user", "deactivated"};
        xmlParser = new VK_XMLParser(node, path);
        if (xmlParser.getLenght() > 0)
            deactivated = xmlParser.getValue(0);
        else
            deactivated = "";
        //System.out.println(deactivated);
    }
    
    /**
     * Реализует метод интерфейса.
     * Подготавливает параметры для GET-запроса
     * в массив поле-значения
     */
    @Override
    public void prepairParams() {
        ArrayList<String> alUser_id = new ArrayList<String>();
        alUser_id.add("user_id");
        alUser_id.add(String.valueOf(userId));
        ArrayList<String> alFields = new ArrayList<String>();
        alFields.add("fields");
        alFields.add("photo_id");
        alFields.add("verified");
        alFields.add("blacklisted");
        alFields.add("sex");
        alFields.add("bdate");
        alFields.add("city");
        alFields.add("country");
        alFields.add("home_town");
        alFields.add("photo_50");
        alFields.add("photo_100");
        alFields.add("photo_200_orig");
        alFields.add("photo_200");
        alFields.add("photo_400_orig");
        alFields.add("photo_max");
        alFields.add("photo_max_orig");
        alFields.add("online");
        alFields.add("lists");
        alFields.add("domain");
        alFields.add("has_mobile");
        alFields.add("contacts");
        alFields.add("site");
        alFields.add("education");
        alFields.add("universities");
        alFields.add("schools");
        alFields.add("status");
        alFields.add("last_seen");
        alFields.add("followers_count");
        //alFields.add("common_count");
        alFields.add("counters");
        alFields.add("occupation");
        alFields.add("nickname");
        alFields.add("relatives");
        alFields.add("relation");
        alFields.add("personal");
        alFields.add("connections");
        alFields.add("exports");
        alFields.add("wall_comments");
        alFields.add("activities");
        alFields.add("interests");
        alFields.add("music");
        alFields.add("movies");
        alFields.add("tv");
        alFields.add("books");
        alFields.add("games");
        alFields.add("about");
        alFields.add("quotes");
        alFields.add("can_post");
        alFields.add("can_see_all_posts");
        alFields.add("can_see_audio");
        alFields.add("can_write_private_message");
        alFields.add("can_send_friend_request");
        alFields.add("is_favorite");
        alFields.add("timezone");
        alFields.add("screen_name");
        alFields.add("maiden_name");
        alFields.add("crop_photo");
        alFields.add("is_friend");
        alFields.add("friend_status");

        params = new ArrayList<ArrayList<String>>();
        params.add(alUser_id);
        params.add(alFields);
    }

    /**
     * Реализует метод интерфейса.
     * Сохраняет XML-документ в файл в папке /users
     */
    @Override
    public void saveXml() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File("users/user_" + userId + "(" + getName() + ").xml"));
            transformer.transform(source, result);
            //System.out.println("Saved: user_" + userId + "(" + getName() + ").xml");
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

    /**
     * Возвращает ID пользователя
     * @return UserID
     */
    int getUserId() {
        return userId;
    }
    
    /**
     * Возвращает значение поля <deactivated>
     * @return String - по-умолчанию ""
     */
    public String getDeactivated() {
        return deactivated;
    }
}
