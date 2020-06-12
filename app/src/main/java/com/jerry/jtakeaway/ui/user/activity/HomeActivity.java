package com.jerry.jtakeaway.ui.user.activity;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.top)
    View top;
    @BindView(R.id.tabbar)
    JPTabBar tabbar;

    @Titles
    private static final String[] mTitles = {"首页","订单","消息","个人"};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.store,R.drawable.cat,R.drawable.tip,R.drawable.persion};

    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.store_s, R.drawable.cat_s, R.drawable.tip_s, R.drawable.persion_s};

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomePageFragment());
        fragments.add(new OrderFragment());
        fragments.add(new EmailFragment());
        fragments.add(new PersonalFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        tabbar.setContainer(viewPager);
        tabbar.setPageAnimateEnable(true);
        tabbar.setSelectedColor(Color.parseColor("#fa8c16"));

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