package com.jerry.jtakeaway.application;

import android.app.Application;

import com.jerry.jtakeaway.UncaughtExceptionHandler.JUncaughtExceptionHandler;

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
    }

    public static JApplication getContext() {
        return jApplication;
    }
}
