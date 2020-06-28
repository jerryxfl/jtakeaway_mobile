package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.responseBean.ResponseOrder;
import com.jerry.jtakeaway.custom.JAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//评价
public class EvaluateFragment extends BaseFragment {
    @BindView(R.id.evaluate_recyclerview)
    RecyclerView evaluate_recyclerview;

    private JAdapter<ResponseOrder> evaluateAdapter;
    private List<ResponseOrder> responseOrderList  = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.fragment_order_evaluate;
    }

    @Override
    public void InitView() {
        SignEventBus();
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        evaluate_recyclerview.setLayoutManager(layoutManager);
        evaluateAdapter=new JAdapter<ResponseOrder>(context, evaluate_recyclerview, new int[]{R.layout.order_evaluate_recyclerview}, new JAdapter.adapterListener<ResponseOrder>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ResponseOrder> datas) {

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
        responseOrderList.clear();
        responseOrderList.addAll(responseOrder);
        new Handler(Looper.getMainLooper()).postDelayed(() -> evaluateAdapter.adapter.setData(responseOrderList),2000);
    }
}
