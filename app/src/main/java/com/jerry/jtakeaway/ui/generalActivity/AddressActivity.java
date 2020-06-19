package com.jerry.jtakeaway.ui.generalActivity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.smarttop.library.widget.AddressSelector;

import butterknife.BindView;

public class AddressActivity extends BaseActivity {
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.top)
    View top;

    private AddressSelector selector;

    @Override
    public int getLayout() {
        return R.layout.activity_address;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        selector = new AddressSelector(this);

        View view = selector.getView();
        container.addView(view);

    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        selector.setOnAddressSelectedListener((province, city, county, street) -> {
            Intent intent = new Intent();
            intent.putExtra("address",(province==null?"":province.name)+" "+(city==null?"":city.name)+" "+(county==null?"":county.name)+" "+(street==null?"":street.name));
            setResult(1,intent);
            finish();
        });

    }

    @Override
    public void destroy() {

    }
}