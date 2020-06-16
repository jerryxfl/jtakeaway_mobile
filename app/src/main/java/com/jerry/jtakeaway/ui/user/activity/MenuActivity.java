package com.jerry.jtakeaway.ui.user.activity;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.original_price_tv)
    TextView original_price_tv;//原价


    @Override
    public int getLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        //设置下划线
        original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }
}