package com.jerry.jtakeaway.ui.user.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class OrderTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> OrderTabFragments;
    private List<String>OrderTabTitle;
    public OrderTabAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> OrderTabFragments, List<String>OrderTabTitle) {
        super(fm, behavior);
        this.OrderTabFragments=OrderTabFragments;
        this.OrderTabTitle=OrderTabTitle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return OrderTabFragments.get(position);
    }

    @Override
    public int getCount() {
        return OrderTabFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return OrderTabTitle.get(position);
    }
}
