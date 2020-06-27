package com.jerry.jtakeaway.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.utils.MMkvUtil;

import java.util.Objects;

public class Notifications {



    public static void sendNormalNotification(Context context,String title,String content,Intent intent,int index){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int tag = MMkvUtil.getInstance(context,"Configuration").decodeInt("NoticeStyle");
        Notification.Builder builder = null;
        switch(tag){
            case 0:
                //默认
                builder = Default(context,title,content,intent);
                break;
            case 1:
                //清新
                builder = Fresh(context,title,content,intent);
                break;
            case 2:
                //黑夜
                builder = Default(context,title,content,intent);
                break;
            case 3:
                //白昼
                builder = Default(context,title,content,intent);
                break;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Objects.requireNonNull(builder).setChannelId(NotificationChannels.SYSTEM_ID);
        }
        Objects.requireNonNull(notificationManager).notify(index, Objects.requireNonNull(builder).build());
    }

    private static Notification.Builder Default(Context context,String title,String content,Intent intent){
        Notification.Builder builder = new Notification.Builder(context).setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo_app);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        //关闭通知  通知意图
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        builder.setContentIntent(pendingIntent);
        return builder;
    }

    private static Notification.Builder Fresh(Context context,String title,String content,Intent intent){
        RemoteViews contentView=new RemoteViews(context.getPackageName(), R.layout.fresh_notice);
//        contentView.setViewVisibility(R.id.notification_background, View.GONE);
//        contentView.setViewVisibility(R.id.background, View.VISIBLE);

        contentView.setTextViewText(R.id.title,title);
        contentView.setTextViewText(R.id.content,content);

        Notification.Builder builder = new Notification.Builder(context).setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        builder.setSmallIcon(R.drawable.logo_app);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(contentView);
        }else{
            builder.setContent(contentView);
        }
        //关闭通知  通知意图
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        builder.setContentIntent(pendingIntent);
        return builder;
    }


}
