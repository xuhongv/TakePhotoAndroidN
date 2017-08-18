package com.example.xuhong.takephotoandroidn_master.UseIn_Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuhong.takephotoandroidn_master.R;
import com.example.xuhong.takephotoandroidn_master.TakePictureManager;
import com.example.xuhong.takephotoandroidn_master.UseIn_Activity.Activity_TakePhotoActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    private TextView tvShow;

    private ImageView ivShow;

    private TakePictureManager takePictureManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Button btCamera = view.findViewById(R.id.btCamera);
        btCamera.setOnClickListener(this);
        Button btAlbum = view.findViewById(R.id.btAlbum);
        btAlbum.setOnClickListener(this);
        tvShow = view.findViewById(R.id.tvShow);
        ivShow = view.findViewById(R.id.ivShow);
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
                        Picasso.with(getActivity()).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
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
                        Picasso.with(getActivity()).load(outFile).error(R.mipmap.ic_launcher).into(ivShow);
                    }

                    @Override
                    public void failed(int errorCode, List<String> deniedPermissions) {

                    }

                });
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
