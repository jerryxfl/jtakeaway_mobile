package com.jerry.jtakeaway.ui.user.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.custom.PayPwdEditText;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PayActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.conpon_recyclview)
    RecyclerView conpon_recyclview;

    @BindView(R.id.walletPay)
    RadioButton walletPay;

    @BindView(R.id.wechatPay)
    RadioButton wechatPay;

    @BindView(R.id.aliPay)
    RadioButton aliPay;

    @BindView(R.id.payWay)
    RadioGroup payWay;

    @BindView(R.id.payBtn)
    Button payBtn;


    //支付方式
    private int pay = 1;//1钱包支付  2微信支付  3支付宝支付

    private Coupon coupon;

    private JAdapter<Coupon> conponAdapter;
    private JBottomDialog payDialog;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private int currentPosition = -1;

    @Override
    public int getLayout() {
        return R.layout.activity_pay;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conpon_recyclview.setLayoutManager(layoutManager);
        conponAdapter = new JAdapter<>(this, conpon_recyclview, new int[]{R.layout.conpon_pay_item}, new JAdapter.adapterListener<Coupon>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Coupon> datas) {
                TextView discount = holder.getView(R.id.discount);//折扣
                TextView name = holder.getView(R.id.name);//名字
                TextView failtime = holder.getView(R.id.failtime);//过期时间
                Button user_conpon_btn = holder.getView(R.id.user_conpon_btn);//过期时间

                discount.setText(datas.get(position).getConponprice()+"折");
                name.setText(datas.get(position).getConpondesc());
                failtime.setText(datas.get(position).getConponfailuretime()+"到期");
                if(currentPosition==position){
                    user_conpon_btn.setText("已选择");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_already_shape));
                }else{
                    user_conpon_btn.setText("立即使用");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_selector));
                }


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        conponAdapter.adapter.notifyItemChanged(currentPosition,"change");
                    }
                },500);

                user_conpon_btn.setOnClickListener(v -> {
                    user_conpon_btn.setText("已选择");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_already_shape));
                    if(currentPosition!=-1)conponAdapter.adapter.notifyItemChanged(currentPosition);
                    currentPosition = position;
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Coupon> datas) {

            }

            @Override
            public int getViewType(List<Coupon> datas, int position) {
                return 0;
            }
        });


    }

    @Override
    public void InitData() {
        getConpon();
    }


    private void getConpon(){
        OkHttp3Util.GET(JUrl.m_conpon, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if(result.getCode() == 10000){
                    System.out.println(result.getData().toString());
                    couponList.clear();
                    couponList.addAll(GsonUtil.jsonToList(result.getData().toString(),Coupon.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if(conponAdapter!=null)conponAdapter.adapter.setFooter(couponList);
                    });
                }else if(result.getCode()==6){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(PayActivity.this,"未登录",Toast.LENGTH_SHORT).show();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(PayActivity.this,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        walletPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(pay);
                pay = 1;
            }
        });
        wechatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(pay);
                pay = 2;
            }
        });
        aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(pay);
                pay = 3;
            }
        });

        payBtn.setOnClickListener(v->{
            if(pay == 1){
                if(payDialog == null){
                    payDialog = new JBottomDialog(this, R.layout.pay_input_dialog, view -> {
                        PayPwdEditText payPwdEditText = view.findViewById(R.id.PayPwdEditText);
                        payPwdEditText.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.grey, R.color.grey, 20);
                        payPwdEditText.setOnTextFinishListener(str -> Toast.makeText(PayActivity.this, str, Toast.LENGTH_SHORT).show());

                        RecyclerView num_recyclview = view.findViewById(R.id.num_recyclview);
                        JgridLayoutManager layoutManager = new JgridLayoutManager(PayActivity.this, 3);
                        num_recyclview.setLayoutManager(layoutManager);
                        JAdapter<String> adapter = new JAdapter<>(PayActivity.this, num_recyclview, new int[]{R.layout.num_item}, new JAdapter.adapterListener<String>() {
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
                }else{
                    payDialog.show();
                }
            }else{
                Toast.makeText(PayActivity.this,"暂不支持此方式支付",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUnCheck(int tag) {
        switch (tag) {
            case 1:
                walletPay.setChecked(false);
                break;
            case 2:
                wechatPay.setChecked(false);
                break;
            case 3:
                aliPay.setChecked(false);
                break;
        }

    }

    @Override
    public void destroy() {

    }
}