package com.jerry.jtakeaway.ui.user.fragment;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.custom.JPullToRefresh;

import butterknife.BindView;

@SuppressWarnings("all")
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.test)
    JPullToRefresh test;
    @BindView(R.id.activity_main)
    RelativeLayout activity_main;

    private static  final  float TOUCK_MOVE_MAX_Y = 600;
    private float mTouchMoveStartY;

    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void InitView() {

    }


    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //得到意图
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN :
                        mTouchMoveStartY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE :
                        float y = event.getY();
                        if(y>=mTouchMoveStartY){
                            float moveSize = y -mTouchMoveStartY;
                            float progress = moveSize>=TOUCK_MOVE_MAX_Y
                                    ?1:moveSize/TOUCK_MOVE_MAX_Y;
                            test.setProgress(progress);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        test.release();
                        return true;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public void destroy() {

    }
}