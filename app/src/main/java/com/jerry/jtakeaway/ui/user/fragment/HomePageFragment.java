package com.jerry.jtakeaway.ui.user.fragment;

import android.Manifest;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.interfaces.RefreshListener;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.GPSUtils;

import butterknife.BindView;

@SuppressWarnings("all")
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.goRefreshLayout)
    GoRefreshLayout goRefreshLayout;
    @BindView(R.id.location_tv)
    TextView location_tv;
    @BindView(R.id.serachview)
    SearchView serachview;
    @BindView(R.id.location_ain)
    AniImgButton location_ain;


    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void InitView() {
        KeyboardDismisser.useWith(this);
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            GPSUtils.getInstance(context).
        }else{
            Toast.makeText(context,"没有定位权限",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
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

    }
}