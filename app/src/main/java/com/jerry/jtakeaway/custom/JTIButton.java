package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import com.jerry.jtakeaway.R;

public class JTIButton extends View {
    private Paint mTextPaint;//文字画笔
    private Paint mImagePaint;//图片画笔
    private RectF rectF;//图片路径


    private int mTextSize = 20;
    private int mTextColor = Color.BLACK;
    private String mText;//没有则不画
    private Drawable mImageSrc;//没有则不画
    private Bitmap mImageSrc_bitMap;//没有则不画

    private float mTextX,mTextY;//文字坐标
    private ScaleAnimation animation;//点击动画




    public JTIButton(Context context) {
        this(context,null);
    }

    public JTIButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JTIButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public JTIButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(attrs);
    }

    private void Init(AttributeSet attrs) {
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.JTIButton);
            mTextSize = a.getInteger(R.styleable.JTIButton_jtextSize,20);
            mTextColor = a.getColor(R.styleable.JTIButton_jtextColor, Color.BLACK);
            mImageSrc = a.getDrawable(R.styleable.JTIButton_jsrc);
            mText = a.getString(R.styleable.JTIButton_jtext);
            a.recycle();
        }
        //将图片转化为bitmap
        BitmapDrawable bd = (BitmapDrawable) mImageSrc;
        mImageSrc_bitMap = bd.getBitmap();

        //初始化文字画笔
        Paint p = new Paint();
        p.setTextSize(mTextSize);
        p.setColor(mTextColor);
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStrokeWidth(5);
        mTextPaint = p;



        //初始化图片画笔
        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        mImagePaint = p;



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画图
        canvas.drawBitmap(mImageSrc_bitMap,null, rectF,mImagePaint);

        //画字
        canvas.drawText(mText,mTextX,mTextY,mTextPaint);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算图片位置
        //图片宽高
        float widthImg = mImageSrc_bitMap.getWidth();
        float heightImg = mImageSrc_bitMap.getHeight();

        //控件宽高
        float width = getWidth();
        float height = getHeight();

        //绘制图片的宽高
        float targetWidth;
        float targetHeight;
        if(widthImg>heightImg){
            if(widthImg>width){
                float com = width/widthImg;//计算图片宽度和控件跨度比列
                targetWidth = widthImg * com  - mTextSize*2;
                targetHeight = heightImg * com - mTextSize*2;
            }else{
                targetWidth = (width*3)/4;
                targetHeight = height * targetWidth/width;
            }
        }else{
            if(heightImg>height){
                float com = height/heightImg;//计算图片宽度和控件跨度比列
                targetWidth = widthImg * com  - mTextSize*2;
                targetHeight = heightImg * com - mTextSize*2;
            }else{
                targetHeight = (height*3)/4;
                targetWidth = height * targetHeight/width;
            }
        }

        //计算四个点坐标
        float left =  (width-targetWidth)/2;
        float top =  (height-targetHeight)/2;
        rectF = new RectF(
                left,
                top,
                left + targetWidth,
                top + targetHeight
        );


        //计算文字坐标
        mTextX = left+targetWidth/2-getTextWidth(mText)-mTextSize/2;
        mTextY = (float) (top+targetHeight+mTextSize/1.2);

        //重新布局测量
        requestLayout();
    }


    private float getTextWidth(String str) {
        Paint pFont = new Paint();
        Rect rect = new Rect();

//返回包围整个字符串的最小的一个Rect区域
        pFont.getTextBounds(str, 0, 1, rect);
        System.out.println("文字宽度:" +rect.width());
        return rect.width();
    }


    private void BtnAnimation(){
        System.out.println("JTIButton动画开始");
        if(animation == null){
            animation = new ScaleAnimation(1,0.7f,1,0.7f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setRepeatCount(0);
            animation.setDuration(300);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setInterpolator(new CycleInterpolator(1));
            startAnimation(animation);
        }else{
            animation.cancel();
            startAnimation(animation);
        }
    }


    public void SetOnclickListener(JTIOnclick jtiOnclick){
        setOnClickListener(v -> {
            BtnAnimation();
            jtiOnclick.onClick();
        });
    }

    public interface JTIOnclick{
        void onClick();
    }


}
