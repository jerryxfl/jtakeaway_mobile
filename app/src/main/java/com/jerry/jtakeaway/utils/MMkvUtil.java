package com.jerry.jtakeaway.utils;

import android.content.Context;

import com.tencent.mmkv.MMKV;

import java.util.Set;


/**
 * 代替sharepermance 单列
 *
 */
public class MMkvUtil {
    private static MMkvUtil instance;
    private static MMKV mmkv;
    public static synchronized MMkvUtil getInstance(Context context,String name){
        if(instance==null){
            instance = new MMkvUtil();
            MMKV.initialize(context);
        }
        mmkv=MMKV.mmkvWithID(name);
        return instance;
    }

    public static synchronized MMkvUtil getInstance(String name){
        if(instance==null){
            instance = new MMkvUtil();
        }
        mmkv=MMKV.mmkvWithID(name);
        return instance;
    }

    public static synchronized MMkvUtil getInstance(){
        if(instance==null){
            instance = new MMkvUtil();
        }
        return instance;
    }

    public MMKV getMmkv(){
        return mmkv;
    }

    public void encode(String key,String value){
        mmkv.encode(key,value);
    }

    public void encode(String key,boolean value){
        mmkv.encode(key,value);
    }

    public void encode(String key,int value){
        mmkv.encode(key,value);
    }

    public void encode(String key,float value){
        mmkv.encode(key,value);
    }

    public void encode(String key,double value){
        mmkv.encode(key,value);
    }


    public void encode(String key,Set<String> value){
        mmkv.encode(key,value);
    }

    public void encode(String key,long value){
        mmkv.encode(key,value);
    }

    public void encode(String key,byte[] value){
        mmkv.encode(key,value);
    }


    public String decodeString(String key){
        return mmkv.decodeString(key);
    }

    public boolean decodeBool(String key){
        return mmkv.decodeBool(key);
    }

    public byte[] decodeBytes(String key){
       return mmkv.decodeBytes(key);
    }

    public Double decodeDouble(String key){
        return mmkv.decodeDouble(key);
    }


    public Float decodeFloat(String key){
        return mmkv.decodeFloat(key);
    }

    public int decodeInt(String key){
        return mmkv.decodeInt(key);
    }

    public Long decodeLong(String key){
        return mmkv.decodeLong(key);
    }

    public Set<String> decodeStringSet(String key){
        return mmkv.decodeStringSet(key);
    }
}
