package com.jerry.jtakeaway.ui.user.fragment;

import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EmailFragment extends BaseFragment {
    @BindView(R.id.email_recyclerview)
    RecyclerView email_recyclerview;

    private JAdapter<Integer>emailAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_email;
    }


    @Override
    public void InitView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        email_recyclerview.setLayoutManager(layoutManager);
        emailAdapter=new JAdapter<Integer>(context, email_recyclerview, new int[]{R.layout.email_recyclerview_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                ImageView unfold_img=holder.getView(R.id.unfold_img);
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.email_dot));

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
        List<Integer> emaildata= new ArrayList<Integer>();
        emaildata.add(R.drawable.dot);
        emailAdapter.adapter.setHeader(emaildata);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }

}