package com.jerry.jtakeaway.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.ui.user.activity.HomeActivity;

public class Notifications {


    public static void sendNormalNotification(Context context,String title,String content){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context).setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(NotificationChannels.SYSTEM_ID);
        }

//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.normal_notice);
//        remoteViews.setBitmap(R.id.headImg,"", BitmapFactory.decodeResource(context.getResources(),img));
//        remoteViews.setTextViewText(R.id.title,title);
//        remoteViews.setTextViewText(R.id.content,content);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setCustomContentView(remoteViews);
//        }else{
//            builder.setContent(remoteViews);
//        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo_app);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        //关闭通知  通知意图
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context, HomeActivity.class),0);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(1,builder.build());
    }


}
