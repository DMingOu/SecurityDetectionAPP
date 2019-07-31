package com.example.odm.securitydetectionapp.module.map_location.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public interface locationContract {
    interface Model extends IBaseModel {
        boolean  handleCallBack (String  callbackString);
    }

    interface View  extends IBaseView {
        void updateAbnormalList();
    }

    interface Presenter extends IBasePresenter {

        void  handleCallBack (String  callbackString);

        void  handleCallBackSuccess();
    }
}
