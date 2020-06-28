package com.jerry.jtakeaway.ui.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.Settings;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.base.BaseViewHolder;
import com.jerry.jtakeaway.custom.JAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.switch_camera)
    ImageView switch_camera;

    @BindView(R.id.shooting)
    ImageView shooting;


    @BindView(R.id.camera_mode_recyclerview)
    RecyclerView camera_mode_recyclerview;

    @BindView(R.id.SurfaceView)
    android.view.SurfaceView SurfaceView;


    private JAdapter<String> modeJAdapter;

    private boolean canCamera = false;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback = (data, camera) -> {
        File tempFile = new File("/sdcard/"+System.currentTimeMillis()+".png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
            Intent intent = new Intent(CameraActivity.this, AlbumActivity.class);
            intent.putExtra("IMG", tempFile.getAbsolutePath());
            startActivity(intent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };


    @Override
    public int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    public void InitView() {
        if (!checkCameraHardware(this)) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("你的设备不支持相机")
                    .setConfirmText("退出")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    })
                    .show();
        } else {
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
            CameraActivityPermissionsDispatcher.initWithPermissionCheck(this);
        }
    }
    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void init(){
        mHolder = SurfaceView.getHolder();
        mHolder.addCallback(this);
        mCamera = getCamera();
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size size = getBestPreviewSize(SurfaceView.getWidth(), SurfaceView.getHeight());
        parameters.setPreviewSize(size.width, size.height);
    }

    private Camera getCamera() {
        Camera camera;
        try{
            camera = Camera.open();

        }catch(Exception e){
            camera = null;
        }
        return camera;
    }


    private void release(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void Capture(){
        canCamera = false;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(SurfaceView.getWidth(),SurfaceView.getHeight());
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus((success, camera) -> {
            if(success){
                mCamera.takePicture(null, null,mPictureCallback);
            }else{
                canCamera = true;
            }
        });
    }

    private void startPreview(Camera camera,SurfaceHolder surfaceHolder){
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            canCamera = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result = null;
        final Camera.Parameters p = mCamera.getParameters();
        //特别注意此处需要规定rate的比是大的比小的，不然有可能出现rate = height/width，但是后面遍历的时候，current_rate = width/height,所以我们限定都为大的比小的。
        float rate = (float) Math.max(width, height)/ (float)Math.min(width, height);
        float tmp_diff;
        float min_diff = -1f;
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            float current_rate = (float) Math.max(size.width, size.height)/ (float)Math.min(size.width, size.height);
            tmp_diff = Math.abs(current_rate-rate);
            if( min_diff < 0){
                min_diff = tmp_diff ;
                result = size;
            }
            if( tmp_diff < min_diff ){
                min_diff = tmp_diff ;
                result = size;
            }
        }
        return result;
    }



    @Override
    public void InitData() {
        List<String> modes = new ArrayList<String>();
        modes.add("PHOTO");
        modeJAdapter.adapter.setFooter(modes);

    }

    @Override
    public void InitListener() {
        switch_camera.setOnClickListener(v -> {

        });
        shooting.setOnClickListener(v -> {
            if(canCamera)Capture();
        });
        SurfaceView.setOnClickListener(v -> {
            mCamera.autoFocus((success, camera) -> { });
        });
    }

    @Override
    public void destroy() {
        release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null){
            mCamera = getCamera();
            if(mHolder!= null){
                startPreview(mCamera,mHolder);
            }
        }else{
            if(mHolder!= null){
                startPreview(mCamera,mHolder);
            }
        }
    }

    //检测是否有相机
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
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

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDenied() {
        Toast.makeText(this, "无法获得权限", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera,holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();;
        startPreview(mCamera,holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        release();
    }
}