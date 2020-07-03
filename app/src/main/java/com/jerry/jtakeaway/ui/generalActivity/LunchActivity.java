package com.jerry.jtakeaway.ui.generalActivity;

import android.content.Intent;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.MMkvUtil;

public class LunchActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_lunch;
    }

    @Override
    public void InitView() {
        int i = MMkvUtil.getInstance(this, "Configuration").decodeInt("GUIDE");
        if(i==0){
            startActivity(new Intent(this,GuideActivity.class));
        }else{
            startActivity(new Intent(this,WelcomeActivity.class));
        }
        finish();
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