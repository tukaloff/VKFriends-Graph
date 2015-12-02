/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author tukaloff
 */
public interface VKObject {
    
    static final String FORMAT_XML = ".xml";
    static final String FORMAT_JSON = "";
    static final String METHOD_FRIENDS_GET = "friends.get";
    static final String METHOD_USER_GET = "users.get";
    static final int XML_PARENT_MAIN_USER = 0;
    static final int XML_PARENT_FRIEND = 1;
    
    void parse(Node node, int parent) throws IndexOutOfBoundsException;
    void prepairParams();
    void saveXml();
    Document loadXml();
}
