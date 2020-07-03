package com.jerry.jtakeaway.ui.generalActivity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.model.Guide;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.guideRecyclerView)
    RecyclerView guideRecyclerView;

    @BindView(R.id.jump)
    TextView jump;

    @BindView(R.id.dort1)
    ImageView dort1;

    @BindView(R.id.dort2)
    ImageView dort2;

    @BindView(R.id.dort3)
    ImageView dort3;

    @BindView(R.id.text)
    TextView text;

    private JAdapter<Guide> guidejAdapter;
    private LinearLayoutManager layoutManager;
    private List<Guide> datas;

    @Override
    public int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void InitView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) jump.getLayoutParams();
        layoutParams.topMargin = PixAndDpUtil.getStatusBarHeight(this);
        jump.setLayoutParams(layoutParams);

        MMkvUtil.getInstance(this,"Configuration").encode("GUIDE",1);


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(guideRecyclerView);
        guideRecyclerView.setLayoutManager(layoutManager);
        guidejAdapter = new JAdapter<>(this, guideRecyclerView, new int[]{R.layout.guide_item}, new JAdapter.adapterListener<Guide>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Guide> datas) {
                ImageView bg =  holder.getView(R.id.bg);
                Glide.with(GuideActivity.this)
                        .load(datas.get(position).getImg())
                        .into(bg);
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Guide> datas) {

            }

            @Override
            public int getViewType(List<Guide> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        datas = new ArrayList<>();
        datas.add(new Guide(R.drawable.guide1,"匠心之作秘制羊肉串疯狂团购中!,\n                                          ---疯狂外卖"));
        datas.add(new Guide(R.drawable.guide2,"全新的吃货天堂,等你来探索\n                                          ---疯狂外卖"));
        datas.add(new Guide(R.drawable.guide3,"我们送达的除了外卖,还有爱和关心,\n                                          ---疯狂外卖"));
        guidejAdapter.adapter.setFooter(datas);
    }

    @Override
    public void InitListener() {
        jump.setOnClickListener(v->{
            startActivity(new Intent(GuideActivity.this,LoginActivity.class));
            finish();
        });
        guideRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition()+1;
                switch (firstVisibleItem){
                    case 1:
                        dort1.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.red_dort));
                        dort2.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        dort3.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        break;
                    case 2:
                        dort1.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        dort2.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.red_dort));
                        dort3.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        break;
                    case 3:
                        dort1.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        dort2.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.white_dort));
                        dort3.setImageDrawable(ContextCompat.getDrawable(GuideActivity.this,R.drawable.red_dort));
                        break;
                }
                text.setText(datas.get(firstVisibleItem-1).getText());
            }
        });
    }

    @Override
    public void destroy() {

    }
}