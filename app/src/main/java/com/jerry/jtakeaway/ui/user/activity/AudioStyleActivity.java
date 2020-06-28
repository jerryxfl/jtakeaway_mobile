package com.jerry.jtakeaway.ui.user.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.jerry.jtakeaway.Notification.NotificationAudios;
import com.jerry.jtakeaway.Notification.NotificationChannels;
import com.jerry.jtakeaway.Notification.Notifications;
import com.jerry.jtakeaway.Notification.model.Audio;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JAudioImgView;
import com.jerry.jtakeaway.utils.MMkvUtil;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.SoundUtil;

import java.util.List;

import butterknife.BindView;

public class AudioStyleActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.Audio_recyclerView)
    RecyclerView Audio_recyclerView;

    @BindView(R.id.show)
    Button show;

    private JAdapter<Audio> audioJAdapter;
    private int currentPosition = 0;


    @Override
    public int getLayout() {   
        return R.layout.activity_audio;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        Audio_recyclerView.setLayoutManager(layoutManager);
        audioJAdapter = new JAdapter<>(this, Audio_recyclerView, new int[]{R.layout.audio_item}, new JAdapter.adapterListener<Audio>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Audio> datas) {
                JAudioImgView audioImg =  holder.getView(R.id.audioImg);
                ImageView audio_Img =  holder.getView(R.id.audio_Img);
                TextView audioTitle =  holder.getView(R.id.audioTitle);
                LottieAnimationView audioSpectrum =  holder.getView(R.id.audioSpectrum);
                if(currentPosition == position){
                    audioImg.setOnSelected(true);
                }else{
                    audioImg.setOnSelected(false);
                    audioSpectrum.pauseAnimation();
                    audioSpectrum.setProgress(0);
                    SoundUtil.stop(datas.get(position).getSource());
                }
                audio_Img.setImageDrawable(ContextCompat.getDrawable(AudioStyleActivity.this,datas.get(position).getImg()));
                audioTitle.setText(datas.get(position).getName());
                audioImg.setOnClickListener(() -> {
                    audioImg.setOnSelected(true);
                    audioSpectrum.playAnimation();
                    MMkvUtil.getInstance(AudioStyleActivity.this,"Configuration").encode("Audio",datas.get(position).getName());
                    NotificationChannels.changeChannels(AudioStyleActivity.this);
                    NotificationChannels.createAllNotificationChannels(AudioStyleActivity.this);
                    SoundUtil.stop(datas.get(position).getSource());
                    SoundUtil.play(AudioStyleActivity.this,datas.get(position).getSource());
                    if(currentPosition!=position) {
                        audioJAdapter.adapter.notifyItemChanged(currentPosition);
                    }
                    currentPosition = position;
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Audio> datas) {

            }

            @Override
            public int getViewType(List<Audio> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        for (int i = 0; i < NotificationAudios.audios.size(); i++) {
            if(MMkvUtil.getInstance(this,"Configuration").decodeString("Audio").equals(NotificationAudios.audios.get(i).getName())){
                currentPosition = i;
            }
        }
        audioJAdapter.adapter.setData(NotificationAudios.audios);
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        show.setOnClickListener(v ->{
            Notifications.sendNormalNotification(AudioStyleActivity.this,"疯狂外卖","音效测试",1,-1);
        });
    }

    @Override
    public void destroy() {
        SoundUtil.release();
    }
}