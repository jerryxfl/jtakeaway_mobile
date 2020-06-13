package com.jerry.jtakeaway.custom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

@SuppressWarnings("all")
public class JLoginButton extends View {
    private Paint buttonPaint;//矩形画笔
    private Paint textPaint;//字体画笔
    private Paint arcPaint;//圆弧画笔



    private Paint circlePaint;//圆的画笔
    private int circleRadius;//圆的半径
    private int circlePointX, circlePointY;//圆的坐标
    //注册背景色
    private int normalColor = Color.parseColor("#F76B6C");
    //显示文字
    private String text = "登录/注册";
    private float fontSize = 50;

//    private boolean isLogin = false;

    private float progress = 0;
    private float angle=0;



    private ValueAnimator valueAnimator;
    private ValueAnimator rotateValueAnimator;
    private float radius;
    private Path path;


    public JLoginButton(Context context) {
        this(context, null);
    }

    public JLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }


    private void Init() {
        //初始化圆的画笔
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setDither(true);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(normalColor);
        p.setStrokeWidth(10);
        buttonPaint = p;

        //初始化字体画笔
        p= new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(fontSize);
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        textPaint = p;

        //初始化圆弧画笔
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        p.setColor(Color.WHITE);
        arcPaint = p;

        path = new Path();

    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();
        //绘制矩形
        //矩形路径
        path.moveTo(0+progress, getHeight() / 2);
        path.quadTo(0+progress, 0, getWidth() / 6+progress>getWidth()/2?getWidth()/2:getWidth() / 6+progress, 0);
        path.lineTo(getWidth() * 5 / 6-progress<getWidth()/2?getWidth()/2:getWidth() * 5 / 6-progress, 0);
        path.quadTo(getWidth()-progress, 0, getWidth()-progress, getHeight() / 2);
        path.quadTo(getWidth()-progress, getHeight(), getWidth() * 5 / 6-progress<getWidth()/2?getWidth()/2:getWidth() * 5 / 6-progress, getHeight());
        path.lineTo( getWidth() / 6+progress>getWidth()/2?getWidth()/2:getWidth() / 6+progress, getHeight());
        path.quadTo(0+progress, getHeight(), 0+progress, getHeight() / 2);
        path.close();
        canvas.drawPath(path, buttonPaint);

        //绘制文字
        if(progress<getWidth()/6)canvas.drawText(text,circlePointX,getHeight()-fontSize,textPaint);
        if(progress>getWidth()/2-(circleRadius+20)){
            RectF rectF = new RectF(
                    getWidth()/2- radius,
                    (getHeight()- radius *2)/2,
                    getWidth()/2+ radius,
                    (getHeight()- radius *2)/2+ radius *2
            );
            canvas.drawArc(rectF,angle,270,false,arcPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 宽度意图
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //高度意图
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = circleRadius*2+getPaddingRight()+getPaddingLeft();
        int minHeight =  circleRadius*2+getPaddingTop()+getPaddingBottom();

        int maeasureWidth,maeasureHeight;

        if(widthMode == MeasureSpec.EXACTLY){
            //确切值
            maeasureWidth = width;
        }else if(widthMode == MeasureSpec.AT_MOST) {
            //最大值
            maeasureWidth = Math.min(width,minWidth);
        }else {
            maeasureWidth = width;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            //确切值
            maeasureHeight = height;
        }else if(heightMode == MeasureSpec.AT_MOST) {
            //最大值
            maeasureHeight = Math.min(height,minHeight);
        }else {
            maeasureHeight = height;
        }
        //通知测量完成
        setMeasuredDimension(maeasureWidth, maeasureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算园中心坐标
        circlePointX = getWidth() / 2;
        circlePointY = getHeight() / 2;
        circleRadius = circlePointY;
        radius = circleRadius-30;
        //重新布局测量
        requestLayout();
    }

    public void setOnClickListener(OnJClickListener listener) {
        setOnClickListener(v -> {
            Doing();
            listener.onClick();
        });
    }

    private void Doing(){
        if(valueAnimator == null){
            rotateValueAnimator = ValueAnimator.ofFloat(360);
            rotateValueAnimator.setDuration(1000);
            rotateValueAnimator.setRepeatCount(-1);
            rotateValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    angle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator = ValueAnimator.ofFloat(0,getWidth()/2-(circleRadius+15));
            valueAnimator.setDuration(1000);
            valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    invalidate();
                    rotateValueAnimator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.start();
        }else{
            valueAnimator.cancel();
            valueAnimator.start();
        }
    }

    public void reset() {
        if(valueAnimator != null){
            valueAnimator.reverse();
            rotateValueAnimator.cancel();
        }
    }

    public interface OnJClickListener {
        public void onClick(); //单击事件处理接口
    }


}
