package com.jerry.jtakeaway.ui.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Slide;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.model.Barrage;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBarrageView;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
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

@SuppressLint("all")
public class ShopActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.banner_recyclerview)
    RecyclerView banner_recyclerview;

    @BindView(R.id.Indicator)
    TextView indicator;

    @BindView(R.id.banner)
    RelativeLayout banner;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.shopAddress)
    TextView shopAddress;

    @BindView(R.id.shopName)
    TextView shopName;

    @BindView(R.id.shopleve_rating)
    AndRatingBar shopleve_rating;


    @BindView(R.id.comment_tv)
    TextView comment_tv;

    @BindView(R.id.menu_recyclerview)
    RecyclerView menu_recyclerview;



    private Suser suser;
    private JAdapter<Slide> bannerAdapter;
    private List<Slide> slideList = new ArrayList<Slide>();
    private LinearLayoutManager banner_layoutManager;
    private JAdapter<Menus> menusJAdapter;
    private List<Menus> menusList = new ArrayList<Menus>();
    private List<Barrage> barrageList = new ArrayList<Barrage>();

    @Override
    public int getLayout() {
        return R.layout.activity_shop;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        banner_layoutManager = new LinearLayoutManager(this);
        banner_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        banner_recyclerview.setLayoutManager(banner_layoutManager);
        //单页滑动
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(banner_recyclerview);
        bannerAdapter = new JAdapter<>(this, banner_recyclerview, new int[]{R.layout.banner_item_shop_layout}, new JAdapter.adapterListener<Slide>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Slide> datas) {
                Glide.with(ShopActivity.this)
                        .load(datas.get(position).getImg())
                        .into((ImageView) holder.getView(R.id.banner_img));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Slide> datas) {

            }

            @Override
            public int getViewType(List<Slide> datas, int position) {
                return 0;
            }
        });


        //菜单
        JgridLayoutManager jgridLayoutManager_menu = new JgridLayoutManager(this,1);
        menu_recyclerview.setLayoutManager(jgridLayoutManager_menu);
        menusJAdapter = new JAdapter<>(this, menu_recyclerview, new int[]{R.layout.shop_menu_item}, new JAdapter.adapterListener<Menus>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Menus> datas) {
                ImageView menuImg = holder.getView(R.id.menuImg);
                TextView menuName = holder.getView(R.id.menuName);
                TextView menuDescr = holder.getView(R.id.menuDescr);
                LinearLayout container = holder.getView(R.id.container);

                Glide.with(ShopActivity.this)
                        .load(datas.get(position).getFoodimg())
                        .into(menuImg);
                menuName.setText(datas.get(position).getFoodname());
                menuDescr.setText(datas.get(position).getFooddesc());
                container.setOnClickListener(v -> {
                    Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("menu",datas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Menus> datas) {

            }

            @Override
            public int getViewType(List<Menus> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        suser = (Suser) intent.getSerializableExtra("shop");
        setData(suser);
        getSlider(suser.getId());//获得轮播图
        getMenus(suser.getId());


        setBarrage();

    }

    private void setBarrage() {
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
        barrageList.add(new Barrage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593362256047&di=1f93b8b93571553aced52f141f98c696&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F0001223938-4.jpg","jerry","你们好啊"));
    }




    //获得商家菜单
    private void getMenus(int id) {
        OkHttp3Util.GET(JUrl.shop_menu(id,menusList.size()), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    menusList.addAll(GsonUtil.jsonToList(result.getData().toString(),Menus.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        menusJAdapter.adapter.setFooter(menusList);
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ShopActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }

    private void setData(Suser suser){
        shopName.setText(suser.getShopname());
        shopAddress.setText(suser.getShopaddress());
        if(suser.getLevel()>5){
            shopleve_rating.setRating(5);
        }else{
            shopleve_rating.setRating((float) suser.getLevel());
        }
    }

    private void getSlider(int suserid) {
        OkHttp3Util.GET(JUrl.shop_slide(suserid), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    slideList.addAll(GsonUtil.jsonToList(result.getData().toString(),Slide.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if(slideList.isEmpty()){
                            banner.setVisibility(View.GONE);
                        }else{
                            bannerAdapter.adapter.setData(slideList);
                        }
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ShopActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void InitListener() {
        banner_recyclerview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            indicator.setText(" "+(banner_layoutManager.findFirstVisibleItemPosition()+1)+"/"+slideList.size()+" ");
        });
        comment_tv.setOnClickListener(v -> {

        });
        return_aib.setOnClickListener(v -> finish());
    }

    @Override
    public void destroy() {
    }
}