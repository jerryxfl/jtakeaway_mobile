package com.jerry.jtakeaway.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class FontIcon extends TextView {
    private Context mContext;

    public FontIcon(Context context) {
        super(context);
        init(context);
    }

    public FontIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        //设置字体图标
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");
        this.setTypeface(font);
    }


    public void changeTypeface(String text){
        setText(text);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");
        this.setTypeface(font);
    }
}
