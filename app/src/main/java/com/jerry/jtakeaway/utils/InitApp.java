package com.jerry.jtakeaway.utils;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InitApp {
    private Set<Activity> activities;//保存所有界面
    private static InitApp instance = new InitApp();
    public static InitApp getInstance() {
        return instance;
    }
    public void addActivity(Activity activity){
        if(activities == null) {
            activities = new HashSet<>();
        }
        activities.add(activity);
        System.out.println("添加窗口"+activities.size());
    }

    public void removeActivity(Activity activity){
        if(activities!=null){
               if(activities.size()==1){
                   finish(activity);
               }else{
                   activities.remove(activity);
                   activity.finish();
                   System.out.println("移除窗口"+activities.size());
               }
        }
    }


    private void finish(Activity activity) {
        System.out.println("显示退出窗口");
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("你确定要退出吗?")
                .setConfirmText("是的")
                .setConfirmClickListener(sDialog -> {
                    System.exit(0);
                })
                .setCancelText("不了")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();

    }
}
