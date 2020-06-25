package com.jerry.jtakeaway.application;

import android.app.Application;

import com.jerry.jtakeaway.Notification.NotificationAudios;
import com.jerry.jtakeaway.Notification.NotificationChannels;
import com.jerry.jtakeaway.UncaughtExceptionHandler.JUncaughtExceptionHandler;
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
        if(MMkvUtil.getInstance(this,"Configuration").decodeString("Audio")== null){
            MMkvUtil.getInstance(this,"Configuration").encode("Audio",NotificationAudios.AUDIO_1);
        }
        NotificationAudios.getInstance().init(this);
        NotificationChannels.createAllNotificationChannels(this);
    }

    public static JApplication getContext() {
        return jApplication;
    }


}
