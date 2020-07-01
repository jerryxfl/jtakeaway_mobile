package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Wallet;
import com.jerry.jtakeaway.bean.events.InvestMoney;
import com.jerry.jtakeaway.bean.model.TIButton;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WalletActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.set_item)
    RecyclerView set_item;

    @BindView(R.id.return_btn)
    LinearLayout return_btn;

    @BindView(R.id.money)
    TextView money;

    private JAdapter<TIButton> jAdapter;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        SignEventBus();

        JgridLayoutManager jgridLayoutManager = new JgridLayoutManager(this, 1);
        set_item.setLayoutManager(jgridLayoutManager);
        jAdapter = new JAdapter<>(this, set_item, new int[]{R.layout.setting_item}, new JAdapter.adapterListener<TIButton>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<TIButton> datas) {
                RelativeLayout container = holder.getView(R.id.container);
                ImageView img = holder.getView(R.id.img);
                TextView text = holder.getView(R.id.text);
                img.setImageDrawable(ContextCompat.getDrawable(WalletActivity.this, datas.get(position).getImg()));
                text.setText(datas.get(position).getText());
                container.setOnClickListener(v -> {
                    datas.get(position).getEvent().onClick();
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<TIButton> datas) {

            }

            @Override
            public int getViewType(List<TIButton> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        List<TIButton> settings = new ArrayList<>();
        settings.add(new TIButton(R.drawable.invest, "充值", () -> {
            startActivity(new Intent(WalletActivity.this, InvestActivity.class));
        }));
        settings.add(new TIButton(R.drawable.wallet, "提现", () -> {
            startActivity(new Intent(WalletActivity.this, ExtractMoneyActivity.class));
        }));
        settings.add(new TIButton(R.drawable.transaction, "交易记录", () -> {
            startActivity(new Intent(WalletActivity.this, TransactionActivity.class));
        }));
        settings.add(new TIButton(R.drawable.pay_password, "支密修改", () -> {
            Toast.makeText(WalletActivity.this,"暂不支持修改支付密码",Toast.LENGTH_SHORT).show();
        }));
        jAdapter.adapter.setData(settings);

        getWallet(0);
    }

    private void getWallet(int type) {
        if (type == 0) {
            if (jCenterDialog == null)
                jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
            jCenterDialog.show();
        }

        OkHttp3Util.GET(JUrl.user_wallet_money, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (type == 0) jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if (result.getCode() == 10000) {
                    System.out.println(result.getData().toString());
                    Wallet wallet = GsonUtil.gsonToBean(result.getData().toString(), Wallet.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (type == 0) jCenterDialog.dismiss();
                        DecimalFormat df = new DecimalFormat("#.00");
                        money.setText(String.valueOf(df.format(wallet.getBalance())));
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (type == 0) jCenterDialog.dismiss();
                        Toast.makeText(WalletActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void InitListener() {
        return_btn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void destroy() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MoneyEvent(InvestMoney investMoney) {
        if (investMoney.isActive()) {
            System.out.println("余额改变");
            getWallet(1);
        }
    }

}