package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jerry.jtakeaway.bean.model.Axis;

import java.util.ArrayList;
import java.util.List;

public class JChart extends View {
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Paint mTipPaint;
    private Paint mTextPaint;
    private Paint mGridPaint;


    private int mPointColor = Color.parseColor("#118EEA");
    private int mLineColor = Color.parseColor("#118EEA");
    private int mTextColor = Color.BLACK;
    private int mTipColor = Color.BLUE;
    private int mGridColor = Color.parseColor("#bfbfbf");

    private int mTextSize= 20;


    private int length = 30;//x坐标数
    private int ylength = 10;


    private float height,width;
    private float perHeight;
    private List<Float> dartPointsX = new ArrayList<Float>();

    private List<Axis> points = new ArrayList<Axis>();


    public JChart(Context context) {
        this(context, null);
    }

    public JChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mPointColor);
        p.setStrokeWidth(2);
        //坐标点
        mPointPaint = p;


        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mLineColor);
        p.setStrokeWidth(4);
        //线
        mLinePaint = p;

        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mTextColor);
        p.setTextSize(mTextSize);
        p.setTextAlign(Paint.Align.CENTER);
        //字体
        mTextPaint = p;

        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(mTipColor);
        //tip
        mTipPaint = p;


        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        p.setColor(mGridColor);
        //网格
        mGridPaint = p;
    }


    public void setYlength(int ylength) {
        this.ylength = ylength;
        perHeight = height/ ylength;
        invalidate();
    }

    public void setLength(int length) {
        this.length = length;
        invalidate();
    }

//    public void setPoints(List<Line> lines){
//        this.points.clear();
//        for (int i = 0; i < lines.size(); i++) {
//            points.add(new Axis(dartPointsX.get(lines.get(i).getTag()),lines.get(i).getY()));
//        }
//        invalidate();
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        float perWidth = width/length;
        for (int i = 0; i < length; i++) {
            dartPointsX.add((i+1)*perWidth);
        }
        perHeight = height/ylength;

        for (int i = 0; i < dartPointsX.size(); i++) {
            points.add(new Axis(dartPointsX.get(i),height-(i*10/4+perHeight)));
        }

        requestLayout();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < ylength; i++) {
            canvas.drawLine(0,perHeight*i,width,perHeight*i,mGridPaint);
        }
        for (int i = 0; i < dartPointsX.size(); i=i+5) {
            canvas.drawText("6-"+(i+1),dartPointsX.get(i),perHeight*ylength-mTextSize,mTextPaint);
        }
        for (int i = 0; i < points.size(); i++) {
            if(i+1<points.size())canvas.drawLine(points.get(i).getX(),points.get(i).getY(),points.get(i+1).getX(),points.get(i+1).getY(),mLinePaint);
            canvas.drawCircle(points.get(i).getX(),points.get(i).getY(),5,mPointPaint);
        }

    }
}
