package com.jerry.jtakeaway.ui.user.fragment;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.ui.user.adapter.OrderTabAdapter;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.AllFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.CompletedFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.EvaluateFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.RefundFragment;
import com.jerry.jtakeaway.ui.user.fragment.ordertabfragment.UnderwayFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderFragment extends BaseFragment {

    @BindView(R.id.order_tab)
    TabLayout order_tab;
    @BindView(R.id.order_tab_view)
    ViewPager order_tab_view;
    @Override
    public int getLayout() {
        return R.layout.fragment_order;
    }

    @Override
    public void InitView() {
        List<Fragment>OrderTabFragments=new ArrayList<Fragment>();
        OrderTabFragments.add(new AllFragment());
        OrderTabFragments.add(new UnderwayFragment());
        OrderTabFragments.add(new CompletedFragment());
        OrderTabFragments.add(new EvaluateFragment());
        OrderTabFragments.add(new RefundFragment());
        List<String>OrderTabTitle=new ArrayList<String>();
        OrderTabTitle.add("全部");
        OrderTabTitle.add("进行中");
        OrderTabTitle.add("已完成");
        OrderTabTitle.add("待评价(1)");
        OrderTabTitle.add("退款/售后");
        for(int i=0;i<OrderTabTitle.size();i++){
            order_tab.addTab(order_tab.newTab().setText(OrderTabTitle.get(i)));
        }
        OrderTabAdapter orderTabAdapter=new OrderTabAdapter(activity.getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,OrderTabFragments,OrderTabTitle);
        order_tab_view.setAdapter(orderTabAdapter);
        order_tab.setupWithViewPager(order_tab_view);
        for(int j=0;j<order_tab.getTabCount();j++){
            if (j == 0) {
                TabLayout.Tab tab=order_tab.getTabAt(j);
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }
        }
        initClick();

    }

    private void initClick() {
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
    public void InitData() {

    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }
}