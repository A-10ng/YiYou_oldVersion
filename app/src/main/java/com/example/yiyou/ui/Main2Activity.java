package com.example.yiyou.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.fragment.NoScrollViewPager;
import com.example.yiyou.fragment.PersonalFragment;
import com.example.yiyou.fragment.StuFunctionFragment;
import com.example.yiyou.fragment.StuHomeFragment;

import java.util.ArrayList;



public class Main2Activity extends AppCompatActivity {

    private NoScrollViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private final Handler handler1 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mViewPager = (NoScrollViewPager) findViewById(R.id.mViewPager);
        mViewPager.setNoScroll(true);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.mBottom);
        mBottomNavigationView.setItemIconTintList(null);

        final Runnable mRunnable1 = new Runnable() {
            @Override
            public void run() {
                if (Data.getTeamName() == "none") {
                    Toast.makeText(Main2Activity.this, "请先去<首页>选择一个队伍", Toast.LENGTH_SHORT).show();
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(1);
                }
            }
        };

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.btm_function:
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler1.post(mRunnable1);
                            }
                        });
                        thread.start();
                        break;
                    case R.id.btm_personal:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //在ArrayList中装入底部导航栏的三个fragment
        final ArrayList<Fragment> fragmentList1 = new ArrayList<>(3);
        fragmentList1.add(new StuHomeFragment());//首页
        fragmentList1.add(new StuFunctionFragment());//功能
        fragmentList1.add(new PersonalFragment());//个人

        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return fragmentList1.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList1.size();
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);
    }
}
