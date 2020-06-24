package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jerry.jtakeaway.utils.PixAndDpUtil;


public class FloatView {
    private Context mContext;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View mView;
    private int  Y=0,X=0;
    private int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mAnimation = android.R.style.Animation_Toast;
    private boolean isDisplay = false;
    public FloatView(Context context) {
        this.mContext = context;
    }




    public void setView(View view){
        this.mView = view;
    }



    public void setGravity(int gravity){
        this.mGravity = gravity;
    }


    public void setXy(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public void setAnimation(int Animation){
        this.mAnimation = Animation;
    }

    public void build(){
        if(mView!=null){
            //获得悬浮窗服务
            windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            //
            layoutParams = new WindowManager.LayoutParams();
            //
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//若要显示输入法将这段注释掉
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            layoutParams.gravity = mGravity;
            layoutParams.x = X;
//            Display display = windowManager.getDefaultDisplay();
//            DisplayMetrics metrics = new DisplayMetrics();
//            display.getMetrics(metrics);
//            int screenheight = metrics.heightPixels;
            layoutParams.y =  PixAndDpUtil.dip2px(mContext,Y);
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.windowAnimations = mAnimation;
        }else{
            Toast.makeText(mContext, "悬浮窗布局未设置", Toast.LENGTH_SHORT).show();
        }
    }

    public View getView(){
        return mView;
    }

    public void show(){
        if(!isDisplay&&mView!=null){
            isDisplay = true;
            windowManager.addView(mView,layoutParams);
        }
    }

    public void dismiss(){
        if(isDisplay&&mView!=null){
            isDisplay = false;
            windowManager.removeView(mView);
        }
    }


    public boolean isDisplay() {
        return isDisplay;
    }
}
