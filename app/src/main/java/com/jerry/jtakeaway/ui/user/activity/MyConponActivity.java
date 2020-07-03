package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyConponActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;


    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.conponRecyvlerView)
    RecyclerView conponRecyvlerView;


    @BindView(R.id.conponGetBtn)
    TextView conponGetBtn;

    private List<Coupon> couponList = new ArrayList<>();
    private JAdapter<Coupon> couponJAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_my_conpon;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conponRecyvlerView.setLayoutManager(layoutManager);
        couponJAdapter = new JAdapter<>(this, conponRecyvlerView, new int[]{R.layout.conpon_pay_item}, new JAdapter.adapterListener<Coupon>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Coupon> datas) {
                TextView discount = holder.getView(R.id.discount);//折扣
                TextView name = holder.getView(R.id.name);//名字
                TextView failtime = holder.getView(R.id.failtime);//过期时间
                Button user_conpon_btn = holder.getView(R.id.user_conpon_btn);//过期时间

                discount.setText(datas.get(position).getConponprice() + "折");
                name.setText(datas.get(position).getConpondesc());
                failtime.setText(datas.get(position).getConponfailuretime() + "到期");

                Date now  = new Date();
                Date failTime = datas.get(position).getConponfailuretime();
                if(now.before(failTime)){
                    //未过期
                    user_conpon_btn.setText("立即使用");
                    user_conpon_btn.setEnabled(true);
                    user_conpon_btn.setOnClickListener(v -> {

                    });
                }else{
                    //过期
                    user_conpon_btn.setText("已过期");
                    user_conpon_btn.setEnabled(false);
                }
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

    private void getConpon() {
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
                if (result.getCode() == 10000) {
                    System.out.println(result.getData().toString());
                    couponList.clear();
                    couponList.addAll(GsonUtil.jsonToList(result.getData().toString(), Coupon.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        couponJAdapter.adapter.setHeader(couponList);
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MyConponActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MyConponActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        conponGetBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MyConponActivity.this, ConponGetActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void destroy() {

    }
}