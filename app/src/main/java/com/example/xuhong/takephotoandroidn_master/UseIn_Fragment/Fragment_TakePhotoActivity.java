package com.example.xuhong.takephotoandroidn_master.UseIn_Fragment;

import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;

import com.example.xuhong.takephotoandroidn_master.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_TakePhotoActivity extends AppCompatActivity {

    //Fragment
    private List<android.support.v4.app.Fragment> mFragment;
    private ViewPager viewPager;
    private List<String> mTitle;


    //TabLayout
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment__take_photo);
        getSupportActionBar().setElevation(0);

        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);

        mFragment = new ArrayList<>();
        mFragment.add(new Fragment());
        mFragment.add(new Fragment());


        mTitle = new ArrayList<>();
        mTitle.add("我是第一个碎片");
        mTitle.add("我是第二个碎片");

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        mTabLayout.setupWithViewPager(viewPager);

    }
}
