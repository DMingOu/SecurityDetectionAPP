package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.module.map_location.presenter.locationPresenter;
import com.tuyenmonkey.mkloader.MKLoader;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class locationFragment<P extends IBasePresenter> extends BaseView<locationPresenter> implements locationContract.View {


    @BindView(R.id.loading)
    MKLoader loadingbar;

    @BindView(R.id.rmv_locate)
    RadarMapView rmv_Locate;
    @BindView(R.id.tb_location)
    CommonTitleBar tbLocation;
    @BindView(R.id.iv_background_location)
    ImageView iv_bg;


    @Override
    public locationPresenter onBindPresenter() {
        return new locationPresenter();
    }

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    protected void lazyLoadData() {
        //每次重新进入此页面才加载的内容
//        rmv_Locate.setDirection(RadarMapView.ANTI_CLOCK_WISE);
//        try {
//            rmv_Locate.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.img_test_20160210_153031, null);
                iv_bg.setBackground(drawable);
            }
        }, 2500);   //5秒
         }


}
