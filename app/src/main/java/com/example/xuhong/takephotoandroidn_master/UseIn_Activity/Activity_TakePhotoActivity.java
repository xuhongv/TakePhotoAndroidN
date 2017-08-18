package com.example.xuhong.takephotoandroidn_master.UseIn_Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                takePictureManager.setTailor(1, 1, 350, 350);
                takePictureManager.startTakeWayByCarema();
                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                    @Override
                    public void successful(boolean isTailor, File outFile, Uri filePath) {
                        Picasso.with(Activity_TakePhotoActivity.this).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
                    }

                    @Override
                    public void failed(int errorCode, List<String> deniedPermissions) {

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
                        Picasso.with(Activity_TakePhotoActivity.this).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
                    }

                    @Override
                    public void failed(int errorCode, List<String> deniedPermissions) {

                    }

                });

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
