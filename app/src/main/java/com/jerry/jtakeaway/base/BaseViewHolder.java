package com.jerry.jtakeaway.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private Context context;

    public BaseViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        views = new SparseArray<>(8);
    }

    public static BaseViewHolder getRecyclerHolder(Context context, View itemView) {
        return new BaseViewHolder(itemView, context);
    }

    public SparseArray<View> getViews() {
        return this.views;
    }


    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseViewHolder setImageResourse(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setImageUrl(int viewId, String url) {
        ImageView iv = getView(viewId);
        Glide.with(context).load(url).into(iv);
        return this;
    }
}
