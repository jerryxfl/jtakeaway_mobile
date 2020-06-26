package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

public class JAudioImgView extends View {
    private Paint borderPaint;
    private Path borderPath;
    private float width, height;
    private float borderWidth = 15;
    private ScaleAnimation scaleAnimation;
    private boolean onSelected = false;



    public JAudioImgView(Context context) {
        this(context, null);
    }

    public JAudioImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JAudioImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JAudioImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        //初始化边框画笔
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.parseColor("#40a9ff"));
        p.setStrokeWidth(borderWidth);
        p.setAntiAlias(true);
        p.setDither(true);
        borderPaint = p;

        borderPath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        requestLayout();
    }

    public void setOnSelected(boolean onSelected) {
        this.onSelected = onSelected;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onSelected) {
            borderPath.reset();
            borderPath.moveTo(width / 10, 0);
            borderPath.lineTo(width * 9 / 10, 0);
            borderPath.rQuadTo(width / 10, 0, width / 10, height / 20);
            borderPath.rLineTo(0, height * 9 / 10);
            borderPath.rQuadTo(0, height / 20, -width / 10, height / 20);
            borderPath.rLineTo(-width * 4 / 5, 0);
            borderPath.rQuadTo(-width / 10, 0, -width / 10, -height / 20);
            borderPath.rLineTo(0, -height * 18 / 20);
            borderPath.rQuadTo(0, -height / 20, width / 10, -height / 20);
            canvas.drawPath(borderPath, borderPaint);
        }

    }



    public void setOnClickListener(Onclick onClick) {
        setOnClickListener(v -> {
            ClickEffect();
            onClick.onClick();
        });

    }

    public interface Onclick {
        public void onClick();
    }


    private void ClickEffect() {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(1, 0.9f, 1, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(300);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setInterpolator(new CycleInterpolator(1));
            startAnimation(scaleAnimation);
        } else {
            scaleAnimation.cancel();
            startAnimation(scaleAnimation);
        }

    }
}
