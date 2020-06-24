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

public class AboutAccountActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    @BindView(R.id.content)
    TextView content;

    @Override
    public int getLayout() {
        return R.layout.activity_about_account;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        setContent();
    }

    private void setContent() {
        String html = "<h1 style='color:#222222'>账号疑问?</h1><br/>" +
                "<p>账号无法登录?</p><br/>" +
                "<p>      检查密码是否输入正确,或者是否在其他设备已登录</p><br/>" +
                "<p>无法进行操作?</p><br/>" +
                "<p>      账户权限不足</p><br/>";
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