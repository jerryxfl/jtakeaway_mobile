package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.bean.responseBean.Result1;

public class JsonUtils {
    public static Result2 getResult2(JSONObject json) {
        Result2 result = new Result2();
        result.setMsg(json.getString("msg"));
        result.setCode(json.getIntValue("code"));
        if(json.get("data")!=null){
            result.setData(json.getJSONArray("data"));
        }
        return result;
    }

    public static Result1 getResult1(JSONObject json) {
        Result1 result = new Result1();
        result.setMsg(json.getString("msg"));
        result.setCode(json.getIntValue("code"));
        if(json.get("data")!=null){
            result.setData(json.getJSONObject("data"));
        }
        return result;
    }
}

//{
//        "code": 2,
//        "msg": "用户已在其他设备登录",
//        "data": null
//        }