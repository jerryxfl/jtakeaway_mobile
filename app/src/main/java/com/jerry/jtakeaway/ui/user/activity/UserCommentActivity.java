package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.Comment;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserCommentActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.avatar)
    CircleImageView avatar;

    @BindView(R.id.userNickName)
    TextView userNickName;

    @BindView(R.id.time)
    TextView time;


    @BindView(R.id.content)
    TextView content;


    @BindView(R.id.menuImg)
    ImageView menuImg;

    @BindView(R.id.shopName)
    TextView shopName;

    @BindView(R.id.shopDescr)
    TextView shopDescr;


    @BindView(R.id.container)
    LinearLayout container;

    private Comment comment;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_user_comment;
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
        comment = (Comment) intent.getSerializableExtra("COMMENT");
        if (comment != null) {
            setPageDatas();
        }
    }

    private void setPageDatas() {
        Glide.with(this)
                .load(comment.getUser().getUseradvatar())
                .into(avatar);
        userNickName.setText(comment.getUser().getUsernickname());

        Date date = comment.getCreatetime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        time.setText(simpleDateFormat.format(date));
        content.setText(comment.getContent());
        getShopMenu(comment.getSuser());
    }

    private void getShopMenu(Suser suser) {
        if (jCenterDialog == null) jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.hot_shop_one_menu(suser.getId()), this, new Callback() {
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
                if (result.getCode() == 10000) {
                    Menus menu = GsonUtil.gsonToBean(result.getData().toString(),Menus.class);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        setMenus(suser,menu);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(UserCommentActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void setMenus(Suser suser, Menus menu) {
        Glide.with(this)
                .load(menu.getFoodimg())
                .into(menuImg);
        shopName.setText(suser.getShopname());
        shopDescr.setText(suser.getDscr());
    }


    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        container.setOnClickListener(v -> {
            Intent intent = new Intent(UserCommentActivity.this, ShopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("shop", comment.getSuser());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public void destroy() {

    }
}