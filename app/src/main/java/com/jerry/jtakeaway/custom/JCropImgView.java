package com.jerry.jtakeaway.custom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import com.jerry.jtakeaway.R;

public class JCropImgView extends View {
    private int mImgSrc;
    private String mFileImgSrc;
    private Paint mBgPaint;
    private Paint mImgPaint;
    private float ImgWidth, ImgHeight;
    private float mSlideX = 0, mSlideY = 0;
    private float mStartSlideX = 0, mStartSlideY = 0;
    private float mScaleProgress = 1;
    private float mProgress = 1;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float speed = 1f;
    private ValueAnimator valueAnimator;
    private float width;
    private float height;
    private boolean isTwoPoint = false;


    public JCropImgView(Context context) {
        this(context, null);
    }

    public JCropImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JCropImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JCropImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setFileImgSrc(String fileImgSrc) {
        this.mFileImgSrc = fileImgSrc;
        if (mImgPaint == null) {
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setDither(true);
            p.setStyle(Paint.Style.FILL);
            mImgPaint = p;
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.JCropImgView);
            mImgSrc = a.getResourceId(R.styleable.JCropImgView_jImgSrc, View.NO_ID);
            a.recycle();
        }
        //初始化黑色背景画笔
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        mBgPaint = p;

        if (mImgSrc != -1) {
            mImgPaint = p;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算黑色背景路径
        width = getWidth();
        height = getHeight();
        if (mImgSrc != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mImgSrc);
            ltrb(bitmap);
        }
        if (mFileImgSrc != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(mFileImgSrc);
            ltrb(bitmap);
        }
        requestLayout();
    }

    private void ltrb(Bitmap bitmap) {
        ImgWidth = bitmap.getWidth();
        ImgHeight = bitmap.getHeight();
        float showHeight = width / 2;
        if (ImgWidth > ImgHeight) {
            //图片宽度大于高度
            if (ImgWidth > width) {
                float com = width / ImgWidth;
                ImgWidth = com * ImgWidth;
                ImgHeight = com * ImgHeight;
            } else {
                float c = width - ImgWidth;
                ImgWidth = ImgWidth + c;
                ImgHeight = ImgHeight + c;
            }
        } else {
            //图片高度大于宽度
            if (ImgHeight > height) {
                float com = height / ImgHeight;
                ImgHeight = ImgHeight * com;
                ImgWidth = ImgWidth * com;

            } else if (ImgHeight < height / 2) {
                float c = height - ImgHeight;
                ImgHeight = ImgHeight + c;
                ImgWidth = ImgWidth + c;
            }
        }
        left = (width - ImgWidth) / 2;
        top = (height - ImgHeight) / 2;
        right = (width - ImgWidth) / 2 + ImgWidth;
        bottom = (height - ImgHeight) / 2 + ImgHeight;
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画黑底
        mBgPaint.setAlpha(255);
        canvas.drawRect(new RectF(
                0,
                0,
                getWidth(),
                getHeight()
        ), mBgPaint);
        if (mFileImgSrc != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(mFileImgSrc);
            DrawImg(canvas, bitmap);
        } else if (mImgSrc != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mImgSrc);
            DrawImg(canvas, bitmap);
        }


        //画黑框
        mBgPaint.setAlpha(150);
        canvas.drawRect(new RectF(
                0,
                0,
                getWidth(),
                getHeight() / 4
        ), mBgPaint);
        canvas.drawRect(new RectF(
                0,
                getHeight() * 3 / 4,
                getWidth(),
                getHeight()
        ), mBgPaint);
    }

    private void DrawImg(Canvas canvas, Bitmap bitmap) {
        if(isTwoPoint){

        }else{
            float moveX = (mSlideX - mStartSlideX) * speed;
            float moveY = (mSlideY - mStartSlideY) * speed;
            mStartSlideX = mSlideX;
            mStartSlideY = mSlideY;
            left = left + moveX;
            top = top + moveY;
            right = right + moveX;
            bottom = bottom + moveY;
        }
        if (left < 0) {
            left = left + mProgress;
            right = right + mProgress;
        }
        if (top < 0) {
            top = top + mProgress;
            bottom = bottom + mProgress;
        }
        if (bottom > getHeight()) {
            bottom = bottom - mProgress;
            top = top - mProgress;
        }
        if (right > getWidth()) {
            right = right - mProgress;
            left = left - mProgress;
        }

        if(right<0){
            right = right + mProgress;
            left = left + mProgress;
        }
        if(left>width){
            right = right - mProgress;
            left = left - mProgress;
        }

        if(top>height){
            bottom = bottom - mProgress;
            top = top - mProgress;
        }
        if(bottom<0){
            top = top + mProgress;
            bottom = bottom + mProgress;
        }
        canvas.drawBitmap(bitmap, null, new RectF(left, top, right, bottom), mImgPaint);
    }


    private void edgeDetection(float value) {
        System.out.println("回弹:" + value);
        valueAnimator = ValueAnimator.ofFloat(0, Math.abs(value));
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            invalidate();
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mProgress =  Math.abs(value)/5;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgress = 0;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.cancel();
        valueAnimator.start();

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStartSlideX = event.getX();
            mStartSlideY = event.getY();
            return true;
        } else if(event.getAction()== MotionEvent.ACTION_POINTER_DOWN){
            //双子
            isTwoPoint = true;



            return true;
        }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(isTwoPoint){

            }else{
                mSlideX = event.getX();
                mSlideY = event.getY();
            }
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isTwoPoint = false;
            mStartSlideX = event.getX();
            mStartSlideY = event.getY();
            if (left < 0) {
                edgeDetection(left);
            } else if (top < 0) {
                edgeDetection(top);
            } else if (right > width) {
                edgeDetection(right - width);
            } else if (bottom > height) {
                edgeDetection(bottom - height);
            }else if(bottom<0){
                edgeDetection(height);
            }else if(top>height){
                edgeDetection(height);
            }else if(right<0){
                edgeDetection(width);
            }else if(left>width){
                edgeDetection(width);
            }
        }
        return super.onTouchEvent(event);
    }
}
