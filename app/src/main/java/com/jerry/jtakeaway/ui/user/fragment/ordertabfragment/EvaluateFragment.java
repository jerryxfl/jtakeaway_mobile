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

//评价
public class EvaluateFragment extends BaseFragment {
    @BindView(R.id.evaluate_recyclerview)
    RecyclerView evaluate_recyclerview;

    private JAdapter<Integer> evaluateAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_evaluate;
    }

    @Override
    public void InitView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        evaluate_recyclerview.setLayoutManager(layoutManager);
        evaluateAdapter=new JAdapter<Integer>(context, evaluate_recyclerview, new int[]{R.layout.order_evaluate_recyclerview}, new JAdapter.adapterListener<Integer>() {
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
        List<Integer> evaluatedata= new ArrayList<Integer>();
        evaluatedata.add(R.drawable.concrete_road_between_trees_563356);
        evaluateAdapter.adapter.setHeader(evaluatedata);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }
}
