package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class TransactionActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;


    @BindView(R.id.return_btn)
    LinearLayout return_btn;

    @Override
    public int getLayout() {
        return R.layout.activity_transaction;
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
        return_btn.setOnClickListener(v -> finish());
    }

    @Override
    public void destroy() {

    }
}