package com.jerry.jtakeaway.ui.generalActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.ui.user.activity.HomeActivity;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.timepicker_tv)
    TextView timePicker;
    @BindView(R.id.fkwm_iv)
    ImageView fkwm_iv;
    @BindView(R.id.advertising_iv)
    ImageView advertising_iv;

    private MyCountdownTimer countdowntimer;
    private Handler handler;
    private Runnable runnable;

    @Override
    public int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void InitView() {
        //设置顶部距离
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)timePicker.getLayoutParams();
        params.topMargin = PixAndDpUtil.getStatusBarHeight(this);
        timePicker.setLayoutParams(params);

        countdowntimer = new MyCountdownTimer(4000, 1000);
        countdowntimer.start();
        runnable = () -> {
            toNewActivity();
        };

        handler = new Handler();
        handler.postDelayed(runnable,4000);
    }


    protected class MyCountdownTimer extends CountDownTimer {

        public MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            timePicker.setText(millisUntilFinished/1000+" S 跳转");
        }

        @Override
        public void onFinish() {
            timePicker.setText("跳转");
        }

    }



    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        timePicker.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            countdowntimer.cancel();
            toNewActivity();
        });

        advertising_iv.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            countdowntimer.cancel();
            Intent intent = new Intent(WelcomeActivity.this,WebActivity.class);
            intent.putExtra("URL","www.baidu.com");
            startActivity(intent);
            finish();
        });


    }

    private void toNewActivity(){
        if(MMkvUtil.getInstance(WelcomeActivity.this, "jwts").decodeString("jwt")==null||MMkvUtil.getInstance(WelcomeActivity.this, "jwts").decodeString("jwt").equals("")){
            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void destroy() {

    }
}
