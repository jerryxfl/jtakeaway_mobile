package com.jerry.jtakeaway.ui.generalActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSONObject;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.google.gson.Gson;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.responseBean.Result;
import com.jerry.jtakeaway.utils.BitmapBlurHelper;
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


public class LoginActivity extends BaseActivity {
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.login_card)
    CardView login_card;

    private ScaleAnimation scaleAnimation;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void InitView() {
        //点击键盘外任何地方键盘消失
        KeyboardDismisser.useWith(this);
        Bitmap bitmap = BitmapBlurHelper.doBlur(this,BitmapFactory.decodeResource(getResources(), R.drawable.startimg), 20);
        Drawable drawable = new BitmapDrawable(bitmap);
        container.setBackground(drawable);

        login_card.getBackground().setAlpha(152);
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
//        loginbtn.setOnClickListener(() -> {
//            String username = username_et.getText().toString().trim();
//            String password = password_et.getText().toString().trim();
//            if ("".equals(username)) {
//                error_tv.setText("账号不能为空");
//                tipAnimation(error_wrapper);
//                loginbtn.LoginFailed();
//                return;
//            }
//            if ("".equals(password)) {
//                error_tv.setText("密码不能为空");
//                tipAnimation(error_wrapper);
//                loginbtn.LoginFailed();
//                return;
//            }
//            if (!userdeal_cb.isChecked()) {
//                error_tv.setText("请先同意用户协议");
//                tipAnimation(error_wrapper);
//                loginbtn.LoginFailed();
//                return;
//            }
//            login(username, password);
//        });
//
//        //忘记密码
//        forgetpwd_tv.setOnClickListener(v -> {
////            startActivity(FlutterActivity
////                    .withNewEngine()
////                    .initialRoute("/forgetPassword")
////                    .build(this));
//        });
//
//        //注册
//        sign_tv.setOnClickListener(v -> {
//
//        });
    }

    @Override
    public void destroy() {

    }


//    private void tipAnimation(View view) {
//        System.out.println("动画开始");
//        //参数7～8：x轴的结束位置
//        TranslateAnimation translateAni = new TranslateAnimation(0, 0, PixAndDpUtil.dip2px(this, 50), PixAndDpUtil.dip2px(this, -50));
//
//        //设置动画执行的时间，单位是毫秒
//        translateAni.setDuration(1000);
//        translateAni.setInterpolator(new AccelerateDecelerateInterpolator());
//        translateAni.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                handler.removeMessages(1);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                handler.sendEmptyMessageDelayed(1, 3000);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        // 设置动画模式（Animation.REVERSE设置循环反转播放动画,Animation.RESTART每次都从头开始）
//        translateAni.setRepeatMode(Animation.REVERSE);
//        translateAni.setFillAfter(true);
//        // 启动动画
//        view.startAnimation(translateAni);
//    }

    private void reset(View view) {
        System.out.println("结束动画开始");
        //参数7～8：x轴的结束位置
        TranslateAnimation translateAni = new TranslateAnimation(0, 0, PixAndDpUtil.dip2px(this, -50), PixAndDpUtil.dip2px(this, 50));

        //设置动画执行的时间，单位是毫秒
        translateAni.setDuration(1000);
        translateAni.setInterpolator(new AccelerateDecelerateInterpolator());
        // 设置动画模式（Animation.REVERSE设置循环反转播放动画,Animation.RESTART每次都从头开始）
        translateAni.setFillAfter(true);
        // 启动动画
        view.startAnimation(translateAni);
    }

    private boolean login(String username, String password) {
        User user = new User();
        user.setAccount(username);
        user.setPassword(password);
        JSONObject json = (JSONObject) JSONObject.toJSON(user);
        System.out.println(json.toString());

        OkHttp3Util.POST(JUrl.login, this, json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
//                    loginbtn.LoginFailed();
//                    error_tv.setText("服务器链接失败");
//                    tipAnimation(error_wrapper);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                    Result result = JsonUtils.getResult(jsonObject);
//                        System.out.println("返回值"+result.getCode()+":"+result.getMsg()+":"+result.getData());
                    System.out.println("返回值" + result.toString());
                    if (result.getCode() == 10000) {
                        //success
                        if (result.getData() != null) {
                            String jwt = result.getData().getString("jwt");
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("jwt", jwt);
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("account", username);
                            MMkvUtil.getInstance(LoginActivity.this, "jwts").encode("password", password);
                            Gson gson = new Gson();
                            UserUtils.getInstance().setUser(gson.fromJson(json.getString("user"), User.class));
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            goToNew();
                        },1500);

                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
//                            loginbtn.LoginFailed();
//                            error_tv.setText(result.getMsg());
//                            tipAnimation(error_wrapper);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }


    private void goToNew(){
//        loginbtn.goToNew();
//        loginbtn.bringToFront();
//        Intent intent = new Intent(this, HomeActivity.class);
//        scaleAnimation = new ScaleAnimation(1,200,1,200,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setDuration(300);
//        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                handler.postDelayed(() -> {
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
//                    finish();
//                },200);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        scaleAnimation.setFillAfter(true);
//        scaleAnimation.setRepeatCount(0);
//        loginbtn.startAnimation(scaleAnimation);
//        Glide.with(this)
//                .load(R.drawable.login_success_shape)
//                .into(loginbackground_iv);

    }

}
