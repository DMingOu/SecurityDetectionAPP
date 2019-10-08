package com.example.odm.securitydetectionapp.module.map_location.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;
import com.example.odm.securitydetectionapp.bean.LocateInfo;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;

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

//        boolean  handleCallBack (String  callbackString);
        /**
         *保存图片
         * @param imageName
         * @return
         */
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

        /**
         * 更新定位模块的信息
         * @param locateInfo 定位模块的信息
         */
         void moduleLocationChanged(LocateInfo locateInfo) ;
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
