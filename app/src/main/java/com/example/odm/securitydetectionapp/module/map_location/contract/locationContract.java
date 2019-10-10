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


        /**
         * 保存图片
         *
         * @param imageName the image name
         * @return boolean
         */
        boolean  saveImage(String imageName);


    }

    /**
     * The interface View.
     */
    interface View  extends IBaseView {


        /**
         * 更新定位模块的信息
         *
         * @param locateInfo 定位模块的信息
         */
        void moduleLocationChanged(LocateInfo locateInfo) ;
    }

    /**
     * The interface Presenter.
     */
    interface Presenter extends IBasePresenter {


        /**
         * 解析定位信息
         *
         * @param data 定位信息字符串(Json)
         */
        void handleLocationInfo(String data);

        /**
         * 保存图片
         *
         * @param imageName 图片全名
         * @return the boolean
         */
        boolean  saveImage(String imageName);
    }
}
