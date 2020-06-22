package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

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