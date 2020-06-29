package com.jerry.jtakeaway.ui.user.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Loginrecord;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecentlLoginRecordActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.loginRecordRecyclerView)
    RecyclerView loginRecordRecyclerView;
    private JAdapter<Loginrecord> recordJAdapter;
    private JCenterDialog jCenterDialog;
    private List<Loginrecord> loginrecordList = new ArrayList<Loginrecord>();

    @Override
    public int getLayout() {
        return R.layout.activity_recentl_login_record;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loginRecordRecyclerView.setLayoutManager(layoutManager);
        recordJAdapter = new JAdapter<>(this, loginRecordRecyclerView, new int[]{R.layout.login_record_item}, new JAdapter.adapterListener<Loginrecord>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Loginrecord> datas) {
                TextView address = holder.getView(R.id.address);
                TextView time = holder.getView(R.id.time);
                address.setText(datas.get(position).getAddress());
                Date date = new Date(datas.get(position).getLotintime().getTime());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time.setText(formatter.format(date));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Loginrecord> datas) {

            }

            @Override
            public int getViewType(List<Loginrecord> datas, int position) {
                return 0;
            }
        });
    }

    @Override
    public void InitData() {
        getLoginRecord();
    }

    private void getLoginRecord() {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.g_login_reord, this, new Callback() {
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
                    loginrecordList.addAll(GsonUtil.jsonToList(result.getData().toString(), Loginrecord.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        recordJAdapter.adapter.setHeader(loginrecordList);
                    });
                } else if (result.getCode() == 4) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(RecentlLoginRecordActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());

    }

    @Override
    public void destroy() {

    }
}