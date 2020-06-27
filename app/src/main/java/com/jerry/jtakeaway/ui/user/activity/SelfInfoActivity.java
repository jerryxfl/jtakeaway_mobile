package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.application.JApplication;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.JUrl;
import com.jerry.jtakeaway.bean.responseBean.Result2;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.custom.JBottomDialog;
import com.jerry.jtakeaway.custom.JCenterDialog;
import com.jerry.jtakeaway.ui.generalActivity.AddressManagerActivity;
import com.jerry.jtakeaway.ui.generalActivity.EditAddressActivity;
import com.jerry.jtakeaway.utils.GsonUtil;
import com.jerry.jtakeaway.utils.JsonUtils;
import com.jerry.jtakeaway.utils.OkHttp3Util;
import com.jerry.jtakeaway.utils.PixAndDpUtil;
import com.jerry.jtakeaway.utils.UserUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.lcw.library.imagepicker.utils.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelfInfoActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;

    @BindView(R.id.headImgWrapper)
    RelativeLayout headImgWrapper;

    @BindView(R.id.headImg)
    CircleImageView headImg;

    @BindView(R.id.userNickNameWrapper)
    RelativeLayout userNickNameWrapper;

    @BindView(R.id.userNickName)
    TextView userNickName;

    @BindView(R.id.addressWrapper)
    RelativeLayout addressWrapper;

    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    private JCenterDialog jCenterDialog;
    private JBottomDialog albumOrCameraDialog;
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;

    @Override
    public int getLayout() {
        return R.layout.activity_self_info;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        SignEventBus();
    }

    @Override
    public void InitData() {
        setPageDatas();
    }

    private void setPageDatas() {
        if (UserUtils.getInstance().getUser().getUseradvatar() != null) {
            Glide.with(this)
                    .load(UserUtils.getInstance().getUser().getUseradvatar())
                    .into(headImg);
        }
        if (UserUtils.getInstance().getUser().getUsernickname() != null) {
            userNickName.setText(UserUtils.getInstance().getUser().getUsernickname());
        }
    }

    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        addressWrapper.setOnClickListener(v -> {
            getAddress();
        });
        headImg.setOnClickListener(v -> {
            Intent intent = new Intent(SelfInfoActivity.this, ImgActivity.class);
            List<String> list = new ArrayList<String>();
            list.add(UserUtils.getInstance().getUser().getUseradvatar());
            intent.putExtra("IMGS", (Serializable) list);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SelfInfoActivity.this, v, "img");
            startActivity(intent, optionsCompat.toBundle());
        });
        headImgWrapper.setOnClickListener(v -> {
            if (albumOrCameraDialog == null) {
                albumOrCameraDialog = new JBottomDialog(SelfInfoActivity.this, R.layout.albumorcamera_dialog, view -> {
                    RelativeLayout album = view.findViewById(R.id.album);
                    RelativeLayout camera = view.findViewById(R.id.camera);
                    RelativeLayout cancel = view.findViewById(R.id.cancel);
                    album.setOnClickListener(v1 -> {
                        //相册
                        albumOrCameraDialog.dismiss();
                        ImagePicker.getInstance()
                                .setTitle("选择头像")//设置标题
                                .showCamera(false)//设置是否显示拍照按钮
                                .showImage(true)//设置是否展示图片
                                .showVideo(false)//设置是否展示视频
                                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                                .start(SelfInfoActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                    });
                    camera.setOnClickListener(v1 -> {
                        //相机
                        albumOrCameraDialog.dismiss();
                        openCamera();
                    });
                    cancel.setOnClickListener(v1 -> {
                        albumOrCameraDialog.dismiss();
                    });
                });
                albumOrCameraDialog.show();
            } else {
                albumOrCameraDialog.show();
            }
        });
        userNickNameWrapper.setOnClickListener(v -> {
            Intent intent = new Intent(SelfInfoActivity.this, ChangeNewNickNameActivity.class);
            intent.putExtra("NICKNAME", UserUtils.getInstance().getUser().getUsernickname());
            startActivity(intent);
        });
    }

    void openCamera() {
        startActivity(new Intent(SelfInfoActivity.this, CameraActivity.class));
    }


    public static class GlideLoader implements ImageLoader {

        private RequestOptions mOptions = new RequestOptions()
                .centerCrop()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.mipmap.icon_image_default)
                .error(R.drawable.icon_image_error);

        private RequestOptions mPreOptions = new RequestOptions()
                .skipMemoryCache(true)
                .error(R.drawable.icon_image_error);

        @Override
        public void loadImage(ImageView imageView, String imagePath) {
            //小图加载
            Glide.with(imageView.getContext()).load(imagePath).apply(mOptions).into(imageView);
        }

        @Override
        public void loadPreImage(ImageView imageView, String imagePath) {
            //大图加载
            Glide.with(imageView.getContext()).load(imagePath).apply(mPreOptions).into(imageView);

        }

        @Override
        public void clearMemoryCache() {
            //清理缓存
            Glide.get(JApplication.getContext()).clearMemory();
        }
    }


    private void getAddress() {
        if (jCenterDialog == null)
            jCenterDialog = new JCenterDialog(this, R.layout.loading_dialog);
        jCenterDialog.show();
        OkHttp3Util.GET(JUrl.address, this, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    jCenterDialog.dismiss();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                Result2 result = JsonUtils.getResult2(jsonObject);
                if (result.getCode() == 10000) {
                    List<Address> address = new ArrayList<Address>(GsonUtil.jsonToList(result.getData().toString(), Address.class));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        if (address.isEmpty()) {
                            startActivity(new Intent(SelfInfoActivity.this, EditAddressActivity.class));
                        } else {
                            Intent intent = new Intent(SelfInfoActivity.this, AddressManagerActivity.class);
                            intent.putExtra("addres", (Serializable) address);
                            startActivity(intent);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        jCenterDialog.dismiss();
                        Toast.makeText(SelfInfoActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }





    @Override
    public void destroy() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            List<String> mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            for (int i = 0; i < mImagePaths.size(); i++) {
                Intent intent = new Intent(SelfInfoActivity.this, AlbumActivity.class);
                intent.putExtra("IMG", mImagePaths.get(i));
                startActivity(intent);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChange(String tag) {
        if (tag.equals("userChangeSuccess")) {
            setPageDatas();
        }
    }
}