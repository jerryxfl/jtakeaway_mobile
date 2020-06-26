package com.jerry.jtakeaway.ui.user.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.events.PageEvents;
import com.jerry.jtakeaway.bean.responseBean.ResponseUser;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JViewPager;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEvent;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEventType;
import com.jerry.jtakeaway.ui.generalActivity.LoginActivity;
import com.jerry.jtakeaway.ui.user.adapter.ViewPagerAdapter;
import com.jerry.jtakeaway.ui.user.fragment.EmailFragment;
import com.jerry.jtakeaway.ui.user.fragment.HomePageFragment;
import com.jerry.jtakeaway.ui.user.fragment.OrderFragment;
import com.jerry.jtakeaway.ui.user.fragment.PersonalFragment;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private JCenterDialog jCenterDialog;

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
        if(UserUtils.getInstance().getUser()==null)Autologin(MMkvUtil.getInstance(HomeActivity.this, "jwts").decodeString("account"),MMkvUtil.getInstance(HomeActivity.this, "jwts").decodeString("password"));
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

    private void Autologin(String username, String password) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        User user = new User();
        user.setAccount(username);
        user.setPassword(password);
        JSONObject json = (JSONObject) JSONObject.toJSON(user);

        OkHttp3Util.POST(JUrl.login, this, json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    finish();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                    Result1 result = JsonUtils.getResult1(jsonObject);
                    if (result.getCode() == 10000) {
                        //success
                        if (result.getData() != null) {
                            String jwt = result.getData().getString("jwt");
                            MMkvUtil.getInstance(HomeActivity.this, "jwts").encode("jwt", jwt);
                            MMkvUtil.getInstance(HomeActivity.this, "jwts").encode("account", username);
                            MMkvUtil.getInstance(HomeActivity.this, "jwts").encode("password", password);
                            UserUtils.getInstance().setUser(GsonUtil.gsonToBean(result.getData().getString("user"), ResponseUser.class));
                            EventBus.getDefault().postSticky(UserUtils.getInstance().getUser());
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            jCenterDialog.dismiss();
                            EventBus.getDefault().post(new WebSocketEvent(WebSocketEventType.OPEN));
                        }, 3000);

                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            jCenterDialog.dismiss();
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            finish();
                        });
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                    });
                }
            }
        });
    }


}