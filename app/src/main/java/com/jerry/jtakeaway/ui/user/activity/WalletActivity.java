package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.model.TIButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WalletActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.set_item)
    RecyclerView set_item;

    @BindView(R.id.return_btn)
    LinearLayout return_btn;

    private JAdapter<TIButton> jAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        JgridLayoutManager jgridLayoutManager = new JgridLayoutManager(this,1);
        set_item.setLayoutManager(jgridLayoutManager);
        jAdapter = new  JAdapter<>(this, set_item, new int[]{R.layout.setting_item}, new JAdapter.adapterListener<TIButton>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<TIButton> datas) {
                RelativeLayout container = holder.getView(R.id.container);
                ImageView img = holder.getView(R.id.img);
                TextView text = holder.getView(R.id.text);
                img.setImageDrawable(ContextCompat.getDrawable(WalletActivity.this,datas.get(position).getImg()));
                text.setText(datas.get(position).getText());
                container.setOnClickListener(v -> {
                    datas.get(position).getEvent();
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<TIButton> datas) {

            }

            @Override
            public int getViewType(List<TIButton> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        List<TIButton> settings = new ArrayList<>();
        settings.add(new TIButton(R.drawable.invest, "充值", new TIButton.Event() {
            @Override
            public void onClick() {

            }
        }));
        settings.add(new TIButton(R.drawable.wallet, "充值", new TIButton.Event() {
            @Override
            public void onClick() {

            }
        }));
        jAdapter.adapter.setData(settings);

    }

    @Override
    public void InitListener() {
        return_btn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void destroy() {

    }
}