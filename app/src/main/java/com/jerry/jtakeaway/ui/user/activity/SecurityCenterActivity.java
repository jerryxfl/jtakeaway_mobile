package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class SecurityCenterActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    @BindView(R.id.pwdChange)
    RelativeLayout pwdChange;
    @BindView(R.id.emailChange)
    RelativeLayout emailChange;
    @BindView(R.id.phoneChange)
    RelativeLayout phoneChange;
    @BindView(R.id.loginRecord)
    RelativeLayout loginRecord;
    @BindView(R.id.accountQuestion)
    RelativeLayout accountQuestion;
    @BindView(R.id.accountCancel)
    RelativeLayout accountCancel;
    @BindView(R.id.pwdLevel)
    TextView pwdLevel;
    @BindView(R.id.phone_tv)
    TextView phone_tv;
    @BindView(R.id.accountLevel)
    TextView accountLevel;

    @Override
    public int getLayout() {
        return R.layout.activity_security_center;
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
        String pwdType = getPwdLevel(UserUtils.getInstance().getUser().getPassword());
        if(pwdType.equals("安全")){
            pwdLevel.setTextColor(Color.parseColor("#52c41a"));
            accountLevel.setTextColor(Color.parseColor("#52c41a"));
        }else if(pwdType.equals("警告")){
            pwdLevel.setTextColor(Color.parseColor("#fff566"));
            accountLevel.setTextColor(Color.parseColor("#fff566"));
        }else if(pwdType.equals("高风险")){
            pwdLevel.setTextColor(Color.parseColor("#ff4d4f"));
            accountLevel.setTextColor(Color.parseColor("#ff4d4f"));
        }
        pwdLevel.setText(pwdType);
        accountLevel.setText(pwdType);
        String phone = UserUtils.getInstance().getUser().getPhone().substring(0, 3) + "****" + UserUtils.getInstance().getUser().getPhone().substring(7, 11);
        phone_tv.setText(phone);

    }

    private String getPwdLevel(String str) {
        int total = 0;
        int len = str.length();
        int upperCh = 0;
        int lowCh = 0;
        int digit = 0;
        int symbol = 0;
        int bonus = 0;
        if (len <= 4)
            total += 5;
        else if (len <= 7)
            total += 10;
        else
            total += 25;
        for (char c : str.toCharArray()) {
            if (c >= 'a' && c <= 'z')
                lowCh++;
            else if (c >= 'A' && c <= 'Z')
                upperCh++;
            else if (c >= '0' && c <= '9')
                digit++;
            else
                symbol++;
        }
        if ((lowCh == 0 && upperCh != 0) || (lowCh != 0 && upperCh == 0))
            total += 10;
        else if (lowCh != 0 && upperCh != 0)
            total += 20;
        if (digit == 1)
            total += 10;
        else if (digit > 1)
            total += 20;
        if (symbol == 1)
            total += 10;
        else if (symbol > 1)
            total += 25;
        if ((upperCh + lowCh != 0) && digit != 0 && symbol == 0)
            bonus = 2;
        else if ((upperCh + lowCh != 0) && digit != 0 && symbol != 0) {
            bonus = 3;
            if (upperCh != 0 && lowCh != 0)
                bonus = 5;
        }
        total += bonus;
        if (total >= 90)
            return "安全";
        else if (total >= 80)
            return "安全";
        else if (total >= 70)
            return "安全";
        else if (total >= 60)
            return "安全";
        else if (total >= 50)
            return "警告";
        else if (total >= 25)
            return "高风险";
        else if (total >= 0)
            return "高风险";
        return "高风险";
    }


    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        pwdChange.setOnClickListener(v ->startActivity(new Intent(SecurityCenterActivity.this,ChangePwdActivity.class)));
        emailChange.setOnClickListener(v ->{
            if(UserUtils.getInstance().getUser().getEmail()==null||UserUtils.getInstance().getUser().getEmail().equals("")){
                startActivity(new Intent(SecurityCenterActivity.this,NewEmailActivity.class));
            }else{
                startActivity(new Intent(SecurityCenterActivity.this,ChangeEmailActivity.class));
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