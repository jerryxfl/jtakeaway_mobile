package com.jerry.jtakeaway.application;

import android.app.Application;


import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class JApplication extends Application {
    FlutterEngine flutterEngine;
    @Override
    public void onCreate() {
        super.onCreate();
        flutterEngine = new FlutterEngine(this);
//        flutterEngine.getNavigationChannel().setInitialRoute("/forgetPassword");//设置路由
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

        FlutterEngineCache
                .getInstance()
                .put("jEngine", flutterEngine);
    }
}
