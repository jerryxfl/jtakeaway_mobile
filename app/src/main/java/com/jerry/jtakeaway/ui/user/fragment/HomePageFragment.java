package com.jerry.jtakeaway.ui.user.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.jerry.jtakeaway.bean.events.PageEvents;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JRollingTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

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
    JRollingTextView JRollingTextView;

    @BindView(R.id.five_shop_recyclerview)
    RecyclerView five_shop_recyclerview;

    @BindView(R.id.people_red_recyclerview)
    RecyclerView people_red_recyclerview;

    private CarouselLayoutManager layoutManager;
    private Timer timer = new Timer();
    private int currentPosition;
    private List<Integer> datas;
    private JAdapter<Integer> hotShopAdapter;
    private JAdapter<Integer> jBannerAdapter;
    private JAdapter<Integer> jMneuAdapter;


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

        jBannerAdapter = new JAdapter<Integer>(context, recyclerview_banner, new int[]{R.layout.banner_item_layout}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.banner_img));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List payloads) {

            }

            @Override
            public int getViewType(List datas, int position) {
                return 0;
            }
        });

        datas = new ArrayList<Integer>();
        datas.add(R.drawable.concrete_road_between_trees_563356);
        datas.add(R.drawable.hot_art);
        datas.add(R.drawable.dribbble_music_corner);
        datas.add(R.drawable.icon_dark_green_by_milkinside);
        jBannerAdapter.adapter.setHeader(datas);
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
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads) {

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
        jMneuAdapter = new JAdapter<Integer>(context, menu_recommend_recyclerview, new int[]{R.layout.menu_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.menu_img));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads) {

            }

            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });
        //end menu_recommend--------------------****************---------------------------------




        //five_shop_recommend--------------------****************---------------------------------
        
        //end  five_shop_recommend--------------------****************---------------------------------




        //people_red_recommend--------------------****************---------------------------------

        //end  prople_red_recommend--------------------****************---------------------------------


        timer.schedule(new timerTask(),0,5000);
    }



    class timerTask extends TimerTask {
        @Override
        public void run() {
            if(currentPosition+1>datas.size()){
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


        //menu
        jMneuAdapter.adapter.setHeader(datass);
        //end menu
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


    class JgridLayoutManager extends GridLayoutManager {
        public JgridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public JgridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public JgridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }


        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }
}