package com.example.odm.securitydetectionapp.module.home.contract;

import com.example.odm.securitydetectionapp.base.model.IBaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.base.view.IBaseView;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;
import com.example.odm.securitydetectionapp.module.home.ui.capInfoAdapter;

import java.util.List;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public interface homeContract {

    interface Model extends IBaseModel {
      //  void initWebSocketClient(String  uriString);
        void sendCallBack(String msg);

        void addCapInfoList(String capInfoData ,capInfoAdapter adapter);
        void clearAllCapInfo(capInfoAdapter adapter);
         int  checkPosition(String address);

        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);

        //String convertWarningData(String warningData ,int position);
    }

    interface View  extends IBaseView {

        capInfoAdapter getAdapter();

    }

    interface Presenter extends IBasePresenter {
        //void toInitWebSocketClient(String  uriString);
        void  checkCapInfo(String  data ,capInfoAdapter adapter);
        void sendCallBack(String msg) ;

        void switchWebSocket(String  newUrl ,capInfoAdapter adapter);
    }

}
