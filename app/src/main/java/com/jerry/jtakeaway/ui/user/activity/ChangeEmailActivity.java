package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.jerry.jtakeaway.utils.UserUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeEmailActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    @BindView(R.id.targetEmail)
    TextView targetEmail;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.timeToReGetCode)
    TextView timeToReGet;
    @BindView(R.id.ok)
    Button ok;

    private boolean canSendCode = true;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_change_email;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        setPageDatas();

    }

    private void setPageDatas() {
        String str="请输入<font color='#FF0000'>"+UserUtils.getInstance().getUser().getEmail().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4")+"</font>收到的验证码";
        targetEmail.setText(Html.fromHtml(str));
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        timeToReGet.setOnClickListener(v -> {
            if(canSendCode)sendEmail("","",1);
        });
        ok.setOnClickListener(v ->{
            String  s_code = code.getText().toString().trim();
            if(s_code.equals("")){
                code.setError("请输入验证码");
                return;
            }
            Verification(s_code,"",2);
        });
    }

    private void sendEmail(String code,String newEmail,int tag) {
        OkHttp3Util.GET(JUrl.change_email(code,newEmail,tag), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if(result.getCode() == 10000){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        canSendCode = false;
                        if(!t.isAlive())t.start();
                    });
                }else if(result.getCode()==22){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ChangeEmailActivity.this.code.setError("邮箱无效");
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ChangeEmailActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void Verification(String code,String newEmail,int tag){
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        OkHttp3Util.GET(JUrl.change_email(code,newEmail,tag), this, new Callback() {
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
                        startActivity( new Intent(ChangeEmailActivity.this, NewEmailActivity.class));
                        finish();
                    });
                }else if(result.getCode()==22){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        ChangeEmailActivity.this.code.setError("邮箱无效");
                    });
                }else if(result.getCode()==18){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ChangeEmailActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(ChangeEmailActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 60; i >0 ; i--) {
                int finalI = i;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if(timeToReGet!=null)timeToReGet.setText(finalI +"s 后重新发送");
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            canSendCode = true;
            new Handler(Looper.getMainLooper()).post(() -> {
                if(timeToReGet!=null)timeToReGet.setText("重新发送");
            });
        }
    });

    @Override
    public void destroy() {
        if(t.isAlive()){
            t.interrupt();
            t = null;
        }
    }
}