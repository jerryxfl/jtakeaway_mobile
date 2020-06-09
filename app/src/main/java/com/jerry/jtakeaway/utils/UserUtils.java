package com.jerry.jtakeaway.utils;

import com.jerry.jtakeaway.bean.User;

public class UserUtils {
    private static UserUtils instance;
    private static User user;


    public static synchronized UserUtils getInstance() {
        if (instance == null)instance = new UserUtils();
        return instance;
    }


    public void setUser(User user) {
        UserUtils.user = user;
    }

    public User getUser() {
        return user;
    }





}
