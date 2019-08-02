package com.example.odm.securitydetectionapp.module.watch.presenter;

import com.example.odm.securitydetectionapp.base.presenter.BasePresenter;
import com.example.odm.securitydetectionapp.module.watch.contract.watchContract;
import com.example.odm.securitydetectionapp.module.watch.model.watchModel;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.module.watch.ui.watchFragment;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.orhanobut.logger.Logger;

/**
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
    public void checkCapInfo(String data ,capInfoAdapter adapter) {
        //初步判断数据是否为关于子模块的信息
        if(data .startsWith("{")) {
            mWatchModel.addCapInfoList(data ,adapter);
        } else if(data .startsWith("嵌")){
            Logger.d("嵌入式设备下线了" + TimeUtil.showCurrentTime(System.currentTimeMillis()));
            mWatchModel.clearAllCapInfo(adapter);
        }
    }

    @Override
    public void switchWebSocket(String newUrl ,capInfoAdapter adapter) {
        mWatchModel.switchWebSocket(newUrl , adapter);
    }

    @Override
    public void sendCallBack(String msg) {
        mWatchModel.sendCallBack(msg);
    }

}
