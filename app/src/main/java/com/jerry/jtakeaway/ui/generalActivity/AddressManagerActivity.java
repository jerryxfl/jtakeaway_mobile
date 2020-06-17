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
import com.jerry.jtakeaway.bean.model.Address;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
                address_tv.setText(datas.get(position).getDetaileAddress());

                edit_aib.setOnClickListener(v -> {
                    Intent intent = new Intent(AddressManagerActivity.this, EditAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address",datas.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent,1);
                });
                container.setOnClickListener(v ->{
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address",datas.get(position));
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads) {
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
                            address_tv.setText(addres.get(position).getDetaileAddress());
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
        return_aib.setOnClickListener(v -> finish());

    }

    @Override
    public void destroy() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            Address address = (Address) data.getSerializableExtra("address");
            int tag = 0;
            for (int i = 0; i < this.addres.size(); i++) {
                if(address.getId()==this.addres.get(i).getId()){
                    tag = i;
                    this.addres.get(i).setAddress(address.getAddress());
                    this.addres.get(i).setDetaileAddress(address.getDetaileAddress());
                    this.addres.get(i).setContact(address.getContact());
                    this.addres.get(i).setPhone(address.getPhone());
                    this.addres.get(i).setLabel(address.getLabel());
                };
            }
            jAdapter.adapter.notifyItemChanged(tag,"change");
            System.out.println("地址是:"+address.getAddress()+address.getDetaileAddress()+address.getContact()+address.getPhone()+address.getLabel()+"     "+tag);
        }
    }
}