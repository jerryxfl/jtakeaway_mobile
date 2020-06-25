package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeNewNickNameActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.SaveBtn)
    Button save_btn;
    @BindView(R.id.NickName)
    EditText NickName;


    private String nickname;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_change_new_nick_name;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        nickname = intent.getStringExtra("NICKNAME");

        NickName.setText(nickname);
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        NickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(NickName.getText().toString().trim().equals("")){
                    save_btn.setEnabled(false);
                    save_btn.setTextColor(Color.parseColor("#919191"));
                }else{
                    save_btn.setEnabled(true);
                    save_btn.setTextColor(Color.WHITE);
                }
            }
        });

        save_btn.setOnClickListener(v -> {
            String nicknameText = NickName.getText().toString().trim();
            if(nicknameText.equals("")){
                NickName.setText("昵称不能设置为空");
            }else{
                changeNickname(nicknameText);
            }
        });
    }

    private void changeNickname(String nickname){
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.change_nickname(nickname), this, new Callback() {
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
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        EventBus.getDefault().post("userChange");
                        finish();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ChangeNewNickNameActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    @Override
    public void destroy() {

    }
}