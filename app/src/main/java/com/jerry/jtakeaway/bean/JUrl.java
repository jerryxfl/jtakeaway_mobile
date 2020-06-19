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
    public static final String g_menus(int shopid,int size) {
        return host+"G/g_shops_menus?shopid="+shopid+"&size="+size;
    }








    //post
    public static final String login = host+"authen/jwtLogin";
    public static final String c_address = host+"U/c_address";
    public static final String a_address = host+"U/a_address";



}
