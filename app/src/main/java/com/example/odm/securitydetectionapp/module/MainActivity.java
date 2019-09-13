package com.example.odm.securitydetectionapp.module;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.PageStatusManager;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.example.odm.securitydetectionapp.module.history.ui.historyFragment;
import com.example.odm.securitydetectionapp.module.watch.ui.watchFragment;
import com.example.odm.securitydetectionapp.module.map_location.ui.locationFragment;
import com.example.odm.securitydetectionapp.util.ToastUtil;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.bottom_Tab)
    AlphaTabsIndicator bottomTab;
    private List<Fragment> fragmentList;
    private VpAdapter vpAdapter;
    long exitTime = 0;

    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.vp)
    ViewPager view_vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPager();
        view_vp.setCurrentItem(1);
    }


    // 添加页面,初始化ViewPager
    private void initViewPager() {
        fragmentList = new ArrayList<Fragment>(3);
        fragmentList.add(new historyFragment<>());
        fragmentList.add(new watchFragment<>());
        fragmentList.add(new locationFragment<>());
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragmentList);
        view_vp.setAdapter(vpAdapter);
        bottomTab.setViewPager(view_vp);
        //viewpager监听页面的跳转
        view_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    PageStatusManager.setPageStatus(PageStatusManager.PAGE_HISTORY_CURENT);

                } else if(position == 1) {
                    PageStatusManager.setPageStatus(PageStatusManager.PAGE_WATCH_CURRENT);
                } else if(position == 2) {
                    PageStatusManager.setPageStatus(PageStatusManager.PAGE_LOCATION_CURRENT);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.fragmentList = data;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 控制返回键连续点击才两次
     * 退出若两次点击返回键时间小于2秒就退出
     */
    private void exit() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShortToastBottom("再按一次返回键退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
