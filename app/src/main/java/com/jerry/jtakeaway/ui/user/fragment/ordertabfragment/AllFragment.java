package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//全部
public class AllFragment extends BaseFragment {
    @BindView(R.id.all_recyclerview)
    RecyclerView all_recyclerview;
    private JAdapter<Integer> allAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_order_all;
    }

    @Override
    public void InitView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        all_recyclerview.setLayoutManager(layoutManager);
        allAdapter=new JAdapter<Integer>(context, all_recyclerview, new int[]{R.layout.order_all_recyclerview}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {

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
        List<Integer>alldata=new ArrayList<>();
        alldata.add(R.drawable.cat);
        allAdapter.adapter.setHeader(alldata);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }
}
