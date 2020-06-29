package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.responseBean.ResponseOrder;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.ui.user.activity.MenuActivity;
import com.jerry.jtakeaway.ui.user.activity.ShopActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

//完成
public class CompletedFragment extends BaseFragment {
    @BindView(R.id.completed_recyclerview)
    RecyclerView completed_recyclerview;

    private JAdapter<ResponseOrder> completedAdapter;
    private List<ResponseOrder> responseOrderList  = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.fragment_order_completed;
    }

    @Override
    public void InitView() {
        SignEventBus();
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        completed_recyclerview.setLayoutManager(layoutManager);
        completedAdapter=new JAdapter<>(context, completed_recyclerview, new int[]{R.layout.order_completed_recyclerview}, new JAdapter.adapterListener<ResponseOrder>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ResponseOrder> datas) {
                ImageView all_shop_img = holder.getView(R.id.all_shop_img);
                TextView all_shop_name = holder.getView(R.id.all_shop_name);
                ImageView all_fruit_img = holder.getView(R.id.all_fruit_img);
                TextView all_time = holder.getView(R.id.all_time);
                TextView all_price = holder.getView(R.id.all_price);
                TextView all_status = holder.getView(R.id.all_status);

                Glide.with(context)
                        .load(datas.get(position).getSuser().getUseradvatar())
                        .into(all_shop_img);
                Glide.with(context)
                        .load(datas.get(position).getMenus().getFoodimg())
                        .into(all_fruit_img);

                all_shop_name.setText(datas.get(position).getSsuser().getShopname());
                Date date = datas.get(position).getCreatedTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                all_time.setText(simpleDateFormat.format(date));
                JSONObject json = JSONObject.parseObject(datas.get(position).getDetailedinformation());
                Menus menu = json.getObject("menu",Menus.class);
                all_price.setText("总价: $"+menu.getFoodprice()*datas.get(position).getMenuSize());
                all_status.setText(datas.get(position).getStatus().getStatusdesc());

                all_shop_img.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ShopActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop",datas.get(position).getSsuser());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });

                all_fruit_img.setOnClickListener(v -> {
                    Intent intent = new Intent(context, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("menu", datas.get(position).getMenus());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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
                if(r.getStatus().getId()==4){
                    responseOrders.add(r);
                }
            }
            responseOrderList.addAll(responseOrders);
            new Handler(Looper.getMainLooper()).postDelayed(() -> completedAdapter.adapter.setHeader(responseOrders),2000);
        }).start();
    }
}
