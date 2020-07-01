package com.jerry.jtakeaway.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.jerry.jtakeaway.utils.InitApp;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    private String[] GPermissions;
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog mDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 设置全屏显示
         */
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(getLayout());
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        /**
         * 设置全屏显示结束
         */
        //绑定bufferknife注解框架
        unbinder = ButterKnife.bind(this);
        InitApp.getInstance().addActivity(this);

        InitView();
        InitData();
        InitListener();
    }

    public abstract int getLayout();

    public abstract void InitView();

    public abstract void InitData();

    public abstract void InitListener();

    public abstract void destroy();

    //绑定eventbus
    protected void SignEventBus(){//注册事件分发器
        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            InitApp.getInstance().removeActivity(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        if (unbinder != null && unbinder != Unbinder.EMPTY) {//取消注解绑定
            unbinder.unbind();
            unbinder = null;
        }
        if(EventBus.getDefault().isRegistered(this)){//注销事件分发器
            EventBus.getDefault().unregister(this);
        }
        InitApp.getInstance().removeActivity(this);
        destroy();
    }
}
