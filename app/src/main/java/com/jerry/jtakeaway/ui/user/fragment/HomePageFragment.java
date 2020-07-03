package com.jerry.jtakeaway.ui.user.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
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
import com.jerry.jtakeaway.bean.responseBean.ShopHaveMenu;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.ui.user.activity.AllShopActivity;
import com.jerry.jtakeaway.ui.user.activity.MenuActivity;
import com.jerry.jtakeaway.ui.user.activity.ShopActivity;
import com.jerry.jtakeaway.utils.GPSUtils;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import per.wsj.library.AndRatingBar;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@SuppressWarnings("all")
@RuntimePermissions
public class HomePageFragment extends BaseFragment {
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

    @BindView(R.id.more)
    Button more;

    @BindView(R.id.more1)
    Button more1;

    private CarouselLayoutManager layoutManager;
    private Timer timer;
    private int currentPosition;
    private JAdapter<Menus> hotShopAdapter;
    private JAdapter<Slide> jBannerAdapter;
    private JAdapter<Menus> jMneuAdapter;
    private JAdapter<ShopHaveMenu> peopleRedAdapter;
    private JAdapter<ShopHaveMenu> fiveLevelAdapter;
    List<Menus> menusList = new ArrayList<Menus>();
    List<Slide> slideList = new ArrayList<Slide>();
    List<Menus> hot_shop_menuList = new ArrayList<Menus>();
    List<ShopHaveMenu> five_shopList = new ArrayList<ShopHaveMenu>();
    List<ShopHaveMenu> red_people_shopList = new ArrayList<ShopHaveMenu>();

    private List<Broadcasts> broadcastsList = new ArrayList<>();

    private String mLocation ="";


    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void InitView() {
        HomePageFragmentPermissionsDispatcher.getLocationWithPermissionCheck(this);
        getLocation();


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
        JgridLayoutManager jgridLayoutManager_hotShop = new JgridLayoutManager(context, 5);
        recyclerview_hot_shop.setLayoutManager(jgridLayoutManager_hotShop);

        hotShopAdapter = new JAdapter<Menus>(context, recyclerview_hot_shop, new int[]{R.layout.hot_shop_item}, new JAdapter.adapterListener<Menus>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Menus> datas) {
                TextView title_tv = holder.getView(R.id.title_tv);
                ImageView shop_image = (ImageView) holder.getView(R.id.shop_image);
                Glide.with(context)
                        .load(datas.get(position).getFoodimg())
                        .into(shop_image);
                title_tv.setText(datas.get(position).getFoodname());
                shop_image.setOnClickListener(v -> {
                    Intent intent = new Intent(context, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("menu", datas.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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
        //end 热门商家--------------------****************---------------------------------


        //菜单推荐--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_menu = new JgridLayoutManager(context, 2);
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
                        bundle.putSerializable("menu", datas.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
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
        //end menu_recommend--------------------****************---------------------------------


        //five_shop_recommend--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_five_shop = new JgridLayoutManager(context, 1);
        five_shop_recyclerview.setLayoutManager(jgridLayoutManager_five_shop);
        fiveLevelAdapter = new JAdapter<ShopHaveMenu>(context, five_shop_recyclerview, new int[]{R.layout.shop_item}, new JAdapter.adapterListener<ShopHaveMenu>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ShopHaveMenu> datas) {
                ImageView shop_image = holder.getView(R.id.shop_image);
                LinearLayout container = holder.getView(R.id.container);
                CardView shop_image_wrapper = holder.getView(R.id.shop_image_wrapper);
                TextView shopname_tv = holder.getView(R.id.shopname_tv);
                AndRatingBar shopleve_rating = holder.getView(R.id.shopleve_rating);
                TextView decr_tv = holder.getView(R.id.decr_tv);
                if(datas.get(position).getMenu()!=null){
                    Glide.with(context)
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
                    Intent intent = new Intent(context, ShopActivity.class);
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
        //end  five_shop_recommend--------------------****************---------------------------------


        //people_red_recommend--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_people_red = new JgridLayoutManager(context, 1);
        people_red_recyclerview.setLayoutManager(jgridLayoutManager_people_red);
        peopleRedAdapter = new JAdapter<ShopHaveMenu>(context, people_red_recyclerview, new int[]{R.layout.shop_item}, new JAdapter.adapterListener<ShopHaveMenu>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ShopHaveMenu> datas) {
                ImageView shop_image = holder.getView(R.id.shop_image);
                LinearLayout container = holder.getView(R.id.container);
                CardView shop_image_wrapper = holder.getView(R.id.shop_image_wrapper);
                TextView shopname_tv = holder.getView(R.id.shopname_tv);
                AndRatingBar shopleve_rating = holder.getView(R.id.shopleve_rating);
                TextView decr_tv = holder.getView(R.id.decr_tv);
                if(datas.get(position).getMenu()!=null){
                    Glide.with(context)
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
                    Intent intent = new Intent(context, ShopActivity.class);
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

        //end  prople_red_recommend--------------------****************---------------------------------
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void getLocation() {
        GPSUtils.getInstance(context).getLngAndLat(new GPSUtils.OnLocationResultListener() {
            @Override
            public void onLocationResult(Location location) {
                String[] location1 = GPSUtils.getInstance(context).getLocation(context, location.getLatitude(), location.getLongitude());
                for (int i = 0; i < location1.length; i++) {
                    System.out.println(location1[i]);
                    mLocation = mLocation + " " + location1[i];
                    if(i==2){
                        if(location_tv!=null)location_tv.setText(location1[i]);
                    }
                }
            }

            @Override
            public void OnLocationChange(Location location) {
                String[] location1 = GPSUtils.getInstance(context).getLocation(context, location.getLatitude(), location.getLongitude());
                for (int i = 0; i < location1.length; i++) {
                    System.out.println(location1[i]);
                    mLocation = mLocation + " " + location1[i];
                    if(i==2){
                        if(location_tv!=null)location_tv.setText(location1[i]);
                    }
                }

            }
        });
    }

    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showWhy(final PermissionRequest request) {
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("我们需要此权限才能给你提供附近的店铺信息?")
                .setConfirmText("好")
                .setConfirmClickListener(sDialog -> {
                    request.proceed();
                    sDialog.dismissWithAnimation();
                })
                .setCancelText("拒绝")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        request.cancel();
                        location_tv.setText("无");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showDenied() {
        location_tv.setText("无");
        Toast.makeText(context, "无法获得权限", Toast.LENGTH_SHORT).show();
    }


    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showNeverAskAgain() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("定位权限被拒绝,为了不影响你的使用请前往设置开启权限?")
                .setConfirmText("好")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setCancelText("拒绝")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        HomePageFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    class timerTask extends TimerTask {
        @Override
        public void run() {
            if (currentPosition + 1 > slideList.size()) {
                currentPosition = 0;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerview_banner != null)
                            recyclerview_banner.scrollToPosition(currentPosition);
                    }
                });
            } else {
                currentPosition = currentPosition + 1;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerview_banner != null)
                            recyclerview_banner.smoothScrollToPosition(currentPosition);
                    }
                });
            }

        }
    }

    ;

    @Override
    public void InitData() {
        getSlider();
        getBorCasts();
        getHotMenus();
        getHotShops();
        getFiveShops();
        getRedPeopleShops();
    }

    private void getRedPeopleShops() {
        OkHttp3Util.GET(JUrl.red_people_shop(0), context, new Callback() {
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
                    red_people_shopList.clear();
                    red_people_shopList.addAll(GsonUtil.jsonToList(result.getData().toString(), ShopHaveMenu.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        peopleRedAdapter.adapter.setData(red_people_shopList);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getFiveShops() {
        OkHttp3Util.GET(JUrl.five_level_shop(0), context, new Callback() {
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
                    five_shopList.clear();
                    five_shopList.addAll(GsonUtil.jsonToList(result.getData().toString(), ShopHaveMenu.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        fiveLevelAdapter.adapter.setData(five_shopList);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getHotShops() {
        OkHttp3Util.GET(JUrl.hot_shop_menu, context, new Callback() {
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
                    hot_shop_menuList.clear();
                    hot_shop_menuList.addAll(GsonUtil.jsonToList(result.getData().toString(), Menus.class));
                    System.out.println("热门商家:" + result.getData());
                    if (hot_shop_menuList.size() > 10) {
                        hot_shop_menuList.subList(11, hot_shop_menuList.size()).clear();
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        hotShopAdapter.adapter.setData(hot_shop_menuList);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    private void getHotMenus() {
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
                if (result.getCode() == 10000) {
                    menusList.clear();
                    menusList.addAll(GsonUtil.jsonToList(result.getData().toString(), Menus.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jMneuAdapter.adapter.setData(menusList);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
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
                if (result.getCode() == 10000) {
                    slideList.clear();
                    slideList.addAll(GsonUtil.jsonToList(result.getData().toString(), Slide.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jBannerAdapter.adapter.setData(slideList);
                        if (timer == null) {
                            timer = new Timer();
                            timer.schedule(new timerTask(), 0, 5000);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
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
                if (result.getCode() == 10000) {
                    broadcastsList.clear();
                    broadcastsList.addAll(GsonUtil.jsonToList(result.getData().toString(), Broadcasts.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(context, broadcastsList);
                        JRollingTextView.setAdapter(simpleTextAdapter);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
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
        more.setOnClickListener(v->startActivity(new Intent(context, AllShopActivity.class)));
        more1.setOnClickListener(v->startActivity(new Intent(context, AllShopActivity.class)));

        recyclerview_banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                getHotShops();
                getFiveShops();
                getRedPeopleShops();
                //添加你自己的代码
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  结束刷新
                        goRefreshLayout.finishRefresh();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void destroy() {
        if (timer != null) timer.cancel();
    }


}