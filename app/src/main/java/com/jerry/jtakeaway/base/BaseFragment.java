package com.jerry.jtakeaway.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public abstract int getLayout();

    public abstract void InitView();

    public abstract void InitData();

    public abstract void InitListener();

    public abstract void destroy();

    public FragmentActivity activity;
    public Context context;
    private Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container,false);
        unbinder = ButterKnife.bind(this,view);
        activity = getActivity();
        context = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        KeyboardDismisser.useWith(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitView();
        InitData();
        InitListener();
    }

    public void SignEventBus() {//注册eventBus
        EventBus.getDefault().register(this);
    }



    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {//注销事件分发器
            EventBus.getDefault().unregister(this);
        }
        if (unbinder != null && unbinder != Unbinder.EMPTY) {//取消注解绑定
            unbinder.unbind();
            unbinder = null;
        }

        destroy();
        super.onDestroyView();
    }
}
