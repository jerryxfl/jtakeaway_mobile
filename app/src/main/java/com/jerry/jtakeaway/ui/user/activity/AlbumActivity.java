package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result1;
import com.jerry.jtakeaway.custom.ClipImageView;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlbumActivity extends BaseActivity {
    @BindView(R.id.clip_image_view)
    ClipImageView clip_image_view;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.ok)
    Button ok;

    private String filePath;

    private String img;
    private JCenterDialog jCenterDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_album;
    }

    @Override
    public void InitView() {

    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        img = intent.getStringExtra("IMG");
        System.out.println("图片路径"+img);
        clip_image_view.setImageBitmap(BitmapFactory.decodeFile(img));
    }

    @Override
    public void InitListener() {
        cancel.setOnClickListener(v -> finish());
        ok.setOnClickListener(v ->{
            filePath = writeFileByBitmap(clip_image_view.clip());
            if(filePath != null){
                upLoadImage(filePath);
            }
        });
    }

    private void upLoadImage(String path) {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        List<String> paths = new ArrayList<>();
        paths.add(path);
        OkHttp3Util.UPLOAD(JUrl.u_advater, this,paths, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result1 result = JsonUtils.getResult1(jsonObject);
                if(result.getCode() == 10000){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        File file = new File(filePath);
                        if(file.isFile()){
                            file.delete();
                        }

                        jCenterDialog.dismiss();
                        EventBus.getDefault().post("userChange");
                        Toast.makeText(AlbumActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(AlbumActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }


    public static String writeFileByBitmap(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();//手机设置的存储位置
        File file = new File(path);
        File imageFile = new File(file, System.currentTimeMillis() + ".png");

        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            imageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void destroy() {

    }
}

