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

    public locationPresenter(locationFragment view) {
        super(view);
        mlocationModel = new locationModel();
    }


    @Override
    public void handleLocationInfo(String data) {
        if(! "".equals(data)) {
            LocateInfo locateInfo = GsonUtil.GsonToBean(data, LocateInfo.class);
            if(locateInfo != null) {
                getView().moduleLocationChanged(locateInfo);
            } else {
                Logger.d("转换出了一个NULL的定位信息对象");
            }
        }
    }


    @Override
    public boolean saveImage(String imageName) {
        return mlocationModel.saveImage(imageName);
    }
}
