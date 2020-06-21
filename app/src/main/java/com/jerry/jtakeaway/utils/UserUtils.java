package com.jerry.jtakeaway.utils;

import com.jerry.jtakeaway.bean.responseBean.ResponseUser;

public class UserUtils {
    private static UserUtils instance;
    private static ResponseUser user;


    public static synchronized UserUtils getInstance() {
        if (instance == null)instance = new UserUtils();
        return instance;
    }


    public void setUser(ResponseUser user) {
        UserUtils.user = user;
        System.out.println("登录成功返回值"+user.toString());
    }

    public ResponseUser getUser() {
        return user;
    }


    public <T> T getUserDetails(Class<T> clazz) {
        return (T) GsonUtil.gsonToBean(user.getUserdetails(), clazz);
    }




}
