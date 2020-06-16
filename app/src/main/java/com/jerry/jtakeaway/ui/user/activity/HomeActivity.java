package com.jerry.jtakeaway.ui.user.activity;

import android.Manifest;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.events.PageEvents;
import com.jerry.jtakeaway.custom.JViewPager;
import com.jerry.jtakeaway.ui.user.adapter.ViewPagerAdapter;
import com.jerry.jtakeaway.ui.user.fragment.EmailFragment;
import com.jerry.jtakeaway.ui.user.fragment.HomePageFragment;
import com.jerry.jtakeaway.ui.user.fragment.OrderFragment;
import com.jerry.jtakeaway.ui.user.fragment.PersonalFragment;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    JViewPager viewPager;
    @BindView(R.id.top)
    View top;
    @BindView(R.id.tabbar)
    JPTabBar tabbar;

    @Titles
    private static final String[] mTitles = {"首页", "订单", "消息", "个人"};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.homepage_normal, R.drawable.orderform_normal, R.drawable.information_normal, R.drawable.me_normal};

    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.homepage_selector, R.drawable.orderform_selector, R.drawable.information_selector, R.drawable.me_selector};

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void InitView() {
        SignEventBus();
        //申请权限 放入权限数组,记得再manifests申请
        RequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION});
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomePageFragment());
        fragments.add(new OrderFragment());
        fragments.add(new EmailFragment());
        fragments.add(new PersonalFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        tabbar.setContainer(viewPager);
        tabbar.setPageAnimateEnable(true);
        tabbar.setSelectedColor(Color.parseColor("#fa8c16"));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PageChangeEvent(PageEvents events){
        viewPager.setScrollble(events.isCanScroll());
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabbar.setSelectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void destroy() {

    }


}