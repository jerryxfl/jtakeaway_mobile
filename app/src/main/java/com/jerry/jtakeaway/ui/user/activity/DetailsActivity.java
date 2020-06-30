package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class DetailsActivity extends BaseActivity {
    @BindView(R.id.details_back)
    ImageView details_back;
    @BindView(R.id.activity_title)
    LinearLayout activity_title;

    @Override
    public int getLayout() {
        return R.layout.activity_details;
    }

    @Override
    public void InitView() {
        details_back.setOnClickListener(v -> {
            Intent intent=new Intent(DetailsActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        });
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) activity_title.getLayoutParams();
        int top= PixAndDpUtil.getStatusBarHeight(this);//获取状态栏高度
        layoutParams.topMargin=top;
        activity_title.setLayoutParams(layoutParams);
    }
}