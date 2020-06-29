package com.jerry.jtakeaway.custom;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.base.BaseAdapter;
import com.jerry.jtakeaway.base.BaseViewHolder;

import java.util.List;

public class JAdapter<T> implements BaseAdapter.setItems<T> {

    public final BaseAdapter<T> adapter;
    private adapterListener<T> adapterListener;

    public JAdapter(Context context, RecyclerView recyclerview, int[] layouts, adapterListener<T> adapterListener) {
        this.adapterListener = adapterListener;
        adapter = new BaseAdapter<T>(context, layouts);
        adapter.setItems(this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void SetItems(BaseViewHolder holder, int position, List<T> datas) {
        adapterListener.setItems(holder, position, datas);
    }

    @Override
    public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<T> datas) {
        adapterListener.upDateItem(holder, position, payloads, datas);
    }

    @Override
    public int getViewType(List<T> datas, int position) {
        return adapterListener.getViewType(datas, position);
    }

    public interface adapterListener<T> {
        void setItems(BaseViewHolder holder, int position, List<T> datas);

        void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<T> datas);

        int getViewType(List<T> datas, int position);
    }
}
