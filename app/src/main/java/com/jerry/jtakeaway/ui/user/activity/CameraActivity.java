package com.jerry.jtakeaway.ui.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CameraActivity extends BaseActivity  {
    @BindView(R.id.switch_camera)
    ImageView switch_camera;

    @BindView(R.id.shooting)
    ImageView shooting;

    @BindView(R.id.album)
    ImageView album;

    @BindView(R.id.camera_mode_recyclerview)
    RecyclerView camera_mode_recyclerview;

    @BindView(R.id.preview)
    PreviewView preview;

    private JAdapter<String> modeJAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    public void InitView() {
        CameraActivityPermissionsDispatcher.InitWithPermissionCheck(this);
        Init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        camera_mode_recyclerview.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(camera_mode_recyclerview);
        modeJAdapter = new JAdapter<>(this, camera_mode_recyclerview, new int[]{R.layout.camera_mode_item}, new JAdapter.adapterListener<String>() {
            @Override
            public void setItems(BaseViewHolder holder, int position, List<String> datas) {
                TextView modeName = holder.getView(R.id.modeName);
                modeName.setText(datas.get(position));
            }

            @Override
            public void upDateItem(BaseViewHolder holder, int position, List<Object> payloads, List<String> datas) {

            }

            @Override
            public int getViewType(List<String> datas, int position) {
                return 0;
            }
        });
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void Init() {
        if (!checkCameraHardware(this)) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("你的设备不支持相机")
                    .setConfirmText("退出")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    })
                    .show();
        }
    }


    private void startCamera() {


    }



    //检测是否有相机
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showWhy(final PermissionRequest request) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("我们需要此权限才能打开相机拍照?")
                .setConfirmText("好")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    request.proceed();
                })
                .setCancelText("拒绝")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    request.cancel();
                    finish();
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDenied() {
        Toast.makeText(this, "无法获得权限", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskAgain() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("相机权限被拒绝,为了不影响你的使用请前往设置开启权限?")
                .setConfirmText("好")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setCancelText("拒绝")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    finish();
                })
                .show();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        CameraActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void InitData() {
        List<String> modes = new ArrayList<String>();
        modes.add("PHOTO");
        modes.add("VIDEO");
        modes.add("VLOG");
        modeJAdapter.adapter.setFooter(modes);

    }

    @Override
    public void InitListener() {
        switch_camera.setOnClickListener(v -> {

        });
        shooting.setOnClickListener(v -> {

        });
        album.setOnClickListener(v -> {

        });
    }

    @Override
    public void destroy() {

    }



    @Override
    protected void onResume() {
        super.onResume();

    }




}