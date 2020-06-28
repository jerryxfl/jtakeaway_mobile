package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

//退款
public class RefundFragment extends BaseFragment {

    @BindView(R.id.refund_recyclerview)
    RecyclerView refund_recyclerview;

    private List<ResponseOrder> responseOrderList  = new ArrayList<>();

    private JAdapter<ResponseOrder> refundAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_refund;
    }

    @Override
    public void InitView() {
        SignEventBus();
        JListLayoutManager jListLayoutManager=new JListLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        refund_recyclerview.setLayoutManager(jListLayoutManager);
        refundAdapter=new JAdapter<ResponseOrder>(context, refund_recyclerview, new int[]{R.layout.order_refund_recyclerview}, new JAdapter.adapterListener<ResponseOrder>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<ResponseOrder> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.refund_shop_img));
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
    class JListLayoutManager extends LinearLayoutManager{

        public JListLayoutManager(Context context) {
            super(context);
        }

        public JListLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public JListLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        @Override
        public boolean canScrollVertically() {
            return true;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky =  true)
    public void DatasEvent(List<ResponseOrder> responseOrder){
        new Thread(() -> {
            List<ResponseOrder> responseOrders = new  ArrayList<ResponseOrder>();
            for (ResponseOrder r : responseOrder) {
                if(r.getStatus().getId()==3){
                    responseOrders.add(r);
                }
            }
            responseOrderList.addAll(responseOrders);
            new Handler(Looper.getMainLooper()).postDelayed(() -> refundAdapter.adapter.setData(responseOrders),2000);
        }).start();
    }
}
