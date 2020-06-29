package com.jerry.jtakeaway.ui.user.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.ResponseTransaction;
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
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TransactionActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;


    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.tranRecyclerView)
    RecyclerView tranRecyclerView;
    private JAdapter<ResponseTransaction> transactionJAdapter;
    private List<ResponseTransaction> responseTransactionList = new ArrayList<>();
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_transaction;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        tranRecyclerView.setLayoutManager(layoutManager);
        transactionJAdapter = new JAdapter<>(this, tranRecyclerView, new int[]{R.layout.transaction_item}, new JAdapter.adapterListener<ResponseTransaction>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ResponseTransaction> datas) {
                CircleImageView avatar = holder.getView(R.id.avatar);
                TextView descr = holder.getView(R.id.descr);
                TextView time = holder.getView(R.id.time);
                TextView money = holder.getView(R.id.money);
                double payMoney = datas.get(position).getJtransaction().getPaymoney();
                if(payMoney>0){
                    Glide.with(TransactionActivity.this)
                            .load(datas.get(position).getTargetUser().getUseradvatar())
                            .into(avatar);
                }else{
                    Glide.with(TransactionActivity.this)
                            .load(datas.get(position).getUser().getUseradvatar())
                            .into(avatar);
                }
                descr.setText(datas.get(position).getJtransaction().getMore());
                Date date = datas.get(position).getJtransaction().getPaytime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                time.setText(simpleDateFormat.format(date));
                money.setText(datas.get(position).getJtransaction().getPaymoney()+"");


            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<ResponseTransaction> datas) {

            }

            @Override
            public int getViewType(List<ResponseTransaction> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        getTransactions();
    }

    private void getTransactions() {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.transactions, this, new Callback() {
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
                if(result.getCode() == 10000){
                    responseTransactionList.addAll(GsonUtil.parserJsonToArrayBeans(result.getData().toString(),ResponseTransaction.class));
                    System.out.println("交易记录:"+result.getData().toString());
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        transactionJAdapter.adapter.setHeader(responseTransactionList);
                    });
                }else if(result.getCode()==4){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(TransactionActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
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