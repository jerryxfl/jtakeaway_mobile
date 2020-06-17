package com.jerry.jtakeaway.ui.generalActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.model.Address;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

public class EditAddressActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.choose_address_tv)
    TextView choose_address_tv;

    @BindView(R.id.real_address_et)
    EditText real_address_et;

    @BindView(R.id.contact_et)
    EditText contact_et;

    @BindView(R.id.phone_et)
    EditText phone_et;

    @BindView(R.id.home_tv)
    TextView home_tv;

    @BindView(R.id.company_tv)
    TextView company_tv;

    @BindView(R.id.school_tv)
    TextView school_tv;

    @BindView(R.id.save_btn)
    Button save_btn;


    private String label = null;
    private int id = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_edit_address;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);
    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        Address address = (Address) intent.getSerializableExtra("address");
        if(address!=null){
            choose_address_tv.setText(address.getAddress());
            real_address_et.setText(address.getDetaileAddress());
            contact_et.setText(address.getContact());
            phone_et.setText(address.getPhone());
            id = address.getId();
            chooseLabel(address.getLabel());
        }

    }

    @Override
    public void InitListener() {
        choose_address_tv.setOnClickListener(v -> {
            startActivityForResult(new Intent(EditAddressActivity.this, AddressActivity.class),1);
        });
        save_btn.setOnClickListener(v ->{
           String address = choose_address_tv.getText().toString();
           String detaileAddress = real_address_et.getText().toString().trim();
           String contact = contact_et.getText().toString().trim();
           String phone = phone_et.getText().toString().trim();

           if(address.equals("点击选择")){
               Toast.makeText(EditAddressActivity.this,"请选择地址",Toast.LENGTH_SHORT).show();
                return;
           }
           if(detaileAddress.equals("")){
               real_address_et.setError("请填写详细地址");
               return;
           }

           if(contact.equals("")){
               company_tv.setError("请填写收货人姓名");
               return;
           }

           if(phone.equals("")){
               phone_et.setError("请填写收货人手机号");
               return;
           }

            Address mAddress = new Address();
            mAddress.setId(id);
            mAddress.setAddress(address);
            mAddress.setContact(contact);
            mAddress.setDetaileAddress(detaileAddress);
            mAddress.setPhone(phone);
            mAddress.setLabel(label);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("address",mAddress);
            intent.putExtras(bundle);
            setResult(1,intent);
            finish();
        });

        home_tv.setOnClickListener(v->{
            chooseLabel(home_tv.getText().toString());
        });

        company_tv.setOnClickListener(v -> {
            chooseLabel(company_tv.getText().toString());
        });

        school_tv.setOnClickListener(v -> {
            chooseLabel(school_tv.getText().toString());
        });
        return_aib.setOnClickListener(v -> finish());
    }

    private void chooseLabel(String label){


        home_tv.setTextColor(Color.BLACK);
        company_tv.setTextColor(Color.BLACK);
        school_tv.setTextColor(Color.BLACK);

        home_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_shape));
        company_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_shape));
        school_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_shape));

        if(!label.equals("")){
            if(label.contentEquals(home_tv.getText())){
                home_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_select_shape));
                home_tv.setTextColor(Color.WHITE);
            }else if(label.contentEquals(company_tv.getText())){
                company_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_select_shape));
                company_tv.setTextColor(Color.WHITE);
            }else if(label.contentEquals(school_tv.getText())){
                school_tv.setBackground(ContextCompat.getDrawable(this,R.drawable.address_select_shape));
                school_tv.setTextColor(Color.WHITE);
            }
        }
        this.label = label;
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            String address = data.getStringExtra("address");
            choose_address_tv.setText(address);
        }
    }

}