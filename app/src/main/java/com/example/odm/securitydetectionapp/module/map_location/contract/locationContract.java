package com.example.odm.securitydetectionapp.module.map_location.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;

/**
 * The interface Location contract.
 *
 * @author: ODM
 * @date: 2019 /7/26
 */
public interface locationContract {
    /**
     * The interface Model.
     */
    interface Model extends IBaseModel {
        /**
         * Handle call back boolean.
         *
         * @param callbackString the callback string
         * @return the boolean
         */
        boolean  handleCallBack (String  callbackString);

        boolean  saveImage(String imageName);
    }

    /**
     * The interface View.
     */
    interface View  extends IBaseView {
        /**
         * Update abnormal list.
         */
        void updateAbnormalList();
    }

    /**
     * The interface Presenter.
     */
    interface Presenter extends IBasePresenter {

        /**
         * Handle call back.
         *
         * @param callbackString the callback string
         */
        void  handleCallBack (String  callbackString);

        /**
         * Handle call back success.
         */
        void  handleCallBackSuccess();

        boolean  saveImage(String imageName);
    }
}
