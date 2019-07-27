package com.example.odm.securitydetectionapp.module;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.module.history.ui.historyFragment;
import com.example.odm.securitydetectionapp.module.home.ui.homeFragment;
import com.example.odm.securitydetectionapp.module.map_location.ui.locationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private  List<Fragment> fragmentList;
    private  VpAdapter vpAdapter;

    @BindView(R.id.bottom_navigationview)
    BottomNavigationViewEx view_bottomNavigation;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.vp)
    ViewPager view_vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initEvent();
        view_vp.setCurrentItem(1);
    }

    public void initViews(){
        //此处改变底部导航的动画效果
    }

    // 添加页面,初始化ViewPager
    private void  initData(){
        fragmentList = new ArrayList<Fragment>(5);
        fragmentList.add(new historyFragment<>());
        fragmentList.add(new homeFragment<>());
        fragmentList.add(new locationFragment<>());
        vpAdapter = new VpAdapter(getSupportFragmentManager() , fragmentList);
        view_vp.setAdapter(vpAdapter);
        view_bottomNavigation.setupWithViewPager(view_vp);
    }

    /**
     * 监听底部导航栏的点击事件
     */
    private void initEvent() {
        // set listener to do something then item selected
        view_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // return false 则取消选择
                return true;
            }
        });
    }
    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
