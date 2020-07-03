package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ConponGetActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.conpon_recyclview)
    RecyclerView conpon_recyclview;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private JAdapter<Coupon> couponJAdapter;

    private List<Coupon> couponList = new ArrayList<>();
    private List<Coupon> couponHaveList = new ArrayList<>();
    private boolean[] booleans;
    private int targetid = -1;

    @Override
    public int getLayout() {
        return R.layout.activity_conpon_get;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conpon_recyclview.setLayoutManager(layoutManager);
        couponJAdapter = new JAdapter<>(this, conpon_recyclview, new int[]{R.layout.conpon_pay_item}, new JAdapter.adapterListener<Coupon>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Coupon> datas) {
                TextView discount = holder.getView(R.id.discount);//折扣
                TextView name = holder.getView(R.id.name);//名字
                TextView failtime = holder.getView(R.id.failtime);//过期时间

                discount.setText(datas.get(position).getConponprice()+"折");
                name.setText(datas.get(position).getConpondesc());
                failtime.setText(datas.get(position).getConponfailuretime()+"到期");

                Button user_conpon_btn = holder.getView(R.id.user_conpon_btn);//领取按钮
                if(booleans[position]){
                    user_conpon_btn.setText("已领取");
                    user_conpon_btn.setBackground(ContextCompat.getDrawable(ConponGetActivity.this,R.drawable.conpon_btn_already_shape));
                }

                user_conpon_btn.setText("立即领取");
                user_conpon_btn.setOnClickListener(v -> {
                    getConpon(datas.get(position).getId(),position);
                });



            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Coupon> datas) {
                Button user_conpon_btn = holder.getView(R.id.user_conpon_btn);//领取按钮
                for(Object payload : payloads){
                    switch (String.valueOf(payload)){
                        case "change":
                            if(booleans[position]){
                                System.out.println("修改领取成功");
                                user_conpon_btn.setText("已领取");
                                user_conpon_btn.setBackground(ContextCompat.getDrawable(ConponGetActivity.this,R.drawable.conpon_btn_already_shape));
                            }
                            break;
                    }
                }
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
        targetid = intent.getIntExtra("targetid",-1);

        getUsefulCoupons();
    }

    private void getConpon(int conponid,int position){
        //领取优惠卷
        OkHttp3Util.GET(JUrl.g_conpon+conponid, this, new Callback() {
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
                    System.out.println("领取成功");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        booleans[position] = true;
                        couponJAdapter.adapter.notifyItemChanged(position,"change");
                    });
                }else if(result.getCode()==6){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"未登录",Toast.LENGTH_SHORT).show();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getHaveConpon(){

        OkHttp3Util.GET(JUrl.m_conpon, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if(result.getCode() == 10000){
                    System.out.println("已领取:"+result.getData().toString());
                    couponHaveList.addAll(GsonUtil.jsonToList(result.getData().toString(),Coupon.class));
                    List<Coupon> Temporary = new ArrayList<>();
                    for (int i = 0; i < couponList.size(); i++) {
                        boolean found = false;
                        for (int j = 0; j < couponHaveList.size(); j++) {
                            if(couponList.get(i).getId() == couponHaveList.get(j).getId()) found = true;
                        }
                        if(!found)Temporary.add(couponList.get(i));
                    }
                    couponList = new ArrayList<>();
                    couponList = Temporary;
                    booleans = new boolean[couponList.size()];
                    for (int i = 0; i < booleans.length; i++) {
                        booleans[i] =false;
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        couponJAdapter.adapter.setHeader(couponList);
                        refresh.setRefreshing(false);
                    });
                }else if(result.getCode()==6){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"未登录",Toast.LENGTH_SHORT).show();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });

    }


    private void getUsefulCoupons() {
        OkHttp3Util.GET(JUrl.u_conpons, this, new Callback() {
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
                    System.out.println("未领取"+result.getData().toString());
                    couponList.addAll(GsonUtil.jsonToList(result.getData().toString(),Coupon.class));
                    List<Coupon> coupons = new ArrayList<Coupon>();
                    for (Coupon coupon : couponList){
                        if(targetid != -1){
                            if(coupon.getConpontarget()==null||coupon.getConpontarget()==targetid){
                                couponList.add(coupon);
                            }
                        }else{
                            couponList.add(coupon);
                        }
                    }
                    getHaveConpon();
                }else if(result.getCode()==6){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"未登录",Toast.LENGTH_SHORT).show();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ConponGetActivity.this,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });

    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        refresh.setOnRefreshListener(() -> getUsefulCoupons());
        conpon_recyclview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refresh.setEnabled(topRowVerticalPosition >= 0 && recyclerView != null && !recyclerView.canScrollVertically(-1));

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void destroy() {

    }
}