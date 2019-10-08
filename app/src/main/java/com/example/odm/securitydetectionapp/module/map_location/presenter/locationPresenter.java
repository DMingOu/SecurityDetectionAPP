package com.example.odm.securitydetectionapp.module.map_location.presenter;

import com.example.odm.securitydetectionapp.base.presenter.BasePresenter;
import com.example.odm.securitydetectionapp.bean.LocateInfo;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.module.map_location.model.locationModel;
import com.example.odm.securitydetectionapp.module.map_location.ui.locationFragment;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.orhanobut.logger.Logger;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class locationPresenter extends BasePresenter<locationContract.View>implements locationContract.Presenter {

    private static final String TAG = "locationPresenter";
    private locationModel mlocationModel;
    private LocateInfo locateInfo;

    public locationPresenter(locationFragment view) {
        super(view);
        mlocationModel = new locationModel();
    }

    @Override
    public void handleCallBack(String callbackString) {
        if ( ! callbackString.startsWith("嵌")) {
            //收到了模块状态更改(变异常)的数据，去M层解析
            if(mlocationModel.handleCallBack(callbackString)) {
                handleCallBackSuccess();
            }
        } else {
            Logger.d("嵌入式设备下线了" + TimeUtil.showCurrentTime(System.currentTimeMillis()));
        }

    }

    public void handleLocationInfo(String data) {
        if(! "".equals(data)) {
            locateInfo = GsonUtil.GsonToBean(data , LocateInfo.class);
            if(locateInfo != null) {
                getView().moduleLocationChanged(locateInfo);
            } else {
                Logger.d("转换出了一个NULL的定位信息对象");
            }
        }
    }

    @Override
    public void handleCallBackSuccess() {
        getView().updateAbnormalList();
    }

    @Override
    public boolean saveImage(String imageName) {
        return mlocationModel.saveImage(imageName);
    }
}
