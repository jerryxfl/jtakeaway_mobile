package com.jerry.jtakeaway.Notification;

import android.content.Context;
import android.net.Uri;

import com.jerry.jtakeaway.Notification.model.Audio;
import com.jerry.jtakeaway.R;

import java.util.ArrayList;
import java.util.List;

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
    public static List<Audio> audios = new ArrayList<>();

    public static synchronized NotificationAudios getInstance() {
        if (instance == null){
            instance = new NotificationAudios();
        }
        return instance;
    }


    public void init(Context context) {
        audios.add(new Audio(AUDIO_1,R.drawable.dribbble_music_corner,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphone),R.raw.iphone));
        audios.add(new Audio(AUDIO_2,R.drawable.concrete_road_between_trees_563356,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.animal_say),R.raw.animal_say));
        audios.add(new Audio(AUDIO_3,R.drawable.hot_art,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.biu),R.raw.biu));
        audios.add(new Audio(AUDIO_4,R.drawable.cu,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.click),R.raw.click));
        audios.add(new Audio(AUDIO_5,R.drawable.c6662b0de7365559f79d9eb6088d9527,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphone3),R.raw.iphone3));
        audios.add(new Audio(AUDIO_6,R.drawable.mp,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphone_long),R.raw.iphone_long));
        audios.add(new Audio(AUDIO_7,R.drawable.pof,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphonedd),R.raw.iphonedd));
        audios.add(new Audio(AUDIO_8,R.drawable.pom,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.iphonenotification),R.raw.iphonenotification));
        audios.add(new Audio(AUDIO_9,R.drawable.wrd,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification),R.raw.notification));
    }



    public Audio getAudio(String audioName){
        for(Audio audio :audios){
            if(audio.getName().equals(audioName))return audio;
        }
        return null;
    }



}
