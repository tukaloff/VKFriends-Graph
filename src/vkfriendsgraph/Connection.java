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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Source;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author tukaloff
 */
public class Connection {
    
    private static final String TAG = Connection.class.getName();
    
    private String sQuery, sJSON, sXML;
    private Socket socket;
    private String sParams;
    private ArrayList<ArrayList<String>> params;
    private String answer;
    
    /**
     * Конструктор создаёт объект подключения с передаваемыми параметрами
     * и пробует послать GET-запрос
     * @param methosName - название вызываемого метода
     * @param UserID - ID пользователя
     * @param respType - тип ответа XML/JSON
     * @param tryOut - количество попыток подключения
     * @param params - массив пар поле-значения
     */
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
    
    public Connection() {
        try {
            sQuery = "https://oauth.vk.com/authorize?"
                    + "client_id=4772459&display=page&"
                    + "redirect_uri=https://oauth.vk.com/blank.html&"
                    + "scope=friends&response_type=token&v=5.40;";
            URL uri = new URL(sQuery);
            HttpsURLConnection https = (HttpsURLConnection) uri.openConnection();
            //System.out.println(https.getURL().toString());
            InputStream is = https.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            answer = "";
            while ((line = br.readLine()) != null) {
                answer += line;
        }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getAnswer() {
        return answer;
    }    
    
    
    /**
     * Формирует из массива с параметрами строку с параметрами
     */
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

    /**
     * Реализует GET-запрос
     * @param methodName - вызываемый в API метод
     * @param answerType - тип ответа
     * @param USER_ID - ID пользователя
     * @return true - успешно выполнен; false - выполнен с ошибкой
     */
    private boolean tryHTTPS_GET (String methodName, String answerType, long USER_ID) {
        try {
            //Генерируем строку запроса
            sQuery = "https://api.vk.com/method/" + methodName + answerType + "?" + sParams 
                    + "&access_token=" + Properties.getAccessToken();
            URL uri = new URL(sQuery);
            HttpsURLConnection https = (HttpsURLConnection) uri.openConnection();
            //System.out.println(https.getURL().toString());
            InputStream is = https.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            answer = "";
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
    
    /**
     * Возвращает объект JSON-парсера.
     * Не используется
     * @return объект JSON-парсера
     */
    public JSONParser getJSON() {
        JSONParser jSONParser = new JSONParser(new Source("Friends.Get", sJSON), null);
        return jSONParser;
    }
    
    /**
     * Возвращает XML-документ.
     * null - выполнилось с ошибкой
     * @return объект XML-документа
     */
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
