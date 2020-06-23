package com.jerry.jtakeaway.ui.user.activity;

import android.os.Handler;
import android.os.Looper;
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

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewEmailActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.newEmail)
    EditText newEmail;

    @BindView(R.id.code)
    EditText code;

    @BindView(R.id.timeToReGetCode)
    TextView timeToReGetCode;

    @BindView(R.id.ok)
    Button ok;

    private boolean canSendCode = true;
    private JCenterDialog jCenterDialog;
    private String setEmail;
    @Override
    public int getLayout() {
        return R.layout.activity_new_email;
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
        timeToReGetCode.setOnClickListener(v -> {
            String newEmailText = newEmail.getText().toString().trim();
            if(checkEmail(newEmailText)){
                sendEmail("",newEmailText,3);
            }else{
                newEmail.setError("请输入正确邮箱地址");
            }
        });
        ok.setOnClickListener(v ->{
            String  s_code = code.getText().toString().trim();
            if(s_code.equals("")){
                code.setError("请输入验证码");
                return;
            }
            if(setEmail.equals("")){
                newEmail.setError("请输入新邮箱地址");
                return;
            }
            Verification(s_code,setEmail,4);
        });
    }
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
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
                        EventBus.getDefault().post("userChange");
                        finish();
                    });
                }else if(result.getCode()==22){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        NewEmailActivity.this.code.setError("邮箱无效");
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(NewEmailActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    private void sendEmail(String code,String newEmailText,int tag) {
        OkHttp3Util.GET(JUrl.change_email(code,newEmailText,tag), this, new Callback() {
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
                        setEmail = newEmailText;
                        newEmail.setEnabled(false);
                        t.start();
                    });
                }else if(result.getCode() ==1){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(NewEmailActivity.this, "无法更换邮箱绑定", Toast.LENGTH_SHORT).show();
                    });
                }else if(result.getCode()==22){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        NewEmailActivity.this.code.setError("邮箱无效");
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(NewEmailActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
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
                    if(timeToReGetCode!=null)timeToReGetCode.setText(finalI +"s 后重新发送");
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            canSendCode = true;
            new Handler(Looper.getMainLooper()).post(() -> {
                if(timeToReGetCode!=null)timeToReGetCode.setText("重新发送");
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