package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class NotifySoundActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.audioStyle)
    RelativeLayout audioStyle;

    @BindView(R.id.noticeStyle)
    RelativeLayout noticeStyle;




    @Override
    public int getLayout() {
        return R.layout.activity_notify_sound;
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
        return_aib.setOnClickListener(v -> finish());
        audioStyle.setOnClickListener(v ->{
            startActivity(new Intent(NotifySoundActivity.this,AudioStyleActivity.class));
        });
        noticeStyle.setOnClickListener(v ->{
            startActivity(new Intent(NotifySoundActivity.this,NoticeStyleActivity.class));
        });

    }

    @Override
    public void destroy() {

    }
}