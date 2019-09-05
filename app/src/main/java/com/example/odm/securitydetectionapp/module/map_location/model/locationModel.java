package com.example.odm.securitydetectionapp.module.map_location.model;

import com.example.odm.securitydetectionapp.bean.callBackInfo;
import com.example.odm.securitydetectionapp.core.PointManager;
import com.example.odm.securitydetectionapp.module.map_location.contract.locationContract;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.orhanobut.logger.Logger;

/**
 * @author: ODM
 * @date: 2019/7/26
 */
public class locationModel implements locationContract.Model {

    private callBackInfo  mCallBackInfo;

    public locationModel() {
        super();

    }

    @Override
    public boolean handleCallBack(String callbackString) {

        if(callbackString.startsWith("{")){
            mCallBackInfo   = GsonUtil.GsonToBean(callbackString,callBackInfo.class);
        }
        //callBackInfo 的 message 字段 储存着 发生异常的模块地址
        if(mCallBackInfo != null && mCallBackInfo.getLocate()) {
            if(mCallBackInfo.getMessage() == null) {
                Logger.d("类型不符合callBackInfo");
                return false;
            } else {
                PointManager.getAbnormalList().add(mCallBackInfo.getMessage());
                Logger.d("地图异常坐标：   "+ mCallBackInfo.getMessage());
                //成功加入后返回true，刷新页面
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean saveImage(String imageName) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.IMAGENAME , imageName);
        return true;
    }
}
