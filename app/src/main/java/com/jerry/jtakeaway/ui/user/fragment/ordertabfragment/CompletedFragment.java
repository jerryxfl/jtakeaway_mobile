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

//完成
public class CompletedFragment extends BaseFragment {
    @BindView(R.id.completed_recyclerview)
    RecyclerView completed_recyclerview;

    private JAdapter<Integer> completedAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_order_completed;
    }

    @Override
    public void InitView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        completed_recyclerview.setLayoutManager(layoutManager);
        completedAdapter=new JAdapter<>(context, completed_recyclerview, new int[]{R.layout.order_completed_recyclerview}, new JAdapter.adapterListener<Integer>() {
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
        List<Integer> completeddata= new ArrayList<Integer>();
        completeddata.add(R.drawable.concrete_road_between_trees_563356);
        completedAdapter.adapter.setHeader(completeddata);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }
}
