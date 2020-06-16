package com.jerry.jtakeaway.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class JViewPager extends ViewPager {
    private boolean scrollble = true;

    private int startX;
    private int startY;

    public JViewPager(@NonNull Context context) {
        super(context);
    }

    public JViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (!scrollble)
        {
            return true;
        }
        return super.onTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev)
//    {
//
//        switch (ev.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                startX = (int) ev.getX();
//                startY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
////                int dX = (int) (ev.getX() - startX);
//                int dY = (int) (ev.getY() - startX);
//                if (Math.abs(dY)>0)  // 说明上下方向滑动了
//                {
//                    return false;
//                } else
//                {
//                    return true;
//                }
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    public boolean isScrollble()
    {
        return scrollble;
    }

    public void setScrollble(boolean scrollble)
    {
        this.scrollble = scrollble;
    }



}
