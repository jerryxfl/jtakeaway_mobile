package com.jerry.jtakeaway.ui.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.events.AddressEvent;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JAdapter;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.custom.JTIButton;
import com.jerry.jtakeaway.custom.JgridLayoutManager;
import com.jerry.jtakeaway.ui.generalActivity.AddressManagerActivity;
import com.jerry.jtakeaway.ui.generalActivity.EditAddressActivity;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.original_price_tv)
    TextView original_price_tv;//原价

    @BindView(R.id.nowPrice)
    TextView nowPrice;//现价

    @BindView(R.id.DiscountLabel)
    TextView DiscountLabel;//打折标签

    @BindView(R.id.DiscountTime)
    TextView DiscountTime;//打折时间

    @BindView(R.id.DiscountText)
    TextView DiscountText;//打折描述

    @BindView(R.id.descr_tv)
    TextView descr_tv;

    @BindView(R.id.moreinfo_tv)
    TextView moreinfo_tv;


    @BindView(R.id.foodImg)
    ImageView foodImg;

    @BindView(R.id.priceOld)
    LinearLayout priceOld;

    @BindView(R.id.shopName)
    TextView shopName;

    @BindView(R.id.conpon_btn)
    TextView conpon_btn;

    @BindView(R.id.conponsize_tv)
    TextView conponsize_tv;

    @BindView(R.id.choseFood_tv)
    TextView choseFood_tv;


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

    //支付按钮
    @BindView(R.id.pay_rel)
    RelativeLayout pay_rel;

    //3个选择dialog
    JBottomDialog mConponDialog;
    JBottomDialog mFoodDialog;
    JBottomDialog mAddressDialog;


    private JAdapter<Menus> goodFoodAdapter;
    private JAdapter<Coupon> conPonAdapter;
    private List<Address> address = new ArrayList<>();
    private List<Menus> menus = new ArrayList<>();
    private Menus menu;


    private List<Coupon> couponList = new ArrayList<>();
    private List<Orde> all_orders = new ArrayList<>();
    private Suser suser;
    private Address setAddress;
    private Menus setMenus;
    private JCenterDialog jCenterDialog;


    @Override
    public int getLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);


        //注册事件监听
        SignEventBus();


        //精品菜--------------------****************---------------------------------
        JgridLayoutManager jgridLayoutManager_hotShop = new JgridLayoutManager(this, 2);
        good_food_recyclerview.setLayoutManager(jgridLayoutManager_hotShop);

        goodFoodAdapter = new JAdapter<Menus>(this, good_food_recyclerview, new int[]{R.layout.menu_item}, new JAdapter.adapterListener<Menus>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<Menus> datas) {
                ImageView imageView = (ImageView) holder.getView(R.id.menu_img);
                Glide.with(MenuActivity.this)
                        .load(datas.get(position).getFoodimg())
                        .into(imageView);
                imageView.setOnClickListener(v -> {
                    Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("menu", datas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Menus> datas) {

            }

            @Override
            public int getViewType(List<Menus> datas, int position) {
                return 0;
            }
        });
        //end 精品菜--------------------****************---------------------------------


    }

    @Override
    public void InitData() {
        //获得intent数据
        Intent intent = getIntent();
        menu = (Menus) intent.getSerializableExtra("menu");
        setData(menu);
        getSuserInfo(menu.getSuerid());
        getAddress();
        getConpon();
        getOrders();
    }


    //获得所有订单
    private void getOrders() {
        OkHttp3Util.GET(JUrl.all_no_pay_orders, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                System.out.println(jsonObject.toString());
                if (result.getCode() == 10000) {
                    all_orders.clear();
                    all_orders.addAll(GsonUtil.jsonToList(result.getData().toString(), Orde.class));
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getMenus() {
        //获得推荐菜单
        OkHttp3Util.GET(JUrl.g_menus(suser.getId(), menus.size()), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                System.out.println(jsonObject.toString());
                if (result.getCode() == 10000) {
                    menus.addAll(GsonUtil.jsonToList(result.getData().toString(), Menus.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (goodFoodAdapter != null) goodFoodAdapter.adapter.setFooter(menus);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    private void getAddress() {
        OkHttp3Util.GET(JUrl.address, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    address.addAll(GsonUtil.jsonToList(result.getData().toString(), com.jerry.jtakeaway.bean.Address.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (!address.isEmpty())
                            if (address_tv != null)
                                address_tv.setText(address.get(0).getAddress() + address.get(0).getDetaileaddress());
                        setAddress = address.get(0);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    private void getSuserInfo(int suserid) {

        OkHttp3Util.GET(JUrl.shop + suserid, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if (result.getCode() == 10000) {
                    suser = GsonUtil.gsonToBean(result.getData().toString(), Suser.class);
                    getMenus();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (moreinfo_tv != null) moreinfo_tv.setText(suser.getShopaddress());
                        if (shopName != null) shopName.setText(suser.getShopname() + ">");
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });

    }

    private void getConpon() {
        OkHttp3Util.GET(JUrl.m_conpon, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    System.out.println(result.getData().toString());
                    couponList.clear();
                    couponList.addAll(GsonUtil.jsonToList(result.getData().toString(), Coupon.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (conPonAdapter != null) conPonAdapter.adapter.setData(couponList);
                        if (conponsize_tv != null)
                            conponsize_tv.setText("[美食专享] 美食卷 x" + couponList.size());
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    private void setData(Menus menu) {
        if (menu.getFoodlowprice() != 0) {
            //设置下划线
            original_price_tv.setText("$" + menu.getFoodprice());
            original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            nowPrice.setText("$" + menu.getFoodlowprice());
            if (menu.getLowpricefailed() != null) {
                setTime();
            } else {
                DiscountTime.setVisibility(View.GONE);
            }
        } else {
            priceOld.setVisibility(View.GONE);
            DiscountLabel.setVisibility(View.GONE);
            DiscountTime.setVisibility(View.GONE);
            nowPrice.setText("$" + menu.getFoodprice());
        }
        descr_tv.setText(menu.getFooddesc());
        Glide.with(this)
                .load(menu.getFoodimg())
                .into(foodImg);
    }


    private void setTime() {
        Date failTime = new Date(menu.getLowpricefailed().getTime());
        Date now = new Date();
        if (now.before(failTime)) {
            long l = failTime.getTime() - now.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println(day + ":" + hour + ":" + min + ":" + s + ":");
            time.start();
        } else {
            DiscountTime.setVisibility(View.GONE);
            menu.setLowpricefailed(null);
            menu.setFoodlowprice(0);
            setData(menu);
        }
    }

    Thread time = new Thread(new Runnable() {
        @Override
        public void run() {
            Date failTime = new Date(menu.getLowpricefailed().getTime());
            Date now = new Date();
            while (now.before(failTime)) {
                long l = failTime.getTime() - new Date().getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                System.out.println(day + ":" + hour + ":" + min + ":" + s);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (DiscountTime != null)
                        DiscountTime.setText(day + ":" + hour + ":" + min + ":" + s);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (DiscountTime != null) DiscountTime.setVisibility(View.GONE);
            if (menu != null) {
                menu.setLowpricefailed(null);
                menu.setFoodlowprice(0);
                setData(menu);
            }
        }
    });


    @Override
    public void InitListener() {
        server_people_jtib.SetOnclickListener(() -> {

        });
        shopping_car_jtib.SetOnclickListener(() -> {

        });

        //选择优惠卷
        choose_conpon_aib.setOnClickListener(v -> {
            if (mConponDialog == null) {
                mConponDialog = new JBottomDialog(this, R.layout.conpon_dialog, view -> {
                    RecyclerView recyclerView = view.findViewById(R.id.conpon_recyclview);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MenuActivity.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    conPonAdapter = new JAdapter<>(MenuActivity.this, recyclerView, new int[]{R.layout.conpon_pay_item_nbtn}, new JAdapter.adapterListener<Coupon>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void setItems(BaseViewHolder holder, int position, List<Coupon> datas) {
                            TextView discount = holder.getView(R.id.discount);//折扣
                            TextView name = holder.getView(R.id.name);//名字
                            TextView failtime = holder.getView(R.id.failtime);//过期时间

                            discount.setText(datas.get(position).getConponprice() + "折");
                            name.setText(datas.get(position).getConpondesc());
                            failtime.setText(datas.get(position).getConponfailuretime() + "到期");
                        }

                        @Override
                        public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<Coupon> datas) {

                        }

                        @Override
                        public int getViewType(List<Coupon> datas, int position) {
                            return 0;
                        }
                    });
                });
                getConpon();
                mConponDialog.show();
            } else {
                getConpon();
                mConponDialog.show();
            }
        });

        choose_food_aib.setOnClickListener(v -> {
            if (mFoodDialog == null) {
                mFoodDialog = new JBottomDialog(MenuActivity.this, R.layout.food_dialog, view -> {
                    ImageView foodImg = view.findViewById(R.id.foodImg);
                    ImageView foodImg2 = view.findViewById(R.id.foodImg2);
                    TextView foodName = view.findViewById(R.id.foodName);
                    TextView foodName2 = view.findViewById(R.id.foodName2);
                    TextView shopLocation = view.findViewById(R.id.shopLocation);
                    TextView shopDistance = view.findViewById(R.id.shopDistance);
                    LinearLayout menuBtn = view.findViewById(R.id.menu);

                    Glide.with(MenuActivity.this)
                            .load(menu.getFoodimg())
                            .into(foodImg);

                    Glide.with(MenuActivity.this)
                            .load(menu.getFoodimg())
                            .into(foodImg2);

                    foodName.setText(menu.getFoodname());
                    foodName2.setText(menu.getFoodname());

                    shopLocation.setText(suser.getShopaddress());
                    shopDistance.setText("距离:100KM");

                    menuBtn.setOnClickListener(v2 -> {
                        setMenus = menu;
                        choseFood_tv.setText(menu.getFoodname());
                        mFoodDialog.dismiss();
                    });
                });
                mFoodDialog.show();
            } else {
                mFoodDialog.show();
            }
        });

        //选择地址
        choose_address_aib.setOnClickListener(v -> {
            if (address.isEmpty()) {
                startActivity(new Intent(MenuActivity.this, EditAddressActivity.class));
            } else {
                Intent intent = new Intent(MenuActivity.this, AddressManagerActivity.class);
                intent.putExtra("addres", (Serializable) address);
                startActivity(intent);
            }
        });

        //立即支付
        pay_rel.setOnClickListener(v -> {
            if (setMenus == null) {
                Toast.makeText(MenuActivity.this, "请选择购买物品", Toast.LENGTH_SHORT).show();
                return;
            }

            if (setAddress == null) {
                Toast.makeText(MenuActivity.this, "请选择配送地址", Toast.LENGTH_SHORT).show();
                return;
            }
            //生成订单
            createOrder(suser.getId(), setMenus.getId(), 1);
        });

        shopName.setOnClickListener(v -> {
            //点击店家名字
            Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("shop", suser);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        conpon_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ConponGetActivity.class);
            intent.putExtra("targetid", suser.getId());
            startActivity(intent);
        });

        return_aib.setOnClickListener(v -> finish());
    }

    @Override
    public void destroy() {
        if (time.isAlive()) {
            time.interrupt();
        }
    }


    //    生成订单
    private void createOrder(int suserid, int menuid, int size) {
        System.out.println("本地" + suserid + "  " + menuid + "   " + size);
        boolean tag = false;
        for (Orde orde : all_orders) {
            if (orde.getSuserid() == suserid  &&orde.getStatusid() == 1) {
                com.alibaba.fastjson.JSONObject json = JSONObject.parseObject(orde.getMenus());
                for (Map.Entry<String, Object> entry : json.entrySet()) {
                    System.out.println("在线" + entry.getKey() + "  " + entry.getValue());
                    if (entry.getKey().equals(String.valueOf(menuid)) && entry.getValue().equals(size)) {
                        tag = true;
                        //弹出框提醒
                        new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("你的购物车中有一个相同的订单还未支付,是否确认创建新订单??")
                                .setConfirmText("创建")
                                .setConfirmClickListener(sDialog -> {
                                    createOrder_yes(suserid, menuid, size);
                                    sDialog.dismissWithAnimation();
                                })
                                .setCancelText("去看看")
                                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    }
                }
                if (tag) break;
            }
        }
        if (!tag) {
            createOrder_yes(suserid, menuid, size);
        }

    }


    private void createOrder_yes(int suserid, int menuid, int size) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();

        OkHttp3Util.GET(JUrl.create_order(suserid, menuid, size), this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if (result.getCode() == 10000) {
                    System.out.println("生成订单-------------------------" + result.getData().toString());
                    Orde orde = GsonUtil.gsonToBean(result.getData().toString(), Orde.class);
                    all_orders.add(orde);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Intent intent = new Intent(MenuActivity.this, PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", orde);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                } else if (result.getCode() == 6) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(MenuActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(MenuActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }


    //监听地址变化
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AddressChanged(AddressEvent address) {
        System.out.println("菜单编辑界面收到信息:");
        switch (address.getEventType()) {
            case 0:
                for (int i = 0; i < this.address.size(); i++) {
                    if (this.address.get(i).getId() == address.getAddress().getId()) {
                        this.address.get(i).setAddress(address.getAddress().getAddress());
                        this.address.get(i).setDetaileaddress(address.getAddress().getDetaileaddress());
                        this.address.get(i).setContact(address.getAddress().getContact());
                        this.address.get(i).setPhone(address.getAddress().getPhone());
                        this.address.get(i).setLabel(address.getAddress().getLabel());
                        if (address.getAddress().getId() == setAddress.getId()) {
                            if (address_tv != null)
                                address_tv.setText(address.getAddress().getAddress() + "  " + address.getAddress().getDetaileaddress());
                            setAddress = address.getAddress();
                        }
                    }
                }
                break;
            case 1://增加
                this.address.add(address.getAddress());
                if (address_tv != null)
                    address_tv.setText(address.getAddress().getAddress() + "  " + address.getAddress().getDetaileaddress());
                setAddress = address.getAddress();
                break;
            case 2://减少
                this.address.remove(address.getAddress());
                if (address.getAddress().getId() == setAddress.getId()) {
                    setAddress = null;
                    if (address_tv != null) address_tv.setText("请重新选择地址");
                }
                break;
        }
    }

    //监听订单变化
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OrdersChanged(Orde orde) {
        for (int i = 0; i < all_orders.size(); i++) {
            if(all_orders.get(i).getId() == orde.getId()){
                all_orders.get(i).setStatusid(orde.getStatusid());
                all_orders.get(i).setLevel(orde.getLevel());
            }
        }
    }

}