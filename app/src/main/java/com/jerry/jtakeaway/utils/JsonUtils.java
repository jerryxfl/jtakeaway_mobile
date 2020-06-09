package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.responseBean.Result;

public class JsonUtils {
    public static  Result getResult(JSONObject json) {
        Result result = new Result();
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