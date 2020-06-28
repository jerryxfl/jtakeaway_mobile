package com.jerry.jtakeaway.BroderCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.utils.OkHttp3Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int MSGID = intent.getIntExtra("MSGID",-1);
        Log.i("TAG", "userClick:我被点击啦！！！ "+MSGID);
        if(MSGID!=-1){
            setMsgAlreadyRead(context,MSGID);
        }
    }


    public void setMsgAlreadyRead(Context context,int id){
        OkHttp3Util.GET(JUrl.msg_red(id), context, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
            }
        });
    }
}
