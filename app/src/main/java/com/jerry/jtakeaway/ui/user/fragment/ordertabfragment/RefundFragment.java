package com.jerry.jtakeaway.ui.user.fragment.ordertabfragment;

import android.content.Context;
import android.util.AttributeSet;
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

//退款
public class RefundFragment extends BaseFragment {

    @BindView(R.id.refund_recyclerview)
    RecyclerView refund_recyclerview;

    private JAdapter<Integer> refundAdapter;
    @Override
    public int getLayout() {
        return R.layout.fragment_order_refund;
    }

    @Override
    public void InitView() {
        JListLayoutManager jListLayoutManager=new JListLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        refund_recyclerview.setLayoutManager(jListLayoutManager);
        refundAdapter=new JAdapter<Integer>(context, refund_recyclerview, new int[]{R.layout.order_refund_recyclerview}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(context)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.refund_shop_img));
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
        List<Integer> refunddata = new ArrayList<Integer>();
        refunddata.add(R.drawable.concrete_road_between_trees_563356);
        refunddata.add(R.drawable.concrete_road_between_trees_563356);
        refunddata.add(R.drawable.concrete_road_between_trees_563356);
        refunddata.add(R.drawable.concrete_road_between_trees_563356);
        refundAdapter.adapter.setHeader(refunddata);
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


}
