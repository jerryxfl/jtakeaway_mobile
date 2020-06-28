package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.responseBean.ResponseOrder;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.ui.user.activity.MapActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//进行中
public class UnderwayFragment extends BaseFragment {

    @BindView(R.id.underway_recyclerview)
    RecyclerView underway_recyclerview;

    private JAdapter<ResponseOrder> underwayAdapter;
    private List<ResponseOrder> responseOrderList  = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.fragment_order_underway;
    }

    @Override
    public void InitView() {
        SignEventBus();
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        underway_recyclerview.setLayoutManager(layoutManager);
        underwayAdapter=new JAdapter<ResponseOrder>(context, underway_recyclerview, new int[]{R.layout.order_underway_recyclerview}, new JAdapter.adapterListener<ResponseOrder>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ResponseOrder> datas) {
                Button underway_btn_check = holder.getView(R.id.underway_btn_check);
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.underway_shop_img));

                underway_btn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, MapActivity.class));
                    }
                });

            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<ResponseOrder> datas) {

            }


            @Override
            public int getViewType(List<ResponseOrder> datas, int position) {
                return 0;
            }
        });


    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky =  true)
    public void DatasEvent(List<ResponseOrder> responseOrder){
        new Thread(() -> {
            List<ResponseOrder> responseOrders = new  ArrayList<ResponseOrder>();
            for (ResponseOrder r : responseOrder) {
                if(r.getStatus().getId()==1||r.getStatus().getId()==2||r.getStatus().getId()==5||r.getStatus().getId()==6||r.getStatus().getId()==7){
                    responseOrders.add(r);
                }
            }
            responseOrderList.addAll(responseOrders);
            new Handler(Looper.getMainLooper()).postDelayed(() -> underwayAdapter.adapter.setData(responseOrders),2000);
        }).start();
    }

}
