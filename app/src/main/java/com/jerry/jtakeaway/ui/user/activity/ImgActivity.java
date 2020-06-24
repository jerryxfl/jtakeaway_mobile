package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;

import java.util.List;

import butterknife.BindView;

public class ImgActivity extends BaseActivity {
    @BindView(R.id.img_recyclerview)
    RecyclerView img_recyclerview;

    @BindView(R.id.num_tv)
    TextView num_tv;
    private JAdapter<String> jAdapter;
    private List<String> imgs;
    private LinearLayoutManager layoutManager;

    @Override
    public int getLayout() {
        return R.layout.activity_img;
    }

    @Override
    public void InitView() {
        layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        img_recyclerview.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(img_recyclerview);
        jAdapter = new JAdapter<>(this, img_recyclerview, new int[]{R.layout.img_item}, new JAdapter.adapterListener<String>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<String> datas) {
                Glide.with(ImgActivity.this)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.PhotoView));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<String> datas) {

            }

            @Override
            public int getViewType(List<String> datas, int position) {
                return 0;
            }
        });
    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        imgs = (List<String>) intent.getSerializableExtra("IMGS");
        jAdapter.adapter.setFooter(imgs);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void InitListener() {
        img_recyclerview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            num_tv.setText(" "+(layoutManager.findFirstVisibleItemPosition()+1)+"/"+imgs.size()+" ");
        });
    }

    @Override
    public void destroy() {

    }
}