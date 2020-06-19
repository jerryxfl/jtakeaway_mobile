package com.jerry.jtakeaway.ui.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.interfaces.RefreshListener;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Broadcasts;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Slide;
import com.jerry.jtakeaway.bean.events.PageEvents;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.ui.user.activity.MenuActivity;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@SuppressWarnings("all")
public class HomePageFragment extends BaseFragment{
    @BindView(R.id.goRefreshLayout)
    GoRefreshLayout goRefreshLayout;
    @BindView(R.id.location_tv)
    TextView location_tv;
    @BindView(R.id.serachview)
    SearchView serachview;
    @BindView(R.id.location_ain)
    AniImgButton location_ain;
    @BindView(R.id.recyclerview_banner)
    RecyclerView recyclerview_banner;
    @BindView(R.id.recyclerview_hot_shop)
    RecyclerView recyclerview_hot_shop;
    @BindView(R.id.menu_recommend)
    RecyclerView menu_recommend_recyclerview;
    @BindView(R.id.JRollingTextView)
    MarqueeView JRollingTextView;

    @BindView(R.id.five_shop_recyclerview)
    RecyclerView five_shop_recyclerview;

    @BindView(R.id.people_red_recyclerview)
    RecyclerView people_red_recyclerview;

    private CarouselLayoutManager layoutManager;
    private Timer timer = new Timer();
    private int currentPosition;
    private List<Integer> datas;
    private JAdapter<Integer> hotShopAdapter;
    private JAdapter<Slide> jBannerAdapter;
    private JAdapter<Menus> jMneuAdapter;
    private JAdapter<Integer> peopleRedAdapter;
    private JAdapter<Integer> fiveLevelAdapter;
    List<Menus> menusList = new ArrayList<Menus>();
    List<Slide> slideList = new ArrayList<Slide>();

    private List<Broadcasts> broadcastsList = new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void InitView() {
//        KeyboardDismisser.useWith(this);
//        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
//            GPSUtils.getInstance(context).getLngAndLat(new GPSUtils.OnLocationResultListener() {
//                @Override
//                public void onLocationResult(Location location) {
//                    System.out.println("位置:");
//                    String[] location1 = GPSUtils.getInstance(context).getLocation(context, location.getLatitude(), location.getLongitude());
//                    for (int i = 0; i < location1.length; i++) {
//                        System.out.println(location1[i]);
//                    }
//                }
//
//                @Override
//                public void OnLocationChange(Location location) {
//                    System.out.println("位置:");
//                    System.out.println(location.getLatitude());
//
//                }
//            });
//        }else{
//            Toast.makeText(context,"没有定位权限",Toast.LENGTH_SHORT).show();
//        }


        //顶部轮播图--------------------****************---------------------------------
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setMaxVisibleItems(1);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());//监听器
        recyclerview_banner.setLayoutManager(layoutManager);
        recyclerview_banner.setHasFixedSize(true);
        recyclerview_banner.addOnScrollListener(new CenterScrollListener());//监听器
        currentPosition = 0;

        jBannerAdapter = new JAdapter<Slide>(context, recyclerview_banner, new int[]{R.layout.banner_item_layout}, new JAdapter.adapterListener<Slide>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Slide> datas) {

                Glide.with(context)
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
//        recyclerview_banner.smoothScrollToPosition(datas.size() / 2);
        //end   顶部轮播图--------------------****************---------------------------------



        //热门商家--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_hotShop = new JgridLayoutManager(context,5);
        recyclerview_hot_shop.setLayoutManager(jgridLayoutManager_hotShop);

        hotShopAdapter = new JAdapter<Integer>(context, recyclerview_hot_shop, new int[]{R.layout.hot_shop_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.shop_image));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads,List<Integer> datas) {

            }

            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });
        //end 热门商家--------------------****************---------------------------------




        //菜单推荐--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_menu = new JgridLayoutManager(context,2);
        menu_recommend_recyclerview.setLayoutManager(jgridLayoutManager_menu);
        jMneuAdapter = new JAdapter<Menus>(context, menu_recommend_recyclerview, new int[]{R.layout.menu_item}, new JAdapter.adapterListener<Menus>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Menus> datas) {
                ImageView menu_img = holder.getView(R.id.menu_img);
                Glide.with(context)
                        .load(datas.get(position).getFoodimg())
                        .into(menu_img);

                menu_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //精品菜单推荐
                        Intent intent = new Intent(context, MenuActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("menu",datas.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads,List<Menus> datas) {

            }

            @Override
            public int getViewType(List<Menus> datas, int position) {
                return 0;
            }
        });
        //end menu_recommend--------------------****************---------------------------------




        //five_shop_recommend--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_five_shop = new JgridLayoutManager(context,1);
        five_shop_recyclerview.setLayoutManager(jgridLayoutManager_five_shop);
        fiveLevelAdapter = new JAdapter<Integer>(context, five_shop_recyclerview, new int[]{R.layout.shop_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.shop_image));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads,List<Integer> datas) {

            }

            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });
        //end  five_shop_recommend--------------------****************---------------------------------




        //people_red_recommend--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_people_red = new JgridLayoutManager(context,1);
        people_red_recyclerview.setLayoutManager(jgridLayoutManager_people_red);
        peopleRedAdapter = new JAdapter<Integer>(context, people_red_recyclerview, new int[]{R.layout.shop_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.shop_image));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads,List<Integer> datas) {

            }

            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });

        //end  prople_red_recommend--------------------****************---------------------------------
    }



    class timerTask extends TimerTask {
        @Override
        public void run() {
            if(currentPosition+1>slideList.size()){
                currentPosition = 0;
            }else{
                currentPosition = currentPosition+1;
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(recyclerview_banner!=null)recyclerview_banner.smoothScrollToPosition(currentPosition);
                }
            });
        }
    };

    @Override
    public void InitData() {
        getSlider();
        getBorCasts();
        getHotMenus();
        //hot_shop
        List<Integer> datass = new ArrayList<Integer>();
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.hot_art);
        datass.add(R.drawable.hot_art);
        datass.add(R.drawable.dribbble_music_corner);
        datass.add(R.drawable.dribbble_music_corner);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        hotShopAdapter.adapter.setHeader(datass);
        //end hot_shop





        //五星
        fiveLevelAdapter.adapter.setHeader(datass);
        //end五星



        //网红
        peopleRedAdapter.adapter.setHeader(datass);
        //end 网红
    }

    private void getHotMenus(){
        OkHttp3Util.GET(JUrl.hot_menus, context, new Callback() {
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
                    menusList.clear();
                    menusList.addAll(GsonUtil.jsonToList(result.getData().toString(),Menus.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jMneuAdapter.adapter.setHeader(menusList);
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getSlider() {
        OkHttp3Util.GET(JUrl.top_slides, context, new Callback() {
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
                    slideList.clear();
                    slideList.addAll(GsonUtil.jsonToList(result.getData().toString(),Slide.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jBannerAdapter.adapter.setHeader(slideList);
                        timer.schedule(new timerTask(),0,5000);
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getBorCasts() {
        OkHttp3Util.GET(JUrl.broadcasts, context, new Callback() {

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
                    broadcastsList.clear();
                    broadcastsList.addAll(GsonUtil.jsonToList(result.getData().toString(),Broadcasts.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(context, broadcastsList);
                        JRollingTextView.setAdapter(simpleTextAdapter);
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context,"数据错误",Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    public class SimpleTextAdapter extends CommonAdapter<Broadcasts> {

        public SimpleTextAdapter(Context context, List<Broadcasts> datas) {
            super(context, R.layout.broadcasts_item, datas);
        }

        @Override
        protected void convert(ViewHolder viewHolder, Broadcasts item, int position) {
            TextView tv = viewHolder.getView(R.id.msg);
            tv.setText(item.getContent());
        }

    }

    @Override
    public void InitListener() {
        recyclerview_banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    EventBus.getDefault().postSticky(new PageEvents(false));
                }
                return false;
            }
        });

        recyclerview_banner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        goRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    EventBus.getDefault().postSticky(new PageEvents(true));
                }
                return false;
            }
        });
        // 设置下拉监听
        goRefreshLayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                getSlider();
                getBorCasts();
                getHotMenus();
                //添加你自己的代码
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  结束刷新
                        goRefreshLayout.finishRefresh();
                    }
                },3000);
            }
        });
    }

    @Override
    public void destroy() {
        timer.cancel();
    }


}