/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Source;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author user
 */
public class Connection {
    
    private static final String TAG = Connection.class.getName();
    
    private String sQuery, sJSON, sXML;
    private Socket socket;
    private String sParams;
    private ArrayList<ArrayList<String>> params;
    
    public Connection(String methosName, int UserID, String respType, int tryOut, ArrayList<ArrayList<String>> params) {
        this.params = params;
        paramsToString();
        for (int i = 0; i < tryOut; i++) {
            //System.out.println(new GregorianCalendar().toInstant() + " Try: " + i);
            if (tryHTTPS_GET(methosName, respType, UserID)) {
                //System.out.println(UserID);
                return;
            }
        }        
    }
    
    private void paramsToString() {
        sParams = "";
        for (int i = 0; i < params.size(); i++) {
            for (int j = 0; j < params.get(i).size(); j++) {
                if (j == 0) {
                    sParams += params.get(i).get(j) + "=";
                } else {
                    if ((j + 1) != params.get(i).size()) {
                        sParams += params.get(i).get(j) + ",";
                    } else {
                        sParams += params.get(i).get(j);
                    }
                }
            }
            if ((i + 1) != params.size()) {
                sParams += "&";
            }
        }
    }

    private boolean tryHTTPS_GET (String methodName, String answerType, long USER_ID) {
        try {
            //Генерируем строку запроса
            sQuery = "https://api.vk.com/method/" + methodName + answerType + "?" + sParams;
            URL uri = new URL(sQuery);
            HttpsURLConnection https = (HttpsURLConnection) uri.openConnection();
            //System.out.println(https.getURL().toString());
            InputStream is = https.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = "", answer = "";
            while ((line = br.readLine()) != null) {
                answer += line;
            }
            //получаем и сохраняем ответ от сервера
            //System.out.println(answer);
            if (answerType == "")
                sJSON = answer;
            else
                sXML = answer;
            //System.out.println(answer);
            return true;
        } catch (MalformedURLException ex) {
            System.out.println(TAG + ": " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println(TAG + ": " + ex.getMessage());
        }
        return false;
    }
    
    public JSONParser getJSON() {
        JSONParser jSONParser = new JSONParser(new Source("Friends.Get", sJSON), null);
        return jSONParser;
    }
    
    public Document getXML() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new ByteArrayInputStream(sXML.getBytes()));
        } catch (ParserConfigurationException ex) {
            System.out.println(TAG + ": " + ex.getMessage());
        } catch (SAXException ex) {
            System.out.println(TAG + ": " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println(TAG + ": " + ex.getMessage());
        }
        return null;
    }    
}
