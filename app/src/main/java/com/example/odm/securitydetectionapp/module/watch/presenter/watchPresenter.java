package com.example.odm.securitydetectionapp.module.watch.presenter;

import com.example.odm.securitydetectionapp.base.presenter.BasePresenter;
import com.example.odm.securitydetectionapp.module.watch.contract.watchContract;
import com.example.odm.securitydetectionapp.module.watch.model.watchModel;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.module.watch.ui.watchFragment;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.orhanobut.logger.Logger;

/**
 * 监控页面 P 层
 *
 * @author: ODM
 * @date: 2019/7/25
 */
public class watchPresenter extends BasePresenter<watchContract.View> implements watchContract.Presenter {
    private watchModel mWatchModel;

    public watchPresenter(watchFragment view) {
        super(view);
        mWatchModel = new watchModel();
    }


    @Override
    public void checkCapInfo(capInfoAdapter adapter) {

        mWatchModel.addCapInfoList(adapter);
    }

    @Override
    public void switchWebSocket(String newUrl ,capInfoAdapter adapter) {
        mWatchModel.switchWebSocket(newUrl , adapter);
    }

    @Override
    public void sendCallBack(String msg) {
        mWatchModel.sendCallBack(msg);
    }

    @Override
    public void handledAbnormalModule(int position ,capInfoAdapter adapter) {
        mWatchModel.handledAbnormalModule(position , adapter);
    }

    @Override
    public void handleModuleOffline(capInfoAdapter adapter) {
        mWatchModel.handleModuleOffline(adapter);
    }
}
