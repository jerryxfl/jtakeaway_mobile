package com.jerry.jtakeaway.ui.user.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Nuser;
import com.jerry.jtakeaway.bean.model.TIButton;
import com.jerry.jtakeaway.bean.responseBean.ResponseUser;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.eventBusEvents.PagePositionEvent;
import com.jerry.jtakeaway.ui.generalActivity.LoginActivity;
import com.jerry.jtakeaway.ui.user.activity.ExtractMoneyActivity;
import com.jerry.jtakeaway.ui.user.activity.InvestActivity;
import com.jerry.jtakeaway.ui.user.activity.OpenPaymentActivity;
import com.jerry.jtakeaway.ui.user.activity.SettingActivity;
import com.jerry.jtakeaway.ui.user.activity.WalletActivity;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonalFragment extends BaseFragment {
    @BindView(R.id.oder_recyclerview)
    RecyclerView oder_recyclerview;

    @BindView(R.id.wallet_recyclerview)
    RecyclerView wallet_recyclerview;


    @BindView(R.id.wallet_btn)
    LinearLayout wallet_btn;

    @BindView(R.id.userHeadImg)
    CircleImageView userHeadImg;

    @BindView(R.id.userNickName)
    TextView userNickName;

    @BindView(R.id.bg_img)
    ImageView bg_img;

    @BindView(R.id.settingAib)
    AniImgButton settingAib;

    @BindView(R.id.all_order)
    TextView all_order;

    private JAdapter<TIButton> jAdapterWallet;
    private JAdapter<TIButton> jAdapterOrder;


    @Override
    public int getLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void InitView() {
        //订单
        JgridLayoutManager jgridLayoutManager_order = new JgridLayoutManager(context, 4);
        oder_recyclerview.setLayoutManager(jgridLayoutManager_order);
        jAdapterOrder = new JAdapter<>(context, oder_recyclerview, new int[]{R.layout.tibutton_item}, new JAdapter.adapterListener<TIButton>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<TIButton> datas) {
                LinearLayout container = holder.getView(R.id.container);
                ImageView img = holder.getView(R.id.img);
                TextView text = holder.getView(R.id.text);
                img.setImageDrawable(ContextCompat.getDrawable(context, datas.get(position).getImg()));
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


        //钱包
        JgridLayoutManager jgridLayoutManager_wallet = new JgridLayoutManager(context, 4);
        wallet_recyclerview.setLayoutManager(jgridLayoutManager_wallet);
        jAdapterWallet = new JAdapter<>(context, wallet_recyclerview, new int[]{R.layout.tibutton_item}, new JAdapter.adapterListener<TIButton>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<TIButton> datas) {
                LinearLayout container = holder.getView(R.id.container);
                ImageView img = holder.getView(R.id.img);
                TextView text = holder.getView(R.id.text);
                img.setImageDrawable(ContextCompat.getDrawable(context, datas.get(position).getImg()));
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
        SignEventBus();
        List<TIButton> orders = new ArrayList<>();
        orders.add(new TIButton(R.drawable.on_send, "进行中", () -> {
            EventBus.getDefault().post(new PagePositionEvent(1,1));
        }));
        orders.add(new TIButton(R.drawable.complete, "已完成", () -> {
            EventBus.getDefault().post(new PagePositionEvent(1,2));
        }));
        orders.add(new TIButton(R.drawable.wait_commment, "待评价", () -> {
            EventBus.getDefault().post(new PagePositionEvent(1,3));
        }));
        orders.add(new TIButton(R.drawable.refund, "退款/售后", () -> {
            EventBus.getDefault().post(new PagePositionEvent(1,4));
        }));
        jAdapterOrder.adapter.setData(orders);

        List<TIButton> wallets = new ArrayList<>();
        wallets.add(new TIButton(R.drawable.invest, "充值", () -> {
            startActivity(new Intent(context, InvestActivity.class));
        }));
        wallets.add(new TIButton(R.drawable.wallet, "提现", () -> {
            startActivity(new Intent(context, ExtractMoneyActivity.class));
        }));
        wallets.add(new TIButton(R.drawable.transaction, "交易记录", () -> {

        }));
        wallets.add(new TIButton(R.drawable.pay_password, "支密修改", () -> {

        }));

        jAdapterWallet.adapter.setData(wallets);
    }


    @Override
    public void InitListener() {
        all_order.setOnClickListener(v -> {
            EventBus.getDefault().post(new PagePositionEvent(1,0));
        });
        wallet_btn.setOnClickListener(v -> {
            if (UserUtils.getInstance().getUser() != null) {
                if (UserUtils.getInstance().getUser().getUsertype() == 0) {
                    Nuser nuser = UserUtils.getInstance().getUserDetails(Nuser.class);
                    if (nuser.getWallet() == null) {
                        //未开通钱包
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("你还未开通钱包功能,是否前去开通?")
                                .setConfirmText("是的")
                                .setConfirmClickListener(sDialog -> {
                                    startActivity(new Intent(context, OpenPaymentActivity.class));
                                    sDialog.dismissWithAnimation();
                                })
                                .setCancelText("不了")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    } else {
                        startActivity(new Intent(context, WalletActivity.class));
                    }
                } else {
                    startActivity(new Intent(context, WalletActivity.class));
                }
            } else {
                Toast.makeText(context, "用户信息失效,重新登录", Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
            }
        });

        settingAib.setOnClickListener(v -> {
            startActivity(new Intent(context, SettingActivity.class));
        });
    }

    @Override
    public void destroy() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void LoginEvent(ResponseUser responseUser) {
        System.out.println(UserUtils.getInstance().getUser().getUseradvatar() + " :::: " + UserUtils.getInstance().getUser().getUsernickname());
        Glide.with(context)
                .load(responseUser.getUseradvatar())
                .into(userHeadImg);
        userNickName.setText(responseUser.getUsernickname());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChange(String tag) {
        if (tag.equals("userChange")) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        OkHttp3Util.GET(JUrl.user_info, context, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "链接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if (result.getCode() == 10000) {
                    System.out.println("用户信息:" + result.getData().toString());
                    UserUtils.getInstance().setUser(GsonUtil.gsonToBean(result.getData().toString(), ResponseUser.class));
                    EventBus.getDefault().postSticky(UserUtils.getInstance().getUser());
                    EventBus.getDefault().post("userChangeSuccess");
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}