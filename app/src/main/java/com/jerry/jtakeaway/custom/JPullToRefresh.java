package com.jerry.jtakeaway.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.core.view.animation.PathInterpolatorCompat;

public class JPullToRefresh extends View {
    private Paint mCirclePaint;
    private float mCircleRadius=50;
    private float mCirclePointX;
    private float mCirclePointY;


    //可拖动高度
    private int mDragHeight = 300;

    private Context mContext;
    private float mProgress;//进度值

    //目标宽度
    private int mTargetWidth = 400;

    //贝塞尔曲线画笔及路径
    private Path mPath = new Path();
    private Paint mPathPaint;

    //重心点最终高度,决定控制点的 y
    private int mTargetGravityHeight = 10;

    //角度变换 0~16度
    private int mTangentAngle = 105;

    private Interpolator mProgressInterpolator = new DecelerateInterpolator();
    private Interpolator mTangentAngleInterpolator;


    public JPullToRefresh(Context context) {
        this(context,null);
    }

    public JPullToRefresh(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JPullToRefresh(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public JPullToRefresh(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context);
    }

    private void Init(Context context) {
        this.mContext = context;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#FF4081"));
        mCirclePaint = p;

        //初始化路径画笔
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#FF4081"));
        mPathPaint = p;

        //切角路径插值器
        mTangentAngleInterpolator =  PathInterpolatorCompat.create(
                (mCircleRadius*2.0f)/mDragHeight,
                90.0f/mTangentAngle
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = canvas.save();
        float tranX = (getWidth()-getValueByLine(getWidth(),mTargetWidth,mProgress))/2;
        canvas.translate(tranX,0);
        //画贝塞尔曲线
        canvas.drawPath(mPath, mPathPaint);


        //画园
        canvas.drawCircle(mCirclePointX,
                mCirclePointY,
                mCircleRadius,
                mCirclePaint);
        canvas.restoreToCount(count);
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

        int minWidth = (int) (2*mCircleRadius+getPaddingLeft() + getPaddingRight());
        int minHeight =  (int)((mDragHeight*mProgress+0.5f)+getPaddingTop() + getPaddingBottom());

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

    //当控件大小改变是触发
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePathLayout();
    }

    public void setProgress(float progress){
        Log.e("TAG","P:"+progress);
        this.mProgress = progress;
        //重新布局测量
        requestLayout();
    }


    /*
     *更新路径
     */
    private void updatePathLayout() {
        //进度值
        final float progress = mProgressInterpolator.getInterpolation(mProgress);
        //获得可绘制区域高度宽度
        final float w = getValueByLine(getWidth(),mTargetWidth,mProgress);
        final float h = getValueByLine(0,mDragHeight,mProgress);
        //中心对称轴x
        final float cPoointX = w/2.0f;
        //园半径
        final float cRadius = mCircleRadius;
        //园中心y
        final float cPoointY = h-cRadius;
        //控制点结束y
        final float endControlY = mTargetGravityHeight;


        //更新园坐标
        mCirclePointX = cPoointX;
        mCirclePointY = cPoointY;

        final Path path = mPath;
        //复位
        path.reset();
        path.moveTo(0,0);

        //左边结束点和控制点
        float lEndPointX,lEndPointY;
        float lControlPointX,lControlPointY;
        float angle = mTangentAngle*mTangentAngleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle);
        float x = (float) (Math.sin(radian)* cRadius);
        float y = (float) (Math.cos(radian)* cRadius);

        lEndPointX = cPoointX -x;
        lEndPointY = cPoointY +y;

        lControlPointY = getValueByLine(0,endControlY,progress);
        float tHeight = lEndPointY - lControlPointY;
        float tWidth = (float) (tHeight / Math.tan(radian));

        lControlPointX = lEndPointX - tWidth;

        path.quadTo(lControlPointX,lControlPointY,lEndPointX,lEndPointY);
        path.lineTo(cPoointX+(cPoointX-lEndPointX),lEndPointY);
        path.quadTo(cPoointX+cPoointX-lControlPointX,lControlPointY,w,0);
    }


    //获取start end progress 返回当前进度值
    private float getValueByLine(float start,float end,float progress){
        return start+(end-start)*progress;
    }

    private ValueAnimator valueAnimator;

    public void release() {
        if(valueAnimator == null){
            ValueAnimator animation = ValueAnimator.ofFloat(mProgress,0f);
            animation.setDuration(400);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object val = animation.getAnimatedValue();
                    if(val instanceof Float){
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animation;
        }else{
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mProgress,0f);
        }
        valueAnimator.start();
    }
}
