package com.jerry.jtakeaway.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private int[] LayoutId;
    private List<T> datas = new ArrayList<T>();
    public String TAG = "video";

    public BaseAdapter(Context context, int[] layoutId) {
        this.context = context;
        this.LayoutId = layoutId;
    }


    public void setData(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setHeader(List<T> datas) {
        for (int i = 0; i < datas.size(); i++) {
            this.datas.add(0, datas.get(i));
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void setHeader(T datas) {
            this.datas.add(0, datas);
            notifyItemInserted(0);
            notifyDataSetChanged();
    }

    public void setHeaderOrderBack(List<T> datas) {
        for (int i = datas.size()-1; i >=0; i--) {
            this.datas.add(0, datas.get(i));
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void setFooter(List<T> datas) {
        for (int i = 0; i < datas.size(); i++) {
            this.datas.add(this.datas.size(), datas.get(i));
            notifyItemInserted(datas.size());
            notifyDataSetChanged();
        }
    }

    public void setFooter(T datas) {
        this.datas.add(this.datas.size(), datas);
        notifyItemInserted(this.datas.size());
        notifyDataSetChanged();
    }

    public void removeByPosition(int position) {//删除条目的位置
        datas.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void removeByObject(T item) {//删除的条目
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i) == item) {
                System.out.println("移除子项成功------------------------------");
                int position = datas.indexOf(item);
                datas.remove(item);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        }
    }

    public void changeData(int position){
        System.out.println("刷新");
        notifyItemChanged(position);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(LayoutId[viewType], parent, false);
        return new BaseViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        System.out.println(">>>>payloads"+payloads);
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
            return;
        }
        setItems.upDateItem(holder,position,payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        setItems.SetItems(holder, position, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return setItems.getViewType(datas, position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface setItems<T> {
        void SetItems(BaseViewHolder holder, int position, List<T> datas);
        void upDateItem(BaseViewHolder holder, int position, List<Object> payloads);
        int getViewType(List<T> datas, int position);
    }

    private setItems setItems;

    public void setItems(setItems setItems) {
        this.setItems = setItems;
    }

    public List<T> getDatas(){
        return datas;
    }

}
