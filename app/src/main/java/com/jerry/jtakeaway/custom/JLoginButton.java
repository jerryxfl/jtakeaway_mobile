package com.jerry.jtakeaway.custom;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

@SuppressWarnings("all")
public class JLoginButton extends View {
    private Context mContext;
    private int loginStatus = 0;//0初始状态 1正在登录 2登录成功 3登录失败

    private Paint circlePaint;//圆的画笔
    private int circleRadius;//圆的半径
    private int circlePointX, circlePointY;//圆的坐标

    private Paint linePaint;
    private Path linePath;


    private Paint arcPaint;
    private int arcAngle = -90;


    private ValueAnimator valueAnimator;


    public JLoginButton(Context context) {
        this(context, null);
    }

    public JLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }


    private void Init(Context context) {
        this.mContext = context;

        //初始化圆的画笔
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);

        //初始化线的画笔
        linePaint = new Paint();
        linePaint.setStrokeWidth(13);
        linePaint.setColor(Color.parseColor("#fbfbfb"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePath = new Path();//线的路径


        //圆弧画笔
        arcPaint = new Paint();
        arcPaint.setStrokeWidth(13);
        arcPaint.setColor(Color.parseColor("#fbfbfb"));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);
        arcPaint.setDither(true);


    }

    //测量控件
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = 2 * circleRadius + getPaddingLeft() + getPaddingRight();
        int minHeight = 2 * circleRadius + getPaddingTop() + getPaddingBottom();

        int maeasureWidth, maeasureHeight;
        if (widthMode == MeasureSpec.EXACTLY) {//确切值
            maeasureWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {//最大值
            maeasureWidth = Math.min(minWidth, width);
        } else {
            maeasureWidth = minWidth;
        }


        if (widthMode == MeasureSpec.EXACTLY) {//确切值
            maeasureHeight = height;
        } else if (widthMode == MeasureSpec.AT_MOST) {//最大值
            maeasureHeight = Math.min(minHeight, height);
        } else {
            maeasureHeight = minHeight;
        }

        setMeasuredDimension(maeasureWidth, maeasureHeight);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circleRadius = getMeasuredWidth() / 2;
        circlePointX = getMeasuredWidth() / 2;
        circlePointY = getMeasuredHeight() / 2;
        if (loginStatus == 0 || loginStatus == 1) {
            circlePaint.setColor(Color.parseColor("#fbfbfb"));
            circlePaint.setAlpha(100);
        } else if (loginStatus == 2) {
            circlePaint.setColor(Color.parseColor("#73d13d"));
            circlePaint.setAlpha(150);
        } else if (loginStatus == 3) {
            circlePaint.setColor(Color.parseColor("#f5222d"));
            circlePaint.setAlpha(150);
        }else if (loginStatus == 4){
            handler.removeMessages(0);
            circlePaint.setColor(Color.parseColor("#a0d911"));
            circlePaint.setAlpha(150);
        }
        canvas.drawCircle(circlePointX, circlePointY, circleRadius, circlePaint);//背景园

        if (loginStatus == 0) {
            //画箭头
            int arrowPointX = (getMeasuredWidth() * 3) / 4;
            int arrowPointY = getMeasuredHeight() / 2;

            int topPointX = getMeasuredWidth() / 2;
            int topPointY = getMeasuredHeight() / 4;

            int bottomPointX = getMeasuredWidth() / 2;
            int bottomPointY = (getMeasuredHeight() * 3) / 4;

            linePath.reset();
            linePath.moveTo(arrowPointX, arrowPointY);
            linePath.lineTo(topPointX, topPointY);
            linePath.moveTo(arrowPointX, arrowPointY);
            linePath.lineTo(bottomPointX, bottomPointY);
            linePath.close();
            canvas.drawPath(linePath, linePaint);
        } else if (loginStatus == 1) {
            //画圆弧
            RectF rectF = new RectF(getMeasuredWidth() / 4, getMeasuredHeight() / 4, (getMeasuredWidth() * 3) / 4, (getMeasuredHeight() * 3) / 4);
            canvas.drawArc(rectF, arcAngle, 260, false, arcPaint);
        }else if (loginStatus == 2){
            //登录成功
            //画勾
            int startPointX = (getMeasuredWidth()*3)/8;
            int startPointY = (getMeasuredHeight()*5)/8;

            int centerPointX = getMeasuredWidth() / 2;
            int centerPointY = (getMeasuredHeight() * 3) / 4;

            int endPointX = (getMeasuredWidth()*6)/8;
            int endPointY = (getMeasuredHeight()*3)/8;

            linePath.reset();
            linePath.moveTo(startPointX, startPointY);
            linePath.lineTo(centerPointX,centerPointY);
            linePath.quadTo(centerPointX,centerPointY,endPointX,endPointY);
            canvas.drawPath(linePath,linePaint);
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0,2000);
        }else if (loginStatus == 3){
            //登录失败
            int firstStartPointX = getMeasuredWidth() / 4;
            int firstStartPointY = getMeasuredHeight() / 4;

            int firstEndPointX = (getMeasuredWidth() *3)/ 4;
            int firstEndPointY = (getMeasuredHeight() *3)/ 4;


            int secondStartPointX = (getMeasuredWidth() *3)/ 4;
            int secondStartPointY = getMeasuredHeight() / 4;

            int secondEndPointX = getMeasuredWidth() / 4;
            int secondEndPointY = (getMeasuredHeight() *3)/ 4;

            linePath.reset();
            linePath.moveTo(firstStartPointX, firstStartPointY);
            linePath.lineTo(firstEndPointX, firstEndPointY);
            linePath.moveTo(secondStartPointX, secondStartPointY);
            linePath.lineTo(secondEndPointX, secondEndPointY);
            canvas.drawPath(linePath, linePaint);
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0,2000);
        }


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            loginStatus = 0;
            invalidate();
        }
    };




    public void startLogin() {
        System.out.println("登录动画执行");
        loginStatus = 1;
        if(valueAnimator==null){
            valueAnimator = ValueAnimator.ofInt(0, 1080);
            valueAnimator.addUpdateListener(animation -> {
                arcAngle = (int) animation.getAnimatedValue();
                invalidate();
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setDuration(3000);
            valueAnimator.start();
        }else {
            if(!valueAnimator.isRunning()){
                valueAnimator.start();
            }
        }
    }

    public void LoginSuccess() {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.end();
                loginStatus = 2;
                invalidate();
            }
        }
    }


    public void LoginFailed() {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.end();
                loginStatus = 3;
                invalidate();
            }
        }
    }

    public void goToNew() {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.end();
                loginStatus = 4;
                invalidate();
            }
        }
    }


    public void setOnClickListener(OnJClickListener  listener) {
        setOnClickListener(v -> {
            startLogin();
            listener.onClick();
        });
    }

    public interface  OnJClickListener{
        public void onClick(); //单击事件处理接口
    }


}
