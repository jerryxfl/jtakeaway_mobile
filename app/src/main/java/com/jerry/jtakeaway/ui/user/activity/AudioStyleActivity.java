package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jerry.jtakeaway.Notification.Notifications;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class AudioStyleActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    @BindView(R.id.btn)
    Button btn;

    @Override
    public int getLayout() {   
        return R.layout.activity_audio;
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
        btn.setOnClickListener(v ->{
            Notifications.sendNormalNotification(AudioStyleActivity.this,0,"疯狂外卖","你的订单已发货");
        });
    }

    @Override
    public void destroy() {

    }
}