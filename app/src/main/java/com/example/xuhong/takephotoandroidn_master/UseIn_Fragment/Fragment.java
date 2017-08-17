package com.example.xuhong.takephotoandroidn_master.UseIn_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuhong.takephotoandroidn_master.R;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class Fragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    private TextView tvShow;

    private ImageView ivShow;

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
                break;
            case R.id.btAlbum:
                break;
        }
    }
}
