package com.example.odm.securitydetectionapp.module.home.presenter;

import com.example.odm.securitydetectionapp.base.presenter.BasePresenter;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;
import com.example.odm.securitydetectionapp.module.home.contract.homeContract;
import com.example.odm.securitydetectionapp.module.home.model.homeModel;
import com.example.odm.securitydetectionapp.module.home.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.module.home.ui.homeFragment;

import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class homePresenter extends BasePresenter<homeContract.View> implements homeContract.Presenter {
    private homeModel  mHomeModel;

    public homePresenter (homeFragment view) {
        super(view);
        mHomeModel = new homeModel();
    }


    @Override
    public void checkCapInfo(String data ,capInfoAdapter adapter) {
        //初步判断数据是否为关于子模块的信息
        if(data .startsWith("{")) {
            mHomeModel.addCapInfoList(data ,adapter);
        } else if(data .startsWith("嵌")){
            mHomeModel.clearAllCapInfo(adapter);
        }
    }



    @Override
    public void sendCallBack(String msg) {
        mHomeModel.sendCallBack(msg);
    }

}
