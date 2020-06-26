package com.jerry.jtakeaway.ui.generalActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.responseBean.ResponseUser;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JLoginButton;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEvent;
import com.jerry.jtakeaway.eventBusEvents.WebSocketEventType;
import com.jerry.jtakeaway.ui.user.activity.HomeActivity;
import com.jerry.jtakeaway.utils.BitmapBlurHelper;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.KeyboardUtils;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;

import net.steamcrafted.loadtoast.LoadToast;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@SuppressWarnings("all")
public class LoginActivity extends BaseActivity {
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.username_et)
    EditText username_et;
    @BindView(R.id.password_et)
    EditText password_et;
    @BindView(R.id.login_btn)
    JLoginButton login_btn;
    @BindView(R.id.qq_aib)
    AniImgButton qq_aib;
    @BindView(R.id.weibo_aib)
    AniImgButton weibo_aib;
    @BindView(R.id.wechat_aib)
    AniImgButton wechat_aib;
    @BindView(R.id.forgetpwd_tv)
    TextView forgetpwd_tv;
    @BindView(R.id.just_login_tv)
    TextView justlogin_tv;
    @BindView(R.id.login_card_wapper)
    LinearLayout login_card_wapper;

    private ScaleAnimation scaleAnimation;
    private LoadToast loadtoast;


    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void InitView() {
        loadtoast = new LoadToast(this);
        //点击键盘外任何地方键盘消失
        KeyboardDismisser.useWith(this);
        Bitmap bitmap = BitmapBlurHelper.doBlur(this, BitmapFactory.decodeResource(getResources(), R.drawable.startimg), 20);
        Drawable drawable = new BitmapDrawable(bitmap);
        container.setBackground(drawable);

        login_card_wapper.getBackground().setAlpha(100);


        username_et.setText(MMkvUtil.getInstance(LoginActivity.this, "jwts").decodeString("account") == null ? "" : MMkvUtil.getInstance(LoginActivity.this, "jwts").decodeString("account"));
        password_et.setText(MMkvUtil.getInstance(LoginActivity.this, "jwts").decodeString("password") == null ? "" : MMkvUtil.getInstance(LoginActivity.this, "jwts").decodeString("password"));
    }

    @Override
    public void InitData() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            loadtoast.hide();
        }
    };

    /*
     * 1成功
     * 2失败
     */
    private void setToast(String text, int type) {
        System.out.println(text);
        handler.removeMessages(0);
        loadtoast.setText(text);
        loadtoast.setTranslationY(PixAndDpUtil.dip2px(this, PixAndDpUtil.getStatusBarHeight(this)));
        switch (type) {
            case 1:
                loadtoast.success();
                loadtoast.setTextColor(Color.WHITE).setBackgroundColor(Color.GREEN).setProgressColor(Color.WHITE);
                loadtoast.show();
                handler.sendEmptyMessageDelayed(0, 2000);
                break;
            case 2:
                loadtoast.error();
                loadtoast.setTextColor(Color.WHITE).setBackgroundColor(Color.RED).setProgressColor(Color.WHITE);
                loadtoast.show();
                handler.sendEmptyMessageDelayed(0, 3000);
                break;
        }

    }

    @Override
    public void InitListener() {
        login_btn.setOnClickListener(new JLoginButton.OnJClickListener() {
            @Override
            public void onClick() {
                KeyboardUtils.hideKeyboard(LoginActivity.this,login_btn);
                String username = username_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if ("".equals(username)) {
                    setToast("账号不能为空", 2);
                    login_btn.reset();
                    return;
                }
                if ("".equals(password)) {
                    setToast("密码不能为空", 2);
                    login_btn.reset();
                    return;
                }
                login(username, password);
            }
        });

        //忘记密码
        forgetpwd_tv.setOnClickListener(v -> {
//            startActivity(FlutterActivity
//                    .withNewEngine()
//                    .initialRoute("/forgetPassword")
//                    .build(this));
        });

        //直接登录
        justlogin_tv.setOnClickListener(v -> {

        });
    }

    @Override
    public void destroy() {

    }

    private boolean login(String username, String password) {
        User user = new User();
        user.setAccount(username);
        user.setPassword(password);
        JSONObject json = (JSONObject) JSONObject.toJSON(user);

        OkHttp3Util.POST(JUrl.login, this, json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    setToast("服务器链接失败", 2);
                    login_btn.reset();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                    Result1 result = JsonUtils.getResult1(jsonObject);
                    if (result.getCode() == 10000) {
                        //success
                        if (result.getData() != null) {
                            String jwt = result.getData().getString("jwt");
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("jwt", jwt);
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("account", username);
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("password", password);
                            UserUtils.getInstance().setUser(GsonUtil.gsonToBean(result.getData().getString("user"), ResponseUser.class));
                            EventBus.getDefault().postSticky(UserUtils.getInstance().getUser());
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            login_btn.reset();
                            EventBus.getDefault().post(new WebSocketEvent(WebSocketEventType.OPEN));
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                            finish();
                        }, 3000);

                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            setToast(result.getMsg(), 2);
                            login_btn.reset();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }
}
