package com.example.odm.securitydetectionapp.module.history.presenter;

import com.example.odm.securitydetectionapp.base.presenter.BasePresenter;
import com.example.odm.securitydetectionapp.base.view.BaseView;
import com.example.odm.securitydetectionapp.module.history.contract.historyContract;
import com.example.odm.securitydetectionapp.module.history.model.historyModel;
import com.example.odm.securitydetectionapp.module.history.ui.historyAdapter;
import com.example.odm.securitydetectionapp.module.history.ui.historyFragment;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class historyPresenter extends BasePresenter<historyContract.View>implements historyContract.Presenter {

    private historyModel  mHistoryModel;

    public historyPresenter(historyFragment view) {
        super(view);
        mHistoryModel = new historyModel();
    }

     @Override
    public void loadHistoryList(historyAdapter adapter) {
        mHistoryModel.loadHistoryList(adapter);
    }
}
