package com.example.odm.securitydetectionapp.module.watch.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;

/**
 * The interface Home contract.
 *
 * @author: ODM
 * @date: 2019 /7/25
 */
public interface watchContract {

    /**
     * The interface Model.
     */
    interface Model extends IBaseModel {

        /**
         * Send call back.
         *
         * @param msg the msg
         */
        void sendCallBack(String msg);

        /**
         * 将模块信息加入模块列表并进行处理
         *
         * @param capInfoData the cap info data
         * @param adapter     the adapter
         */
        void addCapInfoList(capInfoAdapter adapter);

        /**
         * 清空所有模块信息
         *
         * @param adapter the adapter
         */
        void clearAllCapInfo(capInfoAdapter adapter);

        /**
         * Switch web socket.
         *
         * @param newUrl  the new url
         * @param adapter the adapter
         */
        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);


        void  handledAbnormalModule(int position , capInfoAdapter adapter);

        void  handleModuleOffline(capInfoAdapter adapter);
    }

    /**
     * The interface View.
     */
    interface View  extends IBaseView {

        /**
         * Gets adapter.
         *
         * @return the adapter
         */
        capInfoAdapter getAdapter();

    }

    /**
     * The interface Presenter.
     */
    interface Presenter extends IBasePresenter {
        /**
         * Check cap info.
         *
         * @param data    the data
         * @param adapter the adapter
         */
        void  checkCapInfo(capInfoAdapter adapter);

        /**
         * Send call back.
         *
         * @param msg the msg
         */
        void sendCallBack(String msg) ;

        /**
         * Switch web socket.
         *
         * @param newUrl  the new url
         * @param adapter the adapter
         */
        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);


        void  handledAbnormalModule(int position ,capInfoAdapter adapter);

        void  handleModuleOffline(capInfoAdapter adapter);
    }



}
