package com.jerry.jtakeaway.ui.user.fragment;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.ResponseOrder;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.eventBusEvents.PagePositionEvent;
import com.jerry.jtakeaway.ui.user.adapter.OrderTabAdapter;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.AllFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.CompletedFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.EvaluateFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.RefundFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.UnderwayFragment;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;

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

public class OrderFragment extends BaseFragment {

    @BindView(R.id.order_tab)
    TabLayout order_tab;
    @BindView(R.id.order_tab_view)
    ViewPager order_tab_view;
    private List<Fragment> orderTabFragments;
    private List<ResponseOrder> responseOrderList = new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.fragment_order;
    }

    @Override
    public void InitView() {
        SignEventBus();
        orderTabFragments = new ArrayList<Fragment>();
        orderTabFragments.add(new AllFragment());
        orderTabFragments.add(new UnderwayFragment());
        orderTabFragments.add(new CompletedFragment());
        orderTabFragments.add(new EvaluateFragment());
        orderTabFragments.add(new RefundFragment());
        List<String> OrderTabTitle = new ArrayList<String>();
        OrderTabTitle.add("全部");
        OrderTabTitle.add("进行中");
        OrderTabTitle.add("已完成");
        OrderTabTitle.add("待评价(1)");
        OrderTabTitle.add("退款/售后");
        for (int i = 0; i < OrderTabTitle.size(); i++) {
            order_tab.addTab(order_tab.newTab().setText(OrderTabTitle.get(i)));
        }
        OrderTabAdapter orderTabAdapter = new OrderTabAdapter(activity.getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, orderTabFragments, OrderTabTitle);
        order_tab_view.setAdapter(orderTabAdapter);
        order_tab_view.setOffscreenPageLimit(5);
        order_tab.setupWithViewPager(order_tab_view);
        for (int j = 0; j < order_tab.getTabCount(); j++) {
            if (j == 0) {
                TabLayout.Tab tab = order_tab.getTabAt(j);
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }
        }

    }

    @Override
    public void InitData() {
        getOrders();
    }

    private void getOrders() {
        OkHttp3Util.GET(JUrl.orders(responseOrderList.size()), context, new Callback() {
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
                    System.out.println("所有订单" + result.getData().toString());
                    responseOrderList.addAll(GsonUtil.parserJsonToArrayBeans(result.getData().toString(), ResponseOrder.class));
                    EventBus.getDefault().postSticky(responseOrderList);
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public void InitListener() {
        order_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void destroy() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPagePosition(PagePositionEvent pagePosition) {
        if (pagePosition.getPosition_TabView() < orderTabFragments.size()) {
            order_tab_view.setCurrentItem(pagePosition.getPosition_TabView());
        }
    }


}