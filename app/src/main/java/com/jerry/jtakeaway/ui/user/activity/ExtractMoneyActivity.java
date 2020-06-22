package com.jerry.jtakeaway.ui.user.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.events.InvestMoney;
import com.jerry.jtakeaway.bean.requestBean.Tmoney;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.custom.PayPwdEditText;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.KeyboardUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExtractMoneyActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_btn)
    LinearLayout return_btn;

    @BindView(R.id.money)
    EditText money;

    @BindView(R.id.invest)
    Button Invest;
    private JCenterDialog jCenterDialog;
    private JBottomDialog payDialog;


    @Override
    public int getLayout() {
        return R.layout.activity_extract_money;
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
        Invest.setOnClickListener(v -> {
            String money_num = money.getText().toString().trim();
            if ("".equals(money_num)) {
                money.setError("请填写充值金额");
                return;
            }
            KeyboardUtils.hideKeyboard(ExtractMoneyActivity.this);
            showPayDialog(Integer.parseInt(money_num));
        });
    }

    private void showPayDialog(int money) {
        payDialog = new JBottomDialog(this, R.layout.extract_money_input_dialog, view -> {
            TextView money_tv = view.findViewById(R.id.money_tv);
            PayPwdEditText payPwdEditText = view.findViewById(R.id.PayPwdEditText);
            payPwdEditText.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.grey, R.color.grey, 20);
            money_tv.setText(String.valueOf(money));
            payPwdEditText.setOnTextFinishListener(str -> {
                //密码输入完毕触发支付
                Invest(money,payPwdEditText.getPwdText());
            });

            RecyclerView num_recyclview = view.findViewById(R.id.num_recyclview);
            JgridLayoutManager layoutManager = new JgridLayoutManager(ExtractMoneyActivity.this, 3);
            num_recyclview.setLayoutManager(layoutManager);
            JAdapter<String> adapter = new JAdapter<>(ExtractMoneyActivity.this, num_recyclview, new int[]{R.layout.num_item}, new JAdapter.adapterListener<String>() {
                @Override
                public void setItems(BaseViewHolder holder, int position, List<String> datas) {
                    TextView textView = holder.getView(R.id.num);
                    RelativeLayout num_container = holder.getView(R.id.num_container);
                    textView.setText(datas.get(position));

                    if (position == 9) {
                        //删除
                        num_container.setOnClickListener(v1 -> {
                            System.out.println("删除");
                            int length = payPwdEditText.getText().length();
                            if (length >= 1) {
                                payPwdEditText.setText(payPwdEditText.getText().substring(0, length - 1));
                            }
                        });
                    } else if (position == 11) {
                        //确定
                        num_container.setOnClickListener(v1 -> {
                            System.out.println("确定");
                            //点击确定触发支付
                            Invest(money,payPwdEditText.getPwdText());
                        });
                    } else {
                        num_container.setOnClickListener(v1 -> {
                            payPwdEditText.setText(payPwdEditText.getText() + datas.get(position));
                        });
                    }


                }

                @Override
                public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<String> datas) {

                }

                @Override
                public int getViewType(List<String> datas, int position) {
                    return 0;
                }
            });
            List<String> nums = new ArrayList<String>();
            nums.add("1");
            nums.add("2");
            nums.add("3");
            nums.add("4");
            nums.add("5");
            nums.add("6");
            nums.add("7");
            nums.add("8");
            nums.add("9");
            nums.add("删除");
            nums.add("0");
            nums.add("确定");
            adapter.adapter.setFooter(nums);
        }, (dialog, view) -> {
            PayPwdEditText payPwdEditText = view.findViewById(R.id.PayPwdEditText);
            payPwdEditText.clearText();
        });
        payDialog.show();
    }



    private void Invest(int money, String payPassword) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) JSONObject.toJSON(new Tmoney(money, payPassword));

        OkHttp3Util.POST(JUrl.t_wallet_money, this, json, new Callback() {
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
                        if(payDialog!=null)payDialog.dismiss();
                        Toast.makeText(ExtractMoneyActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new InvestMoney(true));
                        finish();
                    });
                }else if(result.getCode()== 9){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ExtractMoneyActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ExtractMoneyActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public void destroy() {

    }
}