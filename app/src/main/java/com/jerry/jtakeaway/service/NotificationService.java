package com.jerry.jtakeaway.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jerry.jtakeaway.Notification.Notifications;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEvent;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEventType;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.websocket.WebSocketClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class NotificationService extends Service {
    private WebSocketClient webSocketClient;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("通知服务启动#####################################################################################################");
        EventBus.getDefault().register(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void WebSocketEvent(WebSocketEvent event) throws InterruptedException, URISyntaxException {
        if(event.getEventType()==WebSocketEventType.OPEN){
            URI uri = new URI("ws://" + JUrl.host.replaceAll("http://","") + "connect?jwt=" + MMkvUtil.getInstance(this, "jwts").decodeString("jwt"));
            System.out.println("ws://" + JUrl.host.replaceAll("http://","") + "connect?jwt=" + MMkvUtil.getInstance(this, "jwts").decodeString("jwt"));
            webSocketClient = new WebSocketClient(uri, new WebSocketClient.webSocketListener() {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(Msg message) {
                    Notifications.sendNormalNotification(NotificationService.this,"系统信息",message.getContent());
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            });
            webSocketClient.connectBlocking();
        }else if(event.getEventType()==WebSocketEventType.CLOSE){
            if(webSocketClient!=null){
                webSocketClient.closeBlocking();
                webSocketClient = null;
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webSocketClient!=null) {
            try {
                webSocketClient.closeBlocking();
                webSocketClient = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
