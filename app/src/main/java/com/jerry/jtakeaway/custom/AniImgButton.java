package com.jerry.jtakeaway.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import com.jerry.jtakeaway.R;

@SuppressLint("AppCompatCustomView")
public class AniImgButton extends ImageButton implements View.OnTouchListener {
    private Context mContext;
    private Drawable normalImg,pressedImg;
    public AniImgButton(Context context) {
        super(context);
        this.mContext = context;
        init(null);
    }

    public AniImgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public AniImgButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    public AniImgButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ResourceAsColor")
    private void init(AttributeSet attrs){
//        setBackgroundColor(android.R.color.transparent);
        if(attrs!=null){
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AniImgButton);
            normalImg = a.getDrawable(R.styleable.AniImgButton_normalImg);
            pressedImg = a.getDrawable(R.styleable.AniImgButton_pressedImg);
            a.recycle();
        }
        setOnTouchListener(this);
    }

    public void click(boolean status){
        if(normalImg!=null&&pressedImg!=null){
            if(status)setImageDrawable(pressedImg);
            else setImageDrawable(normalImg);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            OnDownAnimation();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            OnUpAnimation();
        }


        return false;
    }


    private void OnDownAnimation(){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this,"scaleX",1f,0.7f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this,"scaleY",1f,0.7f);
        set.setDuration(400);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(animatorX,animatorY);
        set.start();
    }

    private void OnUpAnimation(){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this,"scaleX",0.7f,1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this,"scaleY",0.7f,1f);
        set.setDuration(400);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(animatorX,animatorY);
        set.start();
    }
}
