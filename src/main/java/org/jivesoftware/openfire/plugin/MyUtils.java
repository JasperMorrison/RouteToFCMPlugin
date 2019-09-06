package org.jivesoftware.openfire.plugin;

public class MyUtils {
    public static final String PROXY_IP = "10.16.13.18";
    public static final int PROXY_PORT = 8080;
    public static final String DATA_BASE_URL = "https://fcmtest-2c983.firebaseio.com";
    public static final String PROJECT_ID = "fcmtest-2c983";
    public static String getServiceAccount(){
        return System.getProperty("user.dir") + "\\distribution\\target\\distribution-base\\resources\\offlinefcm\\" + "fcmtest-2c983-firebase-adminsdk-laqei-72833c8271.json";
    }
}