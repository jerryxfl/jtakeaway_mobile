package com.jerry.jtakeaway.Notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;

import com.jerry.jtakeaway.utils.MMkvUtil;

public class NotificationChannels {
    public final static String SYSTEM_ID = "system";
    private final static String SYSTEM = "系统消息";




    public static void createAllNotificationChannels(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager.deleteNotificationChannel(SYSTEM_ID);
            AudioAttributes  att = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build();

            @SuppressLint("WrongConstant") NotificationChannel systemChannel = new NotificationChannel(
                    SYSTEM_ID,
                    SYSTEM,
                    NotificationManager.IMPORTANCE_MAX);
            systemChannel.enableLights(true);
            systemChannel.setSound(NotificationAudios.getInstance().getAudioUri(MMkvUtil.getInstance("Configuration").decodeString("Audio")),att);
            systemChannel.setShowBadge(true);
            systemChannel.canBypassDnd();
            systemChannel.setBypassDnd(true);
            systemChannel.enableVibration(true);
            systemChannel.setVibrationPattern(new long[]{100, 200});//设置震动频率


            notificationManager.createNotificationChannel(systemChannel);
        }
    }



}
