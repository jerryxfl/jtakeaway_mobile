package com.jerry.jtakeaway.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JTabItem extends View {
    private Context mContext;
    private Paint circlePaint;
    private int circleRadius;

    private Paint imgPaint;
    private Paint textPaint;
    private List<Integer> icons = new ArrayList<Integer>();
    private List<String> titles = new ArrayList<String>();
    private int oldPosition = 0, currentPosition = 0;


    public JTabItem(Context context) {
        this(context, null);
    }

    public JTabItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JTabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JTabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
    }

    //测量控件
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = width + getPaddingLeft() + getPaddingRight();
        int minHeight = height + getPaddingTop() + getPaddingBottom();
        System.out.println("tabItem:" + minWidth + " :  " + minHeight);

        int maeasureWidth = 0, maeasureHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY) {//确切值
            maeasureWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            maeasureWidth = Math.min(minWidth, width);
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            maeasureWidth = minWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {//确切值
            maeasureHeight = height;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            maeasureHeight = Math.min(minHeight, height);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            maeasureHeight = height;
        }
        System.out.println("tabItem:" + maeasureWidth + " :  " + maeasureHeight);

        setMeasuredDimension(maeasureWidth, maeasureHeight);
    }

    private void Init(Context context, AttributeSet attrs) {
        this.mContext = context;

        //初始化图片画笔
        imgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        imgPaint.setAntiAlias(true);
        imgPaint.setDither(true);

        //初始化文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);


        //初始化圆的画笔
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setStyle(Paint.Style.FILL);
    }


    //设置图标
    public void setIcons(List<Integer> icons) {
        this.icons.addAll(icons);
    }

    //设置每个标题
    public void setTitiles(List<String> titles) {
        this.titles.addAll(titles);
    }

    //设置被选中位置
    public void setPosition(int position) {
        this.currentPosition = position;
    }

    //获得被选中位置
    public int getCurrentPosition() {
        return currentPosition;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = icons.size();//item数量
        int itemWidth = getWidth() / size;//每个item的宽度
        circleRadius = getHeight() / 2 - 10;
        for (int i = 0; i < icons.size(); i++) {
            if (i == currentPosition) circlePaint.setColor(Color.parseColor("#fbfbfb"));
            else circlePaint.setColor(Color.parseColor("#ffa39e"));
            canvas.drawCircle(itemWidth * i + itemWidth / 2, getHeight() / 2, circleRadius, circlePaint);
            RectF rectF = new RectF(
                    itemWidth * i + itemWidth / 4,
                    circleRadius / 2+10,
                    itemWidth * i + itemWidth * 3 / 4,
                    (circleRadius / 2)+circleRadius+20
            );
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icons.get(i));
            canvas.drawBitmap(bitmap, null, rectF, imgPaint);
        }


    }
}

