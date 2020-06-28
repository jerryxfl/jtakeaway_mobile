package com.jerry.jtakeaway.ui.user.activity;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.Notification.NoticeStyles;
import com.jerry.jtakeaway.Notification.Notifications;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.model.NoticeStyle;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.MMkvUtil;
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

    @BindView(R.id.show)
    Button show;

    private int currentPosition = -1;
    private JAdapter<NoticeStyle> noticeStyleJAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        noticeStyle_recyclerView.setLayoutManager(layoutManager);
        noticeStyleJAdapter = new JAdapter<>(this, noticeStyle_recyclerView, new int[]{R.layout.notice_style_item}, new JAdapter.adapterListener<NoticeStyle>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<NoticeStyle> datas) {
                TextView nameStyle = holder.getView(R.id.nameStyle);
                RelativeLayout imgStyleWrapper = holder.getView(R.id.imgStyleWrapper);
                CardView wrapper = holder.getView(R.id.wrapper);
                ImageView imgStyle = holder.getView(R.id.imgStyle);
                nameStyle.setText(datas.get(position).getName());
                imgStyle.setImageDrawable(ContextCompat.getDrawable(NoticeStyleActivity.this,datas.get(position).getImg()));
                if(currentPosition==position){
                    imgStyleWrapper.setBackgroundColor(Color.parseColor("#ffa39e"));
                }else{
                    imgStyleWrapper.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                wrapper.setOnClickListener(v -> {
                    imgStyleWrapper.setBackgroundColor(Color.parseColor("#ffa39e"));
                    MMkvUtil.getInstance("Configuration").encode("NoticeStyle", position);
                    if(currentPosition!=position)noticeStyleJAdapter.adapter.notifyItemChanged(currentPosition);
                    currentPosition = position;
                });
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
        currentPosition = MMkvUtil.getInstance(this,"Configuration").decodeInt("NoticeStyle");
        noticeStyleJAdapter.adapter.setData(NoticeStyles.noticeStyles);
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        show.setOnClickListener(v ->{
            Notifications.sendNormalNotification(NoticeStyleActivity.this,"疯狂外卖","样式测试",1,-1);
        });
    }

    @Override
    public void destroy() {

    }
}