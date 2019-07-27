package com.example.odm.securitydetectionapp.module.map_location.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.module.map_location.presenter.locationPresenter;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class locationFragment<P extends IBasePresenter> extends BaseView<locationPresenter> implements locationContract.View {

    @BindView(R.id.multiple_status_view_location)
    MultipleStatusView multipleStatusViewLocation;

    @Override
    public locationPresenter onBindPresenter() {
        return new locationPresenter();
    }
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        unbinder = ButterKnife.bind(this , view);
        multipleStatusViewLocation.showLoading();
        return view;
    }
}
