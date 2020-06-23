package com.jerry.jtakeaway.ui.user.activity;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.oldPwd)
    EditText oldPwd;
    @BindView(R.id.oldPwdEye)
    ImageView oldPwdEye;

    @BindView(R.id.newPwd)
    EditText newPwd;
    @BindView(R.id.newPwdEye)
    ImageView newPwdEye;

    @BindView(R.id.ok)
    Button ok;

    private boolean oldPwdIsShowing = false;
    private boolean newPwdIsShowing = false;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_change_pwd;
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
        return_aib.setOnClickListener(v -> finish());
        oldPwdEye.setOnClickListener(v ->{
            oldPwdIsShowing = !oldPwdIsShowing;
            if(oldPwdIsShowing){
                oldPwd.setInputType(128);
                oldPwdEye.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.open_eyes));
            }else{
                oldPwd.setInputType(129);
                oldPwdEye.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.close_eyes));
            }
            oldPwd.setSelection(oldPwd.getText().length());
        });
        newPwdEye.setOnClickListener(v -> {
            newPwdIsShowing = !newPwdIsShowing;
            if(newPwdIsShowing){
                newPwd.setInputType(128);
                newPwdEye.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.open_eyes));
            }else{
                newPwd.setInputType(129);
                newPwdEye.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.close_eyes));
            }
            newPwd.setSelection(newPwd.getText().length());
        });
        ok.setOnClickListener(v ->{
            String oldPwdText = oldPwd.getText().toString().trim();
            String newPwdText = newPwd.getText().toString().trim();

            if(oldPwdText.equals("")){
                oldPwd.setError("请先输入旧密码");
                return;
            }
            if(newPwdText.equals("")){
                newPwd.setError("请填写新密码");
                return;
            }
            changePwd(oldPwdText, newPwdText);
        });
    }

    private void changePwd(String oldPwd, String newPwd){
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.change_password(oldPwd,newPwd), this, new Callback() {
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
                }else if(result.getCode()==4){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        ChangePwdActivity.this.oldPwd.setError("密码错误");
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ChangePwdActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void destroy() {

    }
}