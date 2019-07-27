package com.example.odm.securitydetectionapp.module.history.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.module.history.contract.historyContract;
import com.example.odm.securitydetectionapp.module.history.model.historyModel;
import com.example.odm.securitydetectionapp.module.history.presenter.historyPresenter;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class historyFragment<P extends IBasePresenter> extends BaseView<historyPresenter> implements historyContract.View {

    @BindView(R.id.multiple_status_view_history)
    MultipleStatusView multipleStatusViewHistory;
    @BindView(R.id.tv_test)
    TextView tv_Test;
    private historyModel mHistoryModel;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this ,view);
        multipleStatusViewHistory.showEmpty();
        return view;
    }

    @Override
    public historyPresenter onBindPresenter() {
        return new historyPresenter();
    }
}
