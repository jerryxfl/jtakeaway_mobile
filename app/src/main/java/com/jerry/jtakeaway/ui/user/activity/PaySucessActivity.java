package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class PaySucessActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.complete_tv)
    TextView complete_tv;

    @BindView(R.id.payMoney)
    TextView payMoney;

    @BindView(R.id.payMoney2)
    TextView payMoney2;

    @BindView(R.id.wallet_tv)
    TextView wallet_tv;
    @Override
    public int getLayout() {
        return R.layout.activity_pay_sucess;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        String pay_money = intent.getStringExtra("payMoney");
        payMoney.setText("-"+pay_money+"元");
        payMoney2.setText("-"+pay_money+"元");
    }

    @Override
    public void InitListener() {
        complete_tv.setOnClickListener(v -> finish());
        wallet_tv.setOnClickListener(v -> {
            startActivity(new Intent(PaySucessActivity.this,WalletActivity.class));
        });
    }

    @Override
    public void destroy() {

    }
}