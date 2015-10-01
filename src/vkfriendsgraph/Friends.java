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
 * @author user
 */
public class Friends implements VKObject{
    
    private int userId;
    private ArrayList<ArrayList<String>> params;
    private Node xml;
    private ArrayList<User> friends;
    
    public Friends(int userId) {
        this.userId = userId;
        prepairParams();
        Connection connection = new Connection(METHOD_FRIENDS_GET, userId, FORMAT_XML, 10, params);
        xml = connection.getXML().getFirstChild();
        //System.out.println(xml.getTextContent());
        try {
            parse(xml, 2);
        } catch (Exception ex) {
            System.out.println(xml);
            System.out.println(ex.getMessage());
        }
        saveXml();
    }

    @Override
    public void parse(Node node, int parent) {
        friends = new ArrayList<>();
        NodeList users = xml.getChildNodes();
        for(int i = 0; i < users.getLength(); i++) {
            if (users.item(i).getNodeName() != "#text")
                friends.add(new User((Node)users.item(i)));
        }
    }
    
    public ArrayList<User> getArrayOfUsers() {
        return friends;
    }

    @Override
    public void prepairParams() throws IndexOutOfBoundsException {
        ArrayList<String> alUser_id = new ArrayList<>();
        alUser_id.add("user_id");
        alUser_id.add(String.valueOf(userId));
        ArrayList<String> alOrder = new ArrayList<>();
        alOrder.add("order");
        //alOrder.add(ORDER_HINTS);
        ArrayList<String> alCount = new ArrayList<>();
        alCount.add("count");
        //alCount.add(String.valueOf(count));
        ArrayList<String> alFields = new ArrayList<>();
        alFields.add("fields");
        alFields.add("online");
        alFields.add("last_seen");
        alFields.add("photo_200");
        alFields.add("photo_200_orig");
        params = new ArrayList<>();
        params.add(alUser_id);
        //params.add(alOrder);
        //params.add(alCount);
        params.add(alFields);
    }

    @Override
    public void saveXml() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File("friends/friendsOfuser_" + userId + ".xml"));
            transformer.transform(source, result);
            System.out.println("Saved: friendsOfuser_" + userId + ".xml");
        } catch (TransformerConfigurationException ex) {
            System.out.println(ex.getMessage());
        } catch (TransformerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Document loadXml() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
