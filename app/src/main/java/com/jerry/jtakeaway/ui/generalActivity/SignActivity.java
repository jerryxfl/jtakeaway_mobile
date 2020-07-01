package com.jerry.jtakeaway.ui.generalActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.jerry.jtakeaway.bean.model.form;
import com.jerry.jtakeaway.bean.requestBean.Sign;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JLoginButton;
import com.jerry.jtakeaway.utils.BitmapBlurHelper;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
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


    private List<form> formList = new ArrayList<>();
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
        Bitmap bitmap = BitmapBlurHelper.doBlur(this, BitmapFactory.decodeResource(getResources(), R.drawable.startimg), 20);
        Drawable drawable = new BitmapDrawable(bitmap);
        container.setBackground(drawable);

        login_card_wapper.getBackground().setAlpha(100);
        shopSign2 = findViewById(R.id.shopSign);
        hourseSign2 = findViewById(R.id.hourseSign);

        shopSign = getLayoutInflater().inflate(R.layout.shop_sign_layout,null);
        hourseSign = getLayoutInflater().inflate(R.layout.hourse_sign_layout,null);

        IdCard = hourseSign.findViewById(R.id.IdCard);
        phoneNumber = hourseSign.findViewById(R.id.phoneNumber);
        address = shopSign.findViewById(R.id.address);
        shopName = shopSign.findViewById(R.id.shopName);

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
            signBean.setUserNickName(userNickNameText);
            signBean.setPassword(passWordText);
            signBean.setIdcard(IdCardText);
            signBean.setPhoneNumber(phoneNumberText);
            signBean.setAddress(addressText);
            signBean.setShopName(shopNameText);
            Sign(signBean);
        });

        userNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userNickNameText = userNickName.getText().toString().trim();
            }
        });
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passWordText = passWord.getText().toString().trim();
            }
        });
        IdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                IdCardText = IdCard.getText().toString().trim();
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumberText = phoneNumber.getText().toString().trim();
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressText = address.getText().toString().trim();
            }
        });
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shopNameText = shopName.getText().toString().trim();
            }
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

                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            sign_btn.reset();
                            jCenterDialog.dismiss();
                        });
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