package com.jerry.jtakeaway.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.bean.Comment;
import com.jerry.jtakeaway.ui.user.activity.UserCommentActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("all")
public class JBarrageView extends RelativeLayout {
    private List<Comment> barrageList = new ArrayList<Comment>();
    private int currentPosition = 0;
    private int width,height;


    public JBarrageView(Context context) {
        this(context, null);
    }

    public JBarrageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JBarrageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public JBarrageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    public void setBarrageList(List<Comment> barrageList){
        this.barrageList = barrageList;
        if(barrageList.size()!=0)handler.sendEmptyMessageDelayed(0,500);
    }

    private void addViews(View view) {
        System.out.println("控件高度:"+view.getHeight());
        int topMargin = (int) (Math.random()*(height-70));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = topMargin;
        view.setLayoutParams(params);
        addView(view);
        startTranslateAnim(view);
    }
    private void startTranslateAnim(final View view){
        int randomDuration = (int) (Math.random()*3000 + 8000);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"translationX",width,-width);
        objectAnimator.setDuration(randomDuration);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
        view.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                objectAnimator.pause();
                return true;
            }else if(event.getAction() != MotionEvent.ACTION_MOVE&&event.getAction() != MotionEvent.ACTION_DOWN){
                new Handler(Looper.getMainLooper()).postDelayed(objectAnimator::resume,3000);
                return true;
            }
            return false;
        });

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(barrageList.size()!=0){
                if(currentPosition==barrageList.size()){
                    currentPosition =0;
                }
                addViews(createView(barrageList.get(currentPosition)));
                currentPosition++;
                handler.sendEmptyMessageDelayed(0, (long) (Math.random()*1200 + 5000));
            }
        }
    };



    private View createView(Comment comment){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.barrage_layout,null);

        CircleImageView avatar =  view.findViewById(R.id.avatar);
        TextView name =  view.findViewById(R.id.tv_name);
        TextView content =  view.findViewById(R.id.tv_content);

        Glide.with(getContext())
                .load(comment.getUser().getUseradvatar())
                .into(avatar);
        avatar.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), UserCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("COMMENT",comment);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });
        name.setText(comment.getUser().getUsernickname());
        content.setText(comment.getContent());
        return view;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
    }


    public void onResume(){
        handler.sendEmptyMessageDelayed(0,500);
    }

    public void onPause(){
        handler.removeMessages(0);
    }

    public void onDestroy(){
        handler.removeMessages(0);
        handler = null;
    }
}
