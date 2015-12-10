/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import java.beans.EventHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javax.swing.event.ChangeListener;

/**
 *
 * @author user
 */
public class AutorizFXPanel extends JFXPanel{
    
    public AutorizFXPanel() {
        super();
    }

    public void initFX() {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        System.out.println("initFX");
        WebView web = new WebView();
        
        String page = "https://oauth.vk.com/authorize?"
            + "client_id=4772459&display=page&"
            + "redirect_uri=https://oauth.vk.com/blank.html&"
            + "scope=notify,friends,photos,audio,video,docs,notes,pages,status,offers,questions," 
            + "wall,groups,messages,email,notifications,stats,ads,offline&"
                + "response_type=token&v=5.40";

        WebEngine webEngine = web.getEngine();
        webEngine.load(page);
        webEngine.setOnStatusChanged(new javafx.event.EventHandler<WebEvent<String>>() {

            @Override
            public void handle(WebEvent<String> event) {
                if (event.getSource() instanceof WebEngine) {
                    WebEngine we = (WebEngine) event.getSource();
                    String location = we.getLocation();
                    if (location.startsWith("https://oauth.vk.com/blank.html") && location.contains("access_token")) {
                        try {
                            URL url = new URL(location);
                            String[] params = url.getRef().split("&");
                            Map<String, String> map = new HashMap<String, String>();
                            for (String param : params) {
                                String name = param.split("=")[0];
                                String value = param.split("=")[1];
                                map.put(name, value);
                            }
                            Properties.setAccesToken(map.get("access_token"));
                            Properties.setMyID(Double.valueOf(map.get("user_id")).intValue());
                            System.out.println(Properties.getMyID());
                            System.out.println("The access token: "+map.get("access_token"));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        root.getChildren().add(web);
        this.setScene(scene);
    }
}
