package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.responseBean.Result;

public class JsonUtils {
    public static <T> Result<T> getResult(JSONObject json) {
        Result<T> result = new Result<T>();
        result.setCode(json.getIntValue("code"));
        result.setMsg(json.getString("msg"));
        if(json.get("data")!=null){
//            result.setData(json.getObject("data",T.class));
        }
        return result;
    }

}

//{
//        "code": 2,
//        "msg": "用户已在其他设备登录",
//        "data": null
//        }