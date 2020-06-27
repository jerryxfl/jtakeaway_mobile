package com.jerry.jtakeaway.ui.user.activity;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.content)
    TextView content;


    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        String html = "<h1>疯狂外卖</h1><br/><p>订餐服务app,提供给你最周到的服务</p>";
        content.setText(Html.fromHtml(html));

    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
    }

    @Override
    public void destroy() {

    }
}