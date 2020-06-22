package com.jerry.jtakeaway.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.jtakeaway.R;

public class JPayEditText extends View {
    private Paint pwdbgPaint;
    private Paint pwdBorderPaint;
    private Paint pwdPaint;

    private int pwdLength = 6;
    private int borderWidth = 2;
    private int distance = 20;

    private float itemWidth,itemHeight;

    private String text = "";

    private OnTextFinishListener onTextFinishListener;

    private OnTextChangeListener onTextChangeListener;

    public JPayEditText(Context context) {
        this(context,null);
    }

    public JPayEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JPayEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.JPayEditText);
            pwdLength = a.getInt(R.styleable.JPayEditText_jpwdLength,6);
            borderWidth = a.getInt(R.styleable.JPayEditText_jborderWidth,2);
            a.recycle();
        }

        //初始化边框画笔
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        pwdBorderPaint = p;


        //背景画笔
        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        pwdbgPaint = p;



        //初始化密码画笔
        p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        pwdPaint = p;
    }

    public void setPwdLength(int pwdLength) {
        this.pwdLength = pwdLength;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = (getWidth()-distance*(pwdLength-1))/pwdLength;
        itemHeight = getHeight();
        requestLayout();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pwdbgPaint.setColor(Color.parseColor("#F7F8FC"));
        canvas.drawRect(new RectF(0,0,getWidth(),getHeight()),pwdbgPaint);
        pwdbgPaint.setColor(Color.WHITE);
        for (int i = 0; i < pwdLength; i++) {
            @SuppressLint("DrawAllocation") RectF rectF2 = new RectF(
                    i*distance+i*itemWidth,
                    0,
                    i*distance+i*itemWidth+itemWidth,
                    itemHeight
            );
            canvas.drawRect(rectF2,pwdBorderPaint);

            @SuppressLint("DrawAllocation") RectF rectF = new RectF(
                    i*distance+i*itemWidth+borderWidth,
                    borderWidth,
                    i*distance+i*itemWidth+itemWidth-borderWidth,
                    itemHeight-borderWidth
            );
            canvas.drawRect(rectF,pwdbgPaint);
        }
        //画密码点

        if(text!=null){
            for (int i = 0; i < text.length(); i++) {
                float centerX = i*distance+i*itemWidth+itemWidth/2;
                canvas.drawCircle(centerX,itemWidth/2-6,12,pwdPaint);
            }
        }
    }

    public void setOnTextFinishListener(OnTextFinishListener onTextFinishListener){
        this.onTextFinishListener = onTextFinishListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener){
        this.onTextChangeListener = onTextChangeListener;
    }

    public void setJText(String text){
        if(this.text.length()<pwdLength){
            this.text = this.text+text;
            invalidate();
            if(onTextChangeListener!=null)onTextChangeListener.onChange(this.text);
            if(this.text.length()==pwdLength){
                if(onTextFinishListener!=null)onTextFinishListener.onFinish(this.text);
            }
        }else{
            if(onTextFinishListener!=null)onTextFinishListener.onFinish(this.text);
        }
    }

    public void delete(){
        if(this.text.length()>0){
            this.text = this.text.substring(0,this.text.length()-1);
            invalidate();
            if(onTextChangeListener!=null)onTextChangeListener.onChange(this.text);
        }
    }

    public interface OnTextFinishListener
    {
        void onFinish(String str);
    }

    public interface OnTextChangeListener
    {
        void onChange(String str);
    }
}
