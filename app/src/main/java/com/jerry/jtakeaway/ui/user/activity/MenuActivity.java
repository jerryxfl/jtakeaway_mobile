package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.model.Address;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JTIButton;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.ui.generalActivity.AddressManagerActivity;
import com.jerry.jtakeaway.ui.generalActivity.EditAddressActivity;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.original_price_tv)
    TextView original_price_tv;//原价

    @BindView(R.id.server_people_jtib)
    JTIButton server_people_jtib;//客服按钮

    @BindView(R.id.shopping_car_jtib)
    JTIButton shopping_car_jtib;//购物车按钮

    @BindView(R.id.good_food_recyclerview)
    RecyclerView good_food_recyclerview;//精品菜列表


    //3个选择按钮
    @BindView(R.id.choose_conpon_aib)
    AniImgButton choose_conpon_aib;
    @BindView(R.id.choose_food_aib)
    AniImgButton choose_food_aib;
    @BindView(R.id.choose_address_aib)
    AniImgButton choose_address_aib;

    //地址
    @BindView(R.id.address_tv)
    TextView address_tv;


    //3个选择dialog
    JBottomDialog mConponDialog;
    JBottomDialog mFoodDialog;
    JBottomDialog mAddressDialog;


    private JAdapter<Integer> goodFoodAdapter;
    private JAdapter<Integer> conPonAdapter;
    private List<Address> address = new ArrayList<>();;

    @Override
    public int getLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        //设置下划线
        original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        //精品菜--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_hotShop = new JgridLayoutManager(this,2);
        good_food_recyclerview.setLayoutManager(jgridLayoutManager_hotShop);

        goodFoodAdapter = new JAdapter<Integer>(this, good_food_recyclerview, new int[]{R.layout.menu_item}, new JAdapter.adapterListener<Integer>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {
                Glide.with(MenuActivity.this)
                        .load(datas.get(position))
                        .into((ImageView) holder.getView(R.id.menu_img));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads) {

            }

            @Override
            public int getViewType(List<Integer> datas, int position) {
                return 0;
            }
        });
        //end 精品菜--------------------****************---------------------------------


    }

    @Override
    public void InitData() {
        List<Integer> datass = new ArrayList<Integer>();
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.concrete_road_between_trees_563356);
        datass.add(R.drawable.hot_art);
        datass.add(R.drawable.hot_art);
        datass.add(R.drawable.dribbble_music_corner);
        datass.add(R.drawable.dribbble_music_corner);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        datass.add(R.drawable.icon_dark_green_by_milkinside);
        goodFoodAdapter.adapter.setHeader(datass);



        address.add(new Address(1,"北京 天安门","正街","蒋锐1","18582672979","家"));
        address.add(new Address(2,"北京 天安门","正街","蒋锐2","18582672979","家"));
        address.add(new Address(3,"北京 天安门","正街","蒋锐3","18582672979","家"));

    }

    @Override
    public void InitListener() {
        server_people_jtib.SetOnclickListener(() -> {

        });
        shopping_car_jtib.SetOnclickListener(() -> {

        });

        //选择优惠卷
        choose_conpon_aib.setOnClickListener(v -> {
            if(mConponDialog==null){
                mConponDialog = new JBottomDialog(this, R.layout.conpon_dialog, view -> {
                    RecyclerView recyclerView = view.findViewById(R.id.conpon_recyclview);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MenuActivity.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    conPonAdapter = new JAdapter<Integer>(MenuActivity.this, recyclerView, new int[]{R.layout.conpon_pay_item}, new JAdapter.adapterListener<Integer>() {
                        @Override
                        public void setItems(BaseViewHolder holder, int position, List<Integer> datas) {

                        }

                        @Override
                        public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads) {

                        }

                        @Override
                        public int getViewType(List<Integer> datas, int position) {
                            return 0;
                        }
                    });
                    List<Integer> datass = new ArrayList<Integer>();
                    for (int i = 0; i < 20; i++) {
                        datass.add(i);
                    }
                    conPonAdapter.adapter.setHeader(datass);


                });
                mConponDialog.show();
            }else{
                mConponDialog.show();
            }
        });

        choose_food_aib.setOnClickListener(v -> {
            if(mFoodDialog==null){
                mFoodDialog = new JBottomDialog(MenuActivity.this, R.layout.food_dialog, view -> {
                    view.findViewById(R.id.menu).setOnClickListener(v1 -> {

                    });
                });
                mFoodDialog.show();
            }else{
                mFoodDialog.show();
            }
        });

        //选择地址
        choose_address_aib.setOnClickListener(v -> {
            if(address.isEmpty()){
                startActivityForResult(new Intent(MenuActivity.this, EditAddressActivity.class),1);
            }else {
                Intent intent = new Intent(MenuActivity.this, AddressManagerActivity.class);
                intent.putExtra("addres", (Serializable) address);
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    public void destroy() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            Address address = (Address) data.getSerializableExtra("address");
            System.out.println("地址是:"+address.getAddress()+address.getDetaileAddress()+address.getContact()+address.getPhone()+address.getLabel());
            this.address.add(address);
            address_tv.setText(address.getAddress()+"  "+address.getDetaileAddress());
        }
    }
}