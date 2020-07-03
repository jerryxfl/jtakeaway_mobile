package com.jerry.jtakeaway.application;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.jerry.jtakeaway.Notification.NoticeStyles;
import com.jerry.jtakeaway.Notification.NotificationAudios;
import com.jerry.jtakeaway.Notification.NotificationChannels;
import com.jerry.jtakeaway.UncaughtExceptionHandler.JUncaughtExceptionHandler;
import com.jerry.jtakeaway.service.NotificationService;
import com.jerry.jtakeaway.utils.MMkvUtil;

public class JApplication extends Application {
    private static JApplication jApplication;

    //    FlutterEngine flutterEngine;
    @Override
    public void onCreate() {
        super.onCreate();
//        flutterEngine = new FlutterEngine(this);
////        flutterEngine.getNavigationChannel().setInitialRoute("/forgetPassword");//设置路由
//        flutterEngine.getDartExecutor().executeDartEntrypoint(
//                DartExecutor.DartEntrypoint.createDefault()
//        );

//        FlutterEngineCache
//                .getInstance()
//                .put("jEngine", flutterEngine);
//        SDKInitializer.initialize(this);
//        SDKInitializer.setCoordType(CoordType.BD09LL);
        //异常捕获 防止崩溃

        jApplication =this;
        JUncaughtExceptionHandler.getInstance().init(getApplicationContext());
        if(MMkvUtil.getInstance(this,"Configuration").decodeString("GUIDE")== null){
            MMkvUtil.getInstance(this,"Configuration").encode("GUIDE",0);
        }

        if(MMkvUtil.getInstance(this,"Configuration").decodeString("Audio")== null){
            MMkvUtil.getInstance(this,"Configuration").encode("Audio",NotificationAudios.AUDIO_8);
        }

        NotificationAudios.getInstance().init(this);
        NoticeStyles.init();
        if(MMkvUtil.getInstance(this,"Configuration").decodeString("NoticeStyle")== null){
            MMkvUtil.getInstance(this,"Configuration").encode("NoticeStyle", 0);
        }

        NotificationChannels.createAllNotificationChannels(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(new Intent(this, NotificationService.class));
        }else{
            startService(new Intent(this, NotificationService.class));
        }
    }

    public static JApplication getContext() {
        return jApplication;
    }

}
