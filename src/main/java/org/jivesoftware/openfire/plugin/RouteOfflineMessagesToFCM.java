package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jivesoftware.openfire.OfflineMessageListener;
import org.jivesoftware.openfire.OfflineMessageStrategy;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

//import org.xmpp.packet.Message;
//import org.pixsee.fcm.Message;
import org.pixsee.fcm.Notification;

import com.google.gson.JsonObject;

public class RouteOfflineMessagesToFCM implements Component, Plugin, OfflineMessageListener {

	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private String serviceName;
	private static String serverName;
	private OfflineMessageStrategy strategy;

	private static final Logger Log = LoggerFactory.getLogger(RouteOfflineMessagesToFCM.class);
	public static final String SERVICENAME = "plugin.route.RouteOfflineMessagesToFCM";
    private static final String FCM_SERVER_KEY = "AIzaSyC***qO9udC7E";

	public RouteOfflineMessagesToFCM() {
		serverName = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
		strategy = XMPPServer.getInstance().getOfflineMessageStrategy();
	}

	@Override
	public void messageBounced(org.xmpp.packet.Message arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageStored(org.xmpp.packet.Message arg0) {
        Log.debug("get offline message");
        if(!checkForStored(arg0)){
            return;
        }
        /*
        * fcm3Send: use firebase-java-sdk to send fcm，test success
        * fcm2Send: use a git lib which using firebase-java-sdk, but do not support proxy ,send failed
        * fcmSend:  use old http post, test success
        */
        fcm3Send(arg0);
        //fcm2Send(arg0);
        //fcmSend(arg0);
	}
    
    private boolean checkForStored(org.xmpp.packet.Message msg) {
        org.xmpp.packet.Message.Type type = msg.getType();
        String body = msg.getBody();
        if(type == org.xmpp.packet.Message.Type.chat && body != null){
            System.out.println(msg.getType() + " to fcm");
            return true;
        }else {
            System.out.println(msg.getType() + " drop");
            return false;
        }
    }
    
    private void fcm3Send(org.xmpp.packet.Message msg) {
        FCMHelper3.getInstance().sendMsg(msg);
    }
    
    private void fcm2Send(org.xmpp.packet.Message arg0) {
        String toClientToken = new SecurityUtils().getToken();
        Notification notification = new Notification("my_title", "my_body");
        org.pixsee.fcm.Message message = new org.pixsee.fcm.Message.MessageBuilder()
                .toToken(toClientToken) // single android/ios device
                .notification(notification)
                .addData("key_1","value_1")
                .build();

        FCMHelper2.getInstance(new SecurityUtils().getFcmServerKey()).sendMsg(message);
    }
    
    private void fcmSend(org.xmpp.packet.Message arg0) {
        JsonObject dataObject = new JsonObject();
		dataObject.addProperty("psID", arg0.getFrom().toString());
		dataObject.addProperty("description", arg0.getBody());
		dataObject.addProperty("category", "chat");
		JsonObject notificationObject = new JsonObject();
		notificationObject.addProperty("title", arg0.getFrom().toString());
        notificationObject.addProperty("body", arg0.getBody().toString());
        
		try {
			FCMHelper.getInstance().sendNotifictaionAndData(FCMHelper.TYPE_TO, new SecurityUtils().getToken(),
					notificationObject, dataObject);
			//AWSHelper.getInstance().sendAWSMessage(dataObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		strategy.removeListener(this);
		pluginManager = null;
		try {
			componentManager.removeComponent(serviceName);
			componentManager = null;
		} catch (Exception e) {
			if (componentManager != null) {
				Log.error(e.getMessage(), e);
			}
		}
		serviceName = null;
		serverName = null;
	}

	@Override
	public void initializePlugin(PluginManager manager, File arg1) {
		// TODO Auto-generated method stub
		pluginManager = manager;
        serviceName = SERVICENAME;

		componentManager = ComponentManagerFactory.getComponentManager();
		try {
			componentManager.addComponent(serviceName, this);
		} catch (ComponentException e) {
			Log.error(e.getMessage(), e);
		}
		strategy.addListener(this);

	}

	@Override
	public void initialize(JID arg0, ComponentManager arg1) throws ComponentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xmpp.component.Component#getName()
	 */
	public String getName() {
		return pluginManager.getName(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xmpp.component.Component#getDescription()
	 */
	public String getDescription() {
		return pluginManager.getDescription(this);
	}

}
