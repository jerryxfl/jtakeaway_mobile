package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.util.AttributeSet;

public class JRollingTextView extends androidx.appcompat.widget.AppCompatTextView {

    public JRollingTextView(Context context) {
        this(context,null);
    }

    public JRollingTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JRollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
