package com.example.odm.securitydetectionapp.module.home.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;
import com.example.odm.securitydetectionapp.module.home.ui.capInfoAdapter;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public interface homeContract {

    interface Model extends IBaseModel {

        void sendCallBack(String msg);
        void addCapInfoList(String capInfoData ,capInfoAdapter adapter);
        void clearAllCapInfo(capInfoAdapter adapter);
        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);

    }

    interface View  extends IBaseView {

        capInfoAdapter getAdapter();

    }

    interface Presenter extends IBasePresenter {
        void  checkCapInfo(String  data ,capInfoAdapter adapter);
        void sendCallBack(String msg) ;
        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);
    }

}
