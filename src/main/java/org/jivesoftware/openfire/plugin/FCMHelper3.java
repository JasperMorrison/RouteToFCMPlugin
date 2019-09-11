package org.jivesoftware.openfire.plugin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.AndroidConfig;
import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;
import com.google.auth.http.HttpTransportFactory;
import com.google.api.client.http.HttpTransport;
import java.net.InetSocketAddress;
import java.net.Proxy;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.firebase.messaging.Notification;

public class FCMHelper3 {
    private FCMHelper3() {
        initFirebase();
        System.out.println(System.getProperty("user.dir"));
    }
    
    public void sendMsg(org.xmpp.packet.Message msg) {
        String registrationToken = new SecurityUtils().getToken();
        AndroidConfig ac = AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.NORMAL)
            .build();

        String from = msg.getFrom().toString();
        String body = msg.getBody();
        
        // See documentation on defining a message payload.
        Message message = Message.builder()
            .setNotification(new Notification(from, body))
            .putData("title", from)
            .putData("body", body)
            .setToken(registrationToken)  /** CHOOSE EITHER seToken (For Sending to single device) or setTopic method **/
            //.setTopic("Test")
            .setAndroidConfig(ac)
            .build();
        System.out.println("message built");

        try{
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            System.out.println("Successfully sent message: " + response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static FCMHelper3 instance = null;
    
    public static FCMHelper3 getInstance() {
        if (instance == null) instance = new FCMHelper3();
        return instance;
    }
    
    private void initFirebase() {
        try{
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(MyUtils.PROXY_IP, MyUtils.PROXY_PORT));
            HttpTransport httpTransport = new NetHttpTransport.Builder().setProxy(proxy).build();
            HttpTransportFactory httpTransportFactory = () -> httpTransport;

            String project_id = MyUtils.PROJECT_ID;
            FileInputStream serviceAccount = new FileInputStream(MyUtils.getServiceAccount());
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount, httpTransportFactory))
                .setDatabaseUrl(MyUtils.DATA_BASE_URL)
                .setHttpTransport(httpTransport)
                .setProjectId(project_id)
                .build();
            FirebaseApp.initializeApp(options);
        }catch(Exception e){
            e.printStackTrace();
        }
            
        System.out.println("initialized");
    }

}