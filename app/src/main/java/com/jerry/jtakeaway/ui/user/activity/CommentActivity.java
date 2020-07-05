package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Comment;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommentActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;


    @BindView(R.id.comment_recyclerView)
    RecyclerView comment_recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    List<Comment> commentList = new ArrayList<Comment>();
    private JAdapter<Comment> commentJAdapter;
    private Suser suser;

    @Override
    public int getLayout() {
        return R.layout.activity_comment;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comment_recyclerView.setLayoutManager(layoutManager);
        commentJAdapter = new JAdapter<>(this, comment_recyclerView, new int[]{R.layout.comment_item}, new JAdapter.adapterListener<Comment>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Comment> datas) {
                CircleImageView avatar = holder.getView(R.id.avatar);
                TextView userNickName = holder.getView(R.id.userNickName);
                TextView time = holder.getView(R.id.time);
                TextView content = holder.getView(R.id.content);
                CardView container = holder.getView(R.id.container);

                Glide.with(CommentActivity.this)
                        .load(datas.get(position).getUser().getUseradvatar())
                        .into(avatar);

                userNickName.setText(datas.get(position).getUser().getUsernickname());
                Date date = datas.get(position).getCreatetime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                time.setText(dateFormat.format(date));
                content.setText(datas.get(position).getContent());
                container.setOnClickListener(v->{
                    Intent intent = new Intent(CommentActivity.this, UserCommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("COMMENT",datas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Comment> datas) {

            }

            @Override
            public int getViewType(List<Comment> datas, int position) {
                return 0;
            }
        });

    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        suser = (Suser) intent.getSerializableExtra("SUSER");
        if(suser !=null){
            getMsg(suser);
        }

    }


    private void getMsg(Suser suser) {
        OkHttp3Util.GET(JUrl.shop_comment(suser.getId()), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    List<Comment> comments = new ArrayList<>();
                    List<Comment> newComments = new ArrayList<>();
                    comments.addAll(GsonUtil.parserJsonToArrayBeans(result.getData().toString(), Comment.class));
                    for (int i = 0; i < comments.size(); i++) {
                        boolean add = true;

                        for (int j = 0; j < commentList.size(); j++) {
                            if(comments.get(i).getId() == commentList.get(j).getId()){
                                add =false;
                                break;
                            }
                        }

                        if(add)newComments.add(comments.get(i));
                    }

                    commentList.addAll(newComments);

                    new Handler(Looper.getMainLooper()).post(() -> {
                        commentJAdapter.adapter.setHeader(newComments);
                        refresh.setRefreshing(false);
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(CommentActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }


    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        refresh.setOnRefreshListener(() -> getMsg(suser));
        comment_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refresh.setEnabled(topRowVerticalPosition >= 0 && recyclerView != null && !recyclerView.canScrollVertically(-1));

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void destroy() {

    }
}