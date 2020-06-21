package com.jerry.jtakeaway.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jerry.jtakeaway.R;

public class JCenterDialog extends Dialog {

    private View view;

    public JCenterDialog(@NonNull Context context, int layout) {
        super(context);
        init(context,layout,null,null);
    }

    public JCenterDialog(@NonNull Context context, int layout, JBottomDialog.setItem setItem) {
        super(context);
        init(context,layout,setItem,null);
    }

    public JCenterDialog(@NonNull Context context, int layout, JBottomDialog.setItem setItem, JBottomDialog.event event) {
        super(context);
        init(context,layout,setItem,event);
    }


    protected JCenterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private  void init(Context context, int layout, JBottomDialog.setItem setItem, JBottomDialog.event event){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.JBottomDialog);

        view = LayoutInflater.from(context).inflate(layout,null);
        setContentView(view);
        if(setItem!=null)setItem.ItemOnCreate(view);

        if(event!=null){
            setOnDismissListener(dialog -> event.OnHideListener(dialog,view));
        }
    }


    public void setJCancelable(boolean cancelable) {
        setCancelable(cancelable);
    }

    public void setJCanceledOnTouchOutside(boolean canceledOnTouchOutside){
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }



    public View findViewById(int resourceId){
        return view.findViewById(resourceId);
    }

    public interface setItem{
        void ItemOnCreate(View view);
    }


    public interface event{
        void OnHideListener(DialogInterface dialog, View view);
    }
}
