package com.jerry.jtakeaway.ui.user.fragment;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseFragment;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.eventBusEvents.BadgeDragEvent;
import com.jerry.jtakeaway.eventBusEvents.BadgeEvent;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EmailFragment extends BaseFragment {
    @BindView(R.id.email_recyclerview)
    RecyclerView email_recyclerview;

    private JAdapter<Msg>emailAdapter;
    private List<Msg> messages = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.fragment_email;
    }


    @Override
    public void InitView() {
        SignEventBus();
        LinearLayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        email_recyclerview.setLayoutManager(layoutManager);
        emailAdapter=new JAdapter<Msg>(context, email_recyclerview, new int[]{R.layout.email_recyclerview_item}, new JAdapter.adapterListener<Msg>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Msg> datas) {
                ImageView email_dot=holder.getView(R.id.email_dot);
                TextView email_title=holder.getView(R.id.email_title);
                TextView email_time=holder.getView(R.id.email_time);
                TextView content=holder.getView(R.id.content);

                if(datas.get(position).getReadalready()==0){
                    email_dot.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dot));
                }else{
                    email_dot.setImageDrawable(null);
                }

                Date date = datas.get(position).getSendTime();
                SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                email_time.setText(format.format(date));
                content.setText(datas.get(position).getContent());
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Msg> datas) {
                ImageView email_dot=holder.getView(R.id.email_dot);
                for (Object payload : payloads){
                    switch (String.valueOf(payload)){
                        case "change":
                            if(datas.get(position).getReadalready()==0){
                                email_dot.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dot));
                            }else{
                                email_dot.setImageDrawable(null);
                            }
                            break;
                    }
                }

            }

            @Override
            public int getViewType(List<Msg> datas, int position) {
                return 0;
            }
        });
    }



    @Override
    public void InitData() {
        getMessages();
    }

    private void getMessages() {
        OkHttp3Util.GET(JUrl.messages(messages.size()), context, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if(result.getCode() == 10000){
                    System.out.println("信息内容:"+ result.getData().toString());
                    List<Msg> msgs = new ArrayList<>(GsonUtil.parserJsonToArrayBeans(result.getData().toString(), Msg.class));
                    messages.addAll(msgs);
                    int size = 0;
                    for (Msg msg : messages){
                        if(msg.getReadalready()==0)size++;
                    }
                    EventBus.getDefault().post(new BadgeEvent(size));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        emailAdapter.adapter.setHeader(msgs);
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });

    }

    @Override
    public void InitListener() {

    }

    @Override
    public void destroy() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void setBadge(BadgeDragEvent badgeDragEvent) {
        System.out.println("收到drag请求");
        for (int i = 0; i < messages.size(); i++) {
            if(messages.get(i).getReadalready()==0)dragMessage(messages.get(i).getId(),i);

        }
    }


    private void dragMessage(int msgid,int position) {
        OkHttp3Util.GET(JUrl.msg_red(msgid), context, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if(result.getCode() == 10000){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        emailAdapter.adapter.notifyItemChanged(position,"change");
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }
}