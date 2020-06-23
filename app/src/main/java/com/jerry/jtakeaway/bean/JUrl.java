package com.jerry.jtakeaway.bean;

public class JUrl {
//    public static final String host = "http://192.168.0.104:8080/api-0.1/";
    public static final String host = "http://121.199.9.234:8080/api-0.1/";


    //get
    public static final String shop = host+"G/shop?id=";
    public static final String m_conpon = host+"N/m_coupon";
    public static final String u_conpons = host+"N/coupons";
    public static final String g_conpon = host+"N/c_coupon?conponid=";
    public static final String address = host+"U/g_address";
    public static final String hot_menus = host+"G/g_hot_menus";
    public static final String top_slides = host+"G/top_slides";
    public static final String broadcasts = host+"G/broadcasts";
    public static final String hot_shop_menu = host+"G/hot_shop_menu";
    public static final String all_orders = host+"N/all_orders";
    public static final String all_no_pay_orders = host+"N/all_no_pay_orders";
    public static final String user_info = host+"U/user_info";
    public static final String user_wallet_money = host+"U/user_wallet_money";
    public static final String jwtLogout = host+"authen/jwtLogout";
    public static final String o_wallet(String payPassword){
        return host+"U/o_wallet?payPassword="+payPassword;
    }
    public static final String pay_money(int orderid){
        return host+"N/pay_money?orderid="+orderid;
    };
    public static final String coupon_pay_money(int orderid,int conponid){
        return host+"N/coupon_pay_money?orderid="+orderid+"&couponid="+conponid;
    }
    public static final String shop_slide(int suserid){
        return host+"G/shop_slides?suserid="+suserid;
    }
    public static final String shop_menu(int suserid,int size){
        return host+"G/g_shops_menus?shopid="+suserid+"&size="+size;
    }
    public static final String g_menus(int shopid,int size) {
        return host+"G/g_shops_menus?shopid="+shopid+"&size="+size;
    }

    public static final String create_order(int suserid,int menuid,int size) {
        return host+"N/order?sId="+suserid+"&mId="+menuid+"&mSize="+size;
    }

    public static final String shop_can_use_coupon(int suserid) {
        return host+"N/shop_can_use_coupon?suserid="+suserid;
    }
    public static final String c_wallet_money(int money) {
        return host+"U/c_wallet_money?money="+money;
    }

    public static final String change_password(String oldPwd,String newPwd) {
        return host+"U/change_password?oldPassword="+oldPwd+"&newPassword="+newPwd;
    }

   public static final String change_email(String code,String newEmail,int tag) {
        return host+"U/change_email?code="+code+"&newEmail="+newEmail+"&tag="+tag;
    }







    //post
    public static final String login = host+"authen/jwtLogin";
    public static final String c_address = host+"U/c_address";
    public static final String a_address = host+"U/a_address";
    public static final String conpon_pay= host+"N/coupon_pay";
    public static final String pay= host+"N/pay";

    public static final String t_wallet_money= host + "U/t_wallet_money";
    public static final String d_address =host + "U/d_address";

}
