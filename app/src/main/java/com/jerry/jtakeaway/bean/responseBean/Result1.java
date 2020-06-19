package com.jerry.jtakeaway.bean.responseBean;


import com.alibaba.fastjson.JSONObject;

/**
 * @version V1.0
 * @Package com.ss.jwt.R
 * @author: Liu
 * @Date: 10:21
 */
public class Result1 {
    /*返回体*/
    private Integer code;
    private String msg;
    private JSONObject data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
