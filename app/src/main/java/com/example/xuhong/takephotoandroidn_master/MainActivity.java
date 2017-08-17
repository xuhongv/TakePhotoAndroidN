package com.example.xuhong.takephotoandroidn_master;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.xuhong.takephotoandroidn_master.UseIn_Activity.Activity_TakePhotoActivity;
import com.example.xuhong.takephotoandroidn_master.UseIn_Fragment.Fragment_TakePhotoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Fragment使用
    public void btUseInFragment(View view) {
        startActivity(new Intent(this, Fragment_TakePhotoActivity.class));
    }

    //Activity使用
    public void btUseInActivity(View view) {
        startActivity(new Intent(this, Activity_TakePhotoActivity.class));
    }
}
