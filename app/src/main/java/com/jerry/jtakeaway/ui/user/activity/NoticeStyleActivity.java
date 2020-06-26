package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.model.NoticeStyle;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.util.List;

import butterknife.BindView;

public class NoticeStyleActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.noticeStyle_recyclerView)
    RecyclerView noticeStyle_recyclerView;

    @Override
    public int getLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        noticeStyle_recyclerView.setLayoutManager(layoutManager);
        JAdapter<NoticeStyle> noticeStyleJAdapter = new JAdapter<>(this, noticeStyle_recyclerView, new int[]{}, new JAdapter.adapterListener<NoticeStyle>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<NoticeStyle> datas) {

            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<NoticeStyle> datas) {

            }

            @Override
            public int getViewType(List<NoticeStyle> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());

    }

    @Override
    public void destroy() {

    }
}