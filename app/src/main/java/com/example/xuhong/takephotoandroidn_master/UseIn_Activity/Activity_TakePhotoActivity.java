package com.example.xuhong.takephotoandroidn_master.UseIn_Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuhong.takephotoandroidn_master.R;
import com.example.xuhong.takephotoandroidn_master.TakePictureManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class Activity_TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivShow;

    private TextView tvShow;

    private TakePictureManager takePictureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__take_photo);
        inintView();
    }

    private void inintView() {
        ivShow = (ImageView) findViewById(R.id.ivShow);
        Button btCamera = (Button) findViewById(R.id.btCamera);
        btCamera.setOnClickListener(this);
        Button btAlbum = (Button) findViewById(R.id.btAlbum);
        btAlbum.setOnClickListener(this);
        tvShow = (TextView) findViewById(R.id.tvShow);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCamera:

                takePictureManager = new TakePictureManager(this);
                //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
                takePictureManager.setTailor(1, 3, 350, 350);
                //拍照方式
                takePictureManager.startTakeWayByCarema();
                //回调
                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                    //成功拿到图片,isTailor 是否裁剪？ ,outFile 拿到的文件 ,filePath拿到的URl
                    @Override
                    public void successful(boolean isTailor, File outFile, Uri filePath) {
                        tvShow.setText(filePath.getPath());
                        Picasso.with(Activity_TakePhotoActivity.this).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
                    }

                    //失败回调
                    @Override
                    public void failed(int errorCode, List<String> deniedPermissions) {
                        Log.e("==w",deniedPermissions.toString());
                    }
                });
                break;
            case R.id.btAlbum:

                takePictureManager = new TakePictureManager(this);
                takePictureManager.setTailor(1, 1, 350, 350);
                takePictureManager.startTakeWayByAlbum();
                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                    @Override
                    public void successful(boolean isTailor, File outFile, Uri filePath) {
                        tvShow.setText(filePath.getPath());
                        Picasso.with(Activity_TakePhotoActivity.this).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
                    }

                    @Override
                    public void failed(int errorCode, List<String> deniedPermissions) {

                    }

                });

                break;
        }
    }


    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    //onRequestPermissionsResult()方法权限回调绑定到对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
