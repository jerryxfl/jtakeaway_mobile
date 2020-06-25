package com.jerry.jtakeaway.Notification;

import android.content.Context;
import android.net.Uri;

import com.jerry.jtakeaway.R;

import java.util.HashMap;
import java.util.Map;

public class NotificationAudios {
    public static final String AUDIO_1 = "iphone通知提示音";
    public static final String AUDIO_2 = "蜂鸣";
    public static final String AUDIO_3 = "比u比u";
    public static final String AUDIO_4 = "敲击";
    public static final String AUDIO_5= "iPhone提示音2";
    public static final String AUDIO_6 = "iPhone提示音3";
    public static final String AUDIO_7 = "iPhone提示音4";
    public static final String AUDIO_8 = "iPhone通知提示音2";
    public static final String AUDIO_9 = "通知提示音";

    private static NotificationAudios instance;
    public static Map<String, Uri> wowsMap = new HashMap<>();

    public static synchronized NotificationAudios getInstance() {
        if (instance == null){
            instance = new NotificationAudios();
        }
        return instance;
    }


    public void init(Context context) {
        wowsMap.put("iphone通知提示音",Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphone));
        wowsMap.put("蜂鸣",Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.animal_say));
        wowsMap.put("比u比u",Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.biu));
        wowsMap.put("敲击",Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.click));
        wowsMap.put("iPhone提示音2",Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphone3));
        wowsMap.put("iPhone提示音3",Uri.parse("android.ressource://" + context.getPackageName() + "/" + R.raw.iphone_long));
        wowsMap.put("iPhone提示音4",Uri.parse("android.ressource://" + context.getPackageName() + "/" + R.raw.iphonedd));
        wowsMap.put("iPhone通知提示音2",Uri.parse("android.ressource://" + context.getPackageName() + "/" + R.raw.iphonenotification));
        wowsMap.put("通知提示音",Uri.parse("android.ressource://" + context.getPackageName() + "/" + R.raw.notification));
    }

    public Uri getAudioUri(String audio){
        return wowsMap.get(audio);
    }



}
