package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
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

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.requestBean.pay;
import com.jerry.jtakeaway.bean.requestBean.payBean;
import com.jerry.jtakeaway.bean.responseBean.PayMoney;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.custom.PayPwdEditText;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private int payWays = 1;//1钱包支付  2微信支付  3支付宝支付


    private JAdapter<Coupon> conponAdapter;
    private JBottomDialog payDialog;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private int currentPosition = -1;
    private Orde orde;
    private Coupon setConpon;
    private JCenterDialog jCenterDialog;
    private PayMoney mPayMoney;

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

                discount.setText(datas.get(position).getConponprice() + "折");
                name.setText(datas.get(position).getConpondesc());
                failtime.setText(datas.get(position).getConponfailuretime() + "到期");
                if (currentPosition == position) {
                    user_conpon_btn.setText("已选择");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_already_shape));
                } else {
                    user_conpon_btn.setText("立即使用");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_selector));
                }


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        conponAdapter.adapter.notifyItemChanged(currentPosition, "change");
                    }
                }, 500);

                user_conpon_btn.setOnClickListener(v -> {
                    setConpon = datas.get(position);
                    user_conpon_btn.setText("已选择");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(PayActivity.this, R.drawable.conpon_btn_already_shape));
                    if (currentPosition != -1)
                        conponAdapter.adapter.notifyItemChanged(currentPosition);
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
        Intent intent = getIntent();
        orde = (Orde) intent.getSerializableExtra("order");
        System.out.println("支付界面订单编号-------------------------"+orde.getId());

        getConpon();
    }



    private void getConpon() {
        OkHttp3Util.GET(JUrl.shop_can_use_coupon(orde.getSuserid()), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    System.out.println(result.getData().toString());
                    couponList.clear();
                    couponList.addAll(GsonUtil.jsonToList(result.getData().toString(), Coupon.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (conponAdapter != null) conponAdapter.adapter.setFooter(couponList);
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(PayActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(PayActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    //获得直接支付的钱
    private void getPayMoney(int orderid) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        OkHttp3Util.GET(JUrl.pay_money(orderid), this, new Callback() {
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
                    PayMoney payMoney = GsonUtil.gsonToBean(result.getData().toString(),PayMoney.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        showPayDialog(payMoney);
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        System.out.println("出错了-----------------"+result.getMsg());
                        Toast.makeText(PayActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    //获得优惠卷直接支付的钱
    private void getConponPayMoney(int orderid,int conponid) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        OkHttp3Util.GET(JUrl.coupon_pay_money(orderid,conponid), this, new Callback() {
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
                    PayMoney payMoney = GsonUtil.gsonToBean(result.getData().toString(),PayMoney.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        showPayDialog(payMoney);
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        System.out.println("出错了-----------------"+result.getMsg());
                        Toast.makeText(PayActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void showPayDialog(PayMoney paymoney) {
        mPayMoney = paymoney;
        if (payDialog == null) {
            payDialog = new JBottomDialog(this, R.layout.pay_input_dialog, view -> {
                TextView money_tv = view.findViewById(R.id.money_tv);
                PayPwdEditText payPwdEditText = view.findViewById(R.id.PayPwdEditText);
                payPwdEditText.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.grey, R.color.grey, 20);
                money_tv.setText(paymoney.getMoney());
                payPwdEditText.setOnTextFinishListener(str -> {
                    //密码输入完毕触发支付

                    if(setConpon!=null){
                        payBean payBean = new payBean();
                        payBean.setOrdeId(orde.getId());
                        payBean.setPayPassword(payPwdEditText.getPwdText());
                        payBean.setCouponId(setConpon.getId());
                        ConponPay(payBean);
                    }else{
                        com.jerry.jtakeaway.bean.requestBean.pay payBean = new pay();
                        payBean.setOrdeId(orde.getId());
                        payBean.setPayPassword(payPwdEditText.getPwdText());
                        JustPay(payBean);
                    }
                });

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
                                //点击确定触发支付
                                if(setConpon!=null){
                                    payBean payBean = new payBean();
                                    payBean.setOrdeId(orde.getId());
                                    payBean.setPayPassword(payPwdEditText.getPwdText());
                                    payBean.setCouponId(setConpon.getId());
                                    ConponPay(payBean);
                                }else{
                                    com.jerry.jtakeaway.bean.requestBean.pay payBean = new pay();
                                    payBean.setOrdeId(orde.getId());
                                    payBean.setPayPassword(payPwdEditText.getPwdText());
                                    JustPay(payBean);
                                }
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
        } else {
            payDialog.show();
        }
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        walletPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(payWays);
                payWays = 1;
            }
        });
        wechatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(payWays);
                payWays = 2;
            }
        });
        aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setUnCheck(payWays);
                payWays = 3;
            }
        });

        payBtn.setOnClickListener(v -> {
            if (payWays == 1) {
                if(setConpon==null){
                    getPayMoney(orde.getId());
                }else{
                    getConponPayMoney(orde.getId(),setConpon.getId());
                }
            } else {
                Toast.makeText(PayActivity.this, "暂不支持此方式支付", Toast.LENGTH_SHORT).show();
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


    //优惠卷支付
    private void ConponPay(payBean payBean) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        JSONObject json = (JSONObject) JSONObject.toJSON(payBean);
        System.out.println("优惠卷支付:" + json.toString());
        OkHttp3Util.POST(JUrl.conpon_pay, this,json, new Callback() {
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
                    System.out.println(result.toString());
                    Orde orde = GsonUtil.gsonToBean(result.getData().toString(),Orde.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        EventBus.getDefault().post(orde);
                        jCenterDialog.dismiss();
                        System.out.println("----------------------*****************优惠卷支付成功");
                        if(payDialog!=null)payDialog.dismiss();
                        Intent intent = new Intent(PayActivity.this,PaySucessActivity.class);
                        intent.putExtra("payMoney",mPayMoney.getMoney());
                        startActivity(new Intent(intent));
                        finish();
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else if(result.getCode() == 9){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        new SweetAlertDialog(PayActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("余额不足,是否前去充值?")
                                .setConfirmText("好的")
                                .setConfirmClickListener(sDialog -> {
                                    startActivity(new Intent(PayActivity.this, WalletActivity.class));
                                })
                                .setCancelText("不了")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    });
                } else if(result.getCode() == 13) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "支付密码错误", Toast.LENGTH_SHORT).show();
                    });
                }else if(result.getCode() == 8) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        new SweetAlertDialog(PayActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("你还未开通钱包功能,是否前去开通?")
                                .setConfirmText("好的")
                                .setConfirmClickListener(sDialog -> {

                                })
                                .setCancelText("不了")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    });

                }else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        System.out.println("出错了-----------------"+result.getMsg());
                    });
                }

            }
        });
    }


    //直接支付
    private void JustPay(com.jerry.jtakeaway.bean.requestBean.pay  payBean) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        JSONObject json = (JSONObject) JSONObject.toJSON(payBean);
        System.out.println("直接支付:" + json.toString());
        OkHttp3Util.POST(JUrl.pay, this,json, new Callback() {
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
                    System.out.println(result.toString());
                    Orde orde = GsonUtil.gsonToBean(result.getData().toString(),Orde.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        EventBus.getDefault().post(orde);
                        jCenterDialog.dismiss();
                        System.out.println("----------------------*****************直接支付成功");
                        if(payDialog!=null)payDialog.dismiss();
                        Intent intent = new Intent(PayActivity.this,PaySucessActivity.class);
                        intent.putExtra("payMoney",mPayMoney.getMoney());
                        startActivity(new Intent(intent));
                        finish();
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else if(result.getCode() == 9){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        new SweetAlertDialog(PayActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("余额不足,是否前去充值?")
                                .setConfirmText("好的")
                                .setConfirmClickListener(sDialog -> {
                                    startActivity(new Intent(PayActivity.this, WalletActivity.class));
                                })
                                .setCancelText("不了")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    });
                } else if(result.getCode() == 13) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(PayActivity.this, "支付密码错误", Toast.LENGTH_SHORT).show();
                    });
                }else if(result.getCode() == 8) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        new SweetAlertDialog(PayActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("你还未开通钱包功能,是否前去开通?")
                                .setConfirmText("好的")
                                .setConfirmClickListener(sDialog -> {

                                })
                                .setCancelText("不了")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    });

                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        System.out.println("出错了-----------------"+result.getMsg());
                    });
                }

            }
        });
    }

}