package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.bean.responseBean.ShopHaveMenu;
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
import per.wsj.library.AndRatingBar;

public class AllShopActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;


    @BindView(R.id.return_aib)
    AniImgButton return_aib;


    @BindView(R.id.shopRecyclerView)
    RecyclerView shopRecyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private JAdapter<ShopHaveMenu> shopJadapter;
    List<ShopHaveMenu> shopHaveMenuList = new ArrayList<ShopHaveMenu>();

    @Override
    public int getLayout() {
        return R.layout.activity_all_shop;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shopRecyclerView.setLayoutManager(layoutManager);
        shopJadapter = new JAdapter<ShopHaveMenu>(this, shopRecyclerView, new int[]{R.layout.shop_item}, new JAdapter.adapterListener<ShopHaveMenu>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ShopHaveMenu> datas) {
                ImageView shop_image = holder.getView(R.id.shop_image);
                LinearLayout container = holder.getView(R.id.container);
                CardView shop_image_wrapper = holder.getView(R.id.shop_image_wrapper);
                TextView shopname_tv = holder.getView(R.id.shopname_tv);
                AndRatingBar shopleve_rating = holder.getView(R.id.shopleve_rating);
                TextView decr_tv = holder.getView(R.id.decr_tv);
                if(datas.get(position).getMenu()!=null){
                    Glide.with(AllShopActivity.this)
                            .load(datas.get(position).getMenu().getFoodimg())
                            .into(shop_image);
                }else{
                    shop_image_wrapper.setVisibility(View.GONE);
                }
                if(datas.get(position).getSuser().getLevel()>5){
                    shopleve_rating.setRating(5);
                }else{
                    shopleve_rating.setRating((float) datas.get(position).getSuser().getLevel());
                }

                shopname_tv.setText(datas.get(position).getSuser().getShopname());
                decr_tv.setText(datas.get(position).getSuser().getDscr());

                container.setOnClickListener(v -> {
                    Intent intent = new Intent(AllShopActivity.this, ShopActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", datas.get(position).getSuser());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<ShopHaveMenu> datas) {

            }

            @Override
            public int getViewType(List<ShopHaveMenu> datas, int position) {
                return 0;
            }
        });


    }

    @Override
    public void InitData() {
        getShops();
    }

    private void getShops() {
        OkHttp3Util.GET(JUrl.shops(shopHaveMenuList.size()), this, new Callback() {
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
                    System.out.println("商家:" + result.getData());
                    List<ShopHaveMenu> datas = GsonUtil.parserJsonToArrayBeans(result.getData().toString(),ShopHaveMenu.class);
                    shopHaveMenuList.addAll(datas);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        shopJadapter.adapter.setFooter(datas);
                        refresh.setRefreshing(false);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(AllShopActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        refresh.setOnRefreshListener(this::getShops);
        shopRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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