package com.jerry.jtakeaway.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.jerry.jtakeaway.Notification.Notifications;
import com.jerry.jtakeaway.R;
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
import java.util.Objects;

public class NotificationService extends Service {
    private WebSocketClient webSocketClient;
    private static int index = 1;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        startNotification();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    @SuppressLint("WrongConstant")
    private void startNotification() {//开启通知栏提示
        NotificationManager mnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获得系统通知服务
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel("static", "Primary Channel", NotificationManager.IMPORTANCE_LOW);
            assert mnotificationManager != null;
            mnotificationManager.createNotificationChannel(chan1);
            builder = new NotificationCompat.Builder(getApplicationContext(), "static");
            builder.setChannelId(chan1.getId());
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_app)
                .setOngoing(true)
                .setContentTitle("疯狂外卖")
                .setContentText("保活策略")
                .setPriority(NotificationCompat.BADGE_ICON_LARGE)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .build();
        Notification notification = builder.build();
        Objects.requireNonNull(mnotificationManager).notify(0, notification);
        startForeground(1, notification);
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
                    System.out.println("消息id"+message.getId());
                    Notifications.sendNormalNotification(NotificationService.this,"系统信息",message.getContent(),index,message.getId());
                    index++;
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    EventBus.getDefault().post(new WebSocketEvent(WebSocketEventType.OPEN));
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
        EventBus.getDefault().unregister(this);
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
