package com.jerry.jtakeaway.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class JWaveView extends View {
    private Paint mBottomWavePaint;//底部波浪
    private Paint mTopWavePaint;//顶部波浪

    private Path bottomPath;
    private Path topPath;

    private int bottomColor = Color.parseColor("#D9360C");
    private int topColor = Color.WHITE;

    private float width, height, waveLength=800,waveLength2=600;
    private int waveCount;//波形个数
    private float waveHeight,waveTopHeight;
    private float offset = 0;

    public JWaveView(Context context) {
        this(context, null);
    }

    public JWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //初始化底部波浪
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setColor(bottomColor);
        p.setStyle(Paint.Style.FILL);
        mBottomWavePaint = p;


        //初始化顶部波浪
        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setColor(topColor);
        p.setStyle(Paint.Style.FILL);
        mTopWavePaint = p;

        bottomPath = new Path();
        topPath = new Path();

        ChoppyAnimation();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        waveCount = (int) Math.round((width/waveLength+1.5));
        waveHeight = height / 30;
        waveTopHeight = height / 20;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bottomPath.reset();
        topPath.reset();

        bottomPath.moveTo(-waveLength+offset, waveHeight);
        for (int i = 0; i < waveCount; i++) {
            bottomPath.quadTo(-waveLength * 3 / 4 + i * waveLength + offset, waveHeight + 60, -waveLength / 2 + i * waveLength + offset, waveHeight);
            bottomPath.quadTo(-waveLength * 1 / 4 + i * waveLength + offset, waveHeight - 60, i * waveLength + offset, waveHeight);
        }
        bottomPath.lineTo(width, height);
        bottomPath.lineTo(0, height);
        bottomPath.close();
        canvas.drawPath(bottomPath, mBottomWavePaint);

        float offset = this.offset-100;
        topPath.moveTo(-waveLength+offset, waveTopHeight);
        for (int i = 0; i < waveCount; i++) {
            topPath.quadTo(-waveLength * 3 / 4 + i * waveLength + offset, waveTopHeight +60 , -waveLength / 2 + i * waveLength + offset, waveTopHeight);
            topPath.quadTo(-waveLength * 1 / 4 + i * waveLength + offset, waveTopHeight - 60, i * waveLength + offset, waveTopHeight);
        }
        topPath.lineTo(width, height);
        topPath.lineTo(0, height);
        topPath.close();
        canvas.drawPath(topPath, mTopWavePaint);
    }


    private void ChoppyAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,waveLength);
        valueAnimator.addUpdateListener(animation -> {
            offset = (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }
}
