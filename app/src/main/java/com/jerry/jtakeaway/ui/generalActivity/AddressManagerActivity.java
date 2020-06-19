package com.jerry.jtakeaway.ui.generalActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.events.AddressEvent;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddressManagerActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;
    @BindView(R.id.address_recyvlerview)
    RecyclerView address_recyvlerview;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    private JAdapter<Address> jAdapter;
    private List<Address> addres = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_address_manager;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
        SignEventBus();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        address_recyvlerview.setLayoutManager(linearLayoutManager);
        jAdapter = new JAdapter<>(this, address_recyvlerview, new int[]{R.layout.address_item}, new JAdapter.adapterListener<Address>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Address> datas) {
                TextView contact_tv = holder.getView(R.id.contact_tv);
                TextView phone_tv = holder.getView(R.id.phone_tv);
                TextView label_tv = holder.getView(R.id.label_tv);
                TextView address_tv = holder.getView(R.id.address_tv);
                AniImgButton edit_aib = holder.getView(R.id.edit_aib);
                RelativeLayout container = holder.getView(R.id.container);

                contact_tv.setText(datas.get(position).getContact());
                phone_tv.setText(datas.get(position).getPhone());
                label_tv.setText(datas.get(position).getLabel());
                address_tv.setText(datas.get(position).getAddress()+"  "+datas.get(position).getDetaileaddress());

                edit_aib.setOnClickListener(v -> {
                    Intent intent = new Intent(AddressManagerActivity.this, EditAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address",datas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
                container.setOnClickListener(v ->{
                    EventBus.getDefault().post(new AddressEvent(datas.get(position),0));
                    finish();
                });
                container.setOnLongClickListener(v -> {
                    new SweetAlertDialog(AddressManagerActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("是否要删除该地址?")
                            .setConfirmText("是的")
                            .setConfirmClickListener(sDialog -> {
                                EventBus.getDefault().post(new AddressEvent(datas.get(position),2));
                                jAdapter.adapter.removeByObject(datas.get(position));
                                sDialog.dismissWithAnimation();
                            })
                            .setCancelText("不了")
                            .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                            .show();
                    return true;
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads,List<Address> datas) {
                TextView contact_tv = holder.getView(R.id.contact_tv);
                TextView phone_tv = holder.getView(R.id.phone_tv);
                TextView label_tv = holder.getView(R.id.label_tv);
                TextView address_tv = holder.getView(R.id.address_tv);

                for (Object payload : payloads) {
                    switch (String.valueOf(payload)) {
                        case "change":
                            contact_tv.setText(addres.get(position).getContact());
                            phone_tv.setText(addres.get(position).getPhone());
                            label_tv.setText(addres.get(position).getLabel());
                            address_tv.setText(datas.get(position).getAddress()+"  "+datas.get(position).getDetaileaddress());
                    }
                }


            }

            @Override
            public int getViewType(List<Address> datas, int position) {
                return 0;
            }
        });
    }

    @Override
    public void InitData() {
        //获得地址列表
        Intent intent = getIntent();
        addres = (List<Address>) intent.getSerializableExtra("addres");
        jAdapter.adapter.setFooter(addres);
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void destroy() {

    }


    //监听地址变化
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void AddressChanged(AddressEvent address){
        System.out.println("地址编辑界面收到信息:"+address.toString());
        int tag = 0;
        for (int i = 0; i < this.addres.size(); i++) {
            if(address.getAddress().getId()==this.addres.get(i).getId()){
                tag = i;
                this.addres.get(i).setAddress(address.getAddress().getAddress());
                this.addres.get(i).setDetaileaddress(address.getAddress().getDetaileaddress());
                this.addres.get(i).setContact(address.getAddress().getContact());
                this.addres.get(i).setPhone(address.getAddress().getPhone());
                this.addres.get(i).setLabel(address.getAddress().getLabel());
            };
        }
        jAdapter.adapter.notifyItemChanged(tag,"change");
    }

}