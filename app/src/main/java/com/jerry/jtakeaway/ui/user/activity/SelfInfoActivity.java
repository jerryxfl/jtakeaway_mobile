package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.ui.generalActivity.AddressManagerActivity;
import com.jerry.jtakeaway.ui.generalActivity.EditAddressActivity;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelfInfoActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.headImgWrapper)
    RelativeLayout headImgWrapper;

    @BindView(R.id.headImg)
    CircleImageView headImg;

    @BindView(R.id.userNickNameWrapper)
    RelativeLayout userNickNameWrapper;

    @BindView(R.id.userNickName)
    TextView userNickName;

    @BindView(R.id.addressWrapper)
    RelativeLayout addressWrapper;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_self_info;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        SignEventBus();
    }

    @Override
    public void InitData() {
        setPageDatas();
    }

    private void setPageDatas() {
        Glide.with(this)
                .load(UserUtils.getInstance().getUser().getUseradvatar())
                .into(headImg);
        userNickName.setText(UserUtils.getInstance().getUser().getUsernickname());
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        addressWrapper.setOnClickListener(v ->{
            getAddress();
        });
    }

    private void getAddress() {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.address, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    List<Address> address = new ArrayList<Address>(GsonUtil.jsonToList(result.getData().toString(), Address.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        if (address.isEmpty()) {
                            startActivity(new Intent(SelfInfoActivity.this, EditAddressActivity.class));
                        } else {
                            Intent intent = new Intent(SelfInfoActivity.this, AddressManagerActivity.class);
                            intent.putExtra("addres", (Serializable) address);
                            startActivity(intent);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(SelfInfoActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void destroy() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChange(String tag){
        if(tag.equals("userChangeSuccess")){
            setPageDatas();
        }
    }
}