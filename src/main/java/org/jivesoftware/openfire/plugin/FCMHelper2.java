package org.jivesoftware.openfire.plugin;

import org.pixsee.fcm.Sender;

import org.pixsee.fcm.Message;

public class FCMHelper2 {
    private String mServerKey;
    private Sender mSender;
    private FCMHelper2(String serverKey) {
        this.mServerKey = serverKey;
        this.mSender = new Sender(mServerKey);
    }
    
    public void sendMsg(Message msg) {
        mSender.send(msg);
    }
    
    private static FCMHelper2 instance = null;
    
    public static FCMHelper2 getInstance(String serverKey) {
        if (instance == null) instance = new FCMHelper2(serverKey);
        return instance;
    }

}
/*
// build the message 
Message message = new Message.MessageBuilder()
    .toToken(toClientToken) // single android/ios device
    .addData("key_1","value_1")
    .build();
    
Notification notification = new Notification("title", "body");
notification.setIcon("icon");
notification.setSound("sound");
notification.setBadge("badge");
notification.setClickAction("click.action");
Message message = messageBuilder.notification(notification).build(); // add notification
*/