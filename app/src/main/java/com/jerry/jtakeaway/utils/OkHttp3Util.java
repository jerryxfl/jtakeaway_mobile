package com.jerry.jtakeaway.utils;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class OkHttp3Util {

    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void GET(String url, Context context, Callback callback) {
        String jwt = MMkvUtil.getInstance(context, "jwts").decodeString("jwt");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .callTimeout(5*1000, TimeUnit.MILLISECONDS)
                .build();
        Request request = null;
        if (jwt == null || "".equals(jwt)) {
            System.out.println("未使用jwt");
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            System.out.println("使用jwt:"+jwt);
            request = new Request.Builder()
                    .url(url)
                    .addHeader("jwt", jwt)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    public static void POST(String url, Context context, JSONObject json, Callback callback) {
        String jwt = MMkvUtil.getInstance(context, "jwts").decodeString("jwt");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .callTimeout(5*1000, TimeUnit.MILLISECONDS)
                .build();
        Request request = null;
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        if (jwt == null || "".equals(jwt)) {
            System.out.println("未使用jwt");
            request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
        } else {
            System.out.println("使用jwt:"+jwt);
            request = new Request.Builder()
                    .url(url)
                    .addHeader("jwt", jwt)
                    .post(formBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    public static void UPLOAD(String url, Context context, List<String> fileNames, Callback callback) {
        String jwt = MMkvUtil.getInstance(context, "jwts").decodeString("jwt");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .callTimeout(5*1000, TimeUnit.MILLISECONDS)
                .build();

        Request request = null;
        //创建File
        RequestBody requestBody = getRequestBody(fileNames);

        if (jwt == null || "".equals(jwt)) {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("jwt", jwt)
                    .post(requestBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            builder.addFormDataPart( //给Builder添加上传的文件
                    "file",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse("application/octet-stream"), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }

    public static void DOWNLOAD(String url, Context context, String file_name, Callback callback) {
        String jwt = MMkvUtil.getInstance(context, "jwts").decodeString("jwt");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .callTimeout(5*1000, TimeUnit.MILLISECONDS)
                .build();
        Request request = null;
        if (jwt == null || "".equals(jwt)) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("jwt", jwt)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String mSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    File dest = new File(mSDCardPath, file_name);
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
                }
                callback.onResponse(call, response);
            }
        });
    }


}
