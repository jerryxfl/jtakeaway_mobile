package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.ui.user.activity.MapActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//进行中
public class UnderwayFragment extends BaseFragment {

    @BindView(R.id.underway_recyclerview)
    RecyclerView underway_recyclerview;

    private JAdapter<Integer> underwayAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_underway;
    }

    @Override
    public void InitView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        underway_recyclerview.setLayoutManager(layoutManager);
        underwayAdapter=new JAdapter<Integer>(context, underway_recyclerview, new int[]{R.layout.order_underway_recyclerview}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
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
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Integer> datas) {

            }


            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });


    }

    @Override
    public void InitData() {
        List<Integer> underwaydata = new ArrayList<Integer>();
        underwaydata.add(R.drawable.concrete_road_between_trees_563356);
        underwaydata.add(R.drawable.concrete_road_between_trees_563356);
        underwaydata.add(R.drawable.concrete_road_between_trees_563356);
        underwaydata.add(R.drawable.concrete_road_between_trees_563356);
        underwayAdapter.adapter.setHeader(underwaydata);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }

}
