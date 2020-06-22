package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.JPayEditText;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class OpenPaymentActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.top)
    View top;

    @BindView(R.id.num_0)
    Button num_0;

    @BindView(R.id.num_1)
    Button num_1;

    @BindView(R.id.num_2)
    Button num_2;

    @BindView(R.id.num_3)
    Button num_3;

    @BindView(R.id.num_4)
    Button num_4;

    @BindView(R.id.num_5)
    Button num_5;

    @BindView(R.id.num_6)
    Button num_6;

    @BindView(R.id.num_7)
    Button num_7;

    @BindView(R.id.num_8)
    Button num_8;

    @BindView(R.id.num_9)
    Button num_9;

    @BindView(R.id.delete)
    Button delete;

    @BindView(R.id.payEdit)
    JPayEditText payEdit;

    @Override
    public int getLayout() {
        return R.layout.activity_open_payment;
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
        num_0.setOnClickListener(this);
        num_1.setOnClickListener(this);
        num_2.setOnClickListener(this);
        num_3.setOnClickListener(this);
        num_4.setOnClickListener(this);
        num_5.setOnClickListener(this);
        num_6.setOnClickListener(this);
        num_7.setOnClickListener(this);
        num_8.setOnClickListener(this);
        num_9.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                payEdit.delete();
                break;
            default:
                Button button = (Button) v;
                payEdit.setText(button.getText().toString());
        }
    }
}