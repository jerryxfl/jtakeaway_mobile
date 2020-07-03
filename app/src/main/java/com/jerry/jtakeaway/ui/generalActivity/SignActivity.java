package com.jerry.jtakeaway.ui.generalActivity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.requestBean.Sign;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.bean.responseBean.SignResult;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JLoginButton;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignActivity extends BaseActivity {
    @BindView(R.id.container)
    RelativeLayout container;

    @BindView(R.id.login_card_wapper)
    LinearLayout login_card_wapper;

    @BindView(R.id.userTypeSpinner)
    Spinner userTypeSpinner;

    @BindView(R.id.sign_btn)
    JLoginButton sign_btn;

    @BindView(R.id.userNickName)
    EditText userNickName;

    @BindView(R.id.passWord)
    EditText passWord;


    private int userType = 0;
    private JCenterDialog jCenterDialog;
    private View shopSign;
    private View hourseSign;
    private View shopSign2;
    private View hourseSign2;
    private EditText IdCard;
    private EditText phoneNumber;
    private EditText address;
    private EditText shopName;

    private String userNickNameText = "";
    private String passWordText= "";
    private String IdCardText= "";
    private String phoneNumberText= "";
    private String addressText= "";
    private String shopNameText= "";

    @Override
    public int getLayout() {
        return R.layout.activity_sign;
    }

    @Override
    public void InitView() {
        KeyboardDismisser.useWith(this);

        login_card_wapper.getBackground().setAlpha(100);
        shopSign2 = findViewById(R.id.shopSign);
        hourseSign2 = findViewById(R.id.hourseSign);

        shopSign = getLayoutInflater().inflate(R.layout.shop_sign_layout,null);
        hourseSign = getLayoutInflater().inflate(R.layout.hourse_sign_layout,null);

        IdCard = hourseSign2.findViewById(R.id.IdCard);
        phoneNumber = hourseSign2.findViewById(R.id.phoneNumber);
        address = shopSign2.findViewById(R.id.address);
        shopName = shopSign2.findViewById(R.id.shopName);

        initSpinner();
    }

    private void initSpinner() {
        String[] items = getResources().getStringArray(R.array.UserType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                userType = pos;
                switch (userType) {
                    case 0:
                        hourseSign2.setVisibility(View.GONE);
                        shopSign2.setVisibility(View.GONE);
                        break;
                    case 1:
                        hourseSign2.setVisibility(View.VISIBLE);
                        shopSign2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        hourseSign2.setVisibility(View.VISIBLE);
                        shopSign2.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        sign_btn.setOnClickListener(v -> {
            System.out.println("注册按钮:"+userType);
            userNickNameText =userNickName.getText().toString().trim();
            passWordText =passWord.getText().toString().trim();
            IdCardText =IdCard.getText().toString().trim();
            phoneNumberText =phoneNumber.getText().toString().trim();
            addressText =address.getText().toString().trim();
            shopNameText =shopName.getText().toString().trim();

            if(userNickNameText.equals("")){
                userNickName.setError("请填写昵称");
                return;
            }
            if(passWordText.equals("")){
                passWord.setError("请填写密码");
                return;
            }
            if(userType==1||userType==2){
                if(IdCardText.equals("")){
                    IdCard.setError("请填写身份证");
                    return;
                }
                if(phoneNumberText.equals("")){
                    phoneNumber.setError("请填写手机号码");
                    return;
                }
            }
            if(userType == 1){
                if(addressText.equals("")){
                    address.setError("请填写地址");
                    return;
                }
                if(shopNameText.equals("")){
                    shopName.setError("请填写商店名称");
                    return;
                }
            }
            Sign signBean = new Sign();
            signBean.setType(userType);
            signBean.setUserNickName(userNickNameText);
            signBean.setPassword(passWordText);
            signBean.setIdcard(IdCardText);
            signBean.setPhoneNumber(phoneNumberText);
            signBean.setAddress(addressText);
            signBean.setShopName(shopNameText);
            Sign(signBean);
        });
    }


    private void Sign(Sign sign) {
        if (jCenterDialog == null) jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        JSONObject json = (JSONObject) JSONObject.toJSON(sign);
        OkHttp3Util.POST(JUrl.Sign, this, json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    sign_btn.reset();
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                    Result1 result = JsonUtils.getResult1(jsonObject);
                    System.out.println(result.getData().toString());
                    if (result.getCode() == 10000) {
                        //success
                        if (result.getData() != null) {
                            SignResult signResult = GsonUtil.gsonToBean(result.getData().toString(),SignResult.class);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                sign_btn.reset();
                                jCenterDialog.dismiss();
                                new SweetAlertDialog(SignActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("注册成功")
                                        .setContentText("你的账号为:"+signResult.getAccount())
                                        .setConfirmText("确认")
                                        .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                                        .show();
                            });
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(SignActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            sign_btn.reset();
                            jCenterDialog.dismiss();
                        });
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        sign_btn.reset();
                        jCenterDialog.dismiss();
                    });
                }
            }
        });
    }


    @Override
    public void destroy() {

    }
}