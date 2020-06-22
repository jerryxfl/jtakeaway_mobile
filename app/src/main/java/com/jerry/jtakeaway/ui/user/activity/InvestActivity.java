package com.jerry.jtakeaway.ui.user.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.events.InvestMoney;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InvestActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_btn)
    LinearLayout return_btn;

    @BindView(R.id.money)
    EditText money;

    @BindView(R.id.invest)
    Button Invest;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_invest;
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
        return_btn.setOnClickListener(v ->finish());
        Invest.setOnClickListener(v ->{
            String money_num = money.getText().toString().trim();
            if("".equals(money_num)){
                money.setError("请填写充值金额");
                return;
            }
            Invest(Integer.parseInt(money_num));
        });
    }

    private void Invest(int money){
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.c_wallet_money(money), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if (result.getCode() == 10000) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(InvestActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new InvestMoney(true));
                        finish();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(InvestActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public void destroy() {

    }
}