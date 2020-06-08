package com.jerry.jtakeaway.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;


import java.util.HashSet;
import java.util.Set;

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
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("提示");
        dialog.setMessage("是否退出当前程序？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("jerry", "___取消");
            }
        });
        if (dialog != null) {
            dialog.show();
        }
    }
}
