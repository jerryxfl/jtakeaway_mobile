package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.ui.generalActivity.LoginActivity;
import com.jerry.jtakeaway.utils.InitApp;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.selfInfo)
    RelativeLayout selfInfo;//个人信息

    @BindView(R.id.accountSafe)
    RelativeLayout accountSafe;//账号与安全

    @BindView(R.id.SoundEffectsAndNotifications)
    RelativeLayout SoundEffectsAndNotifications;//音效与通知

    @BindView(R.id.privacy)
    RelativeLayout privacy;//隐私

    @BindView(R.id.bugReport)
    RelativeLayout bugReport;//问题反馈

    @BindView(R.id.aboutUs)
    RelativeLayout aboutUs;//关于疯狂外卖

    @BindView(R.id.logOut)
    Button logout;//登出
    private JCenterDialog jCenterDialog;


    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        logout.setOnClickListener(v -> {
            logout();
        });
        selfInfo.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this,SelfInfoActivity.class)));
        accountSafe.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this,SecurityCenterActivity.class)));
        return_aib.setOnClickListener(v -> finish());
    }

    private void logout() {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.jwtLogout, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if(result.getCode() == 10000){
                    System.out.println("退出登录"+result.toString());
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        UserUtils.getInstance().setUser(null);
                        MMkvUtil.getInstance(SettingActivity.this, "jwts").encode("jwt","");
                        startActivity( new Intent(SettingActivity.this, LoginActivity.class));
                        InitApp.getInstance().finishAll();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void destroy() {
        if(jCenterDialog != null){
            if(jCenterDialog.isShowing())jCenterDialog.dismiss();
        }
    }
}