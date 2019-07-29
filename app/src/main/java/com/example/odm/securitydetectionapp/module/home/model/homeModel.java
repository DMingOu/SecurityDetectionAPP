package com.example.odm.securitydetectionapp.module.home.model;

import android.util.ArrayMap;
import android.util.Log;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.base.model.BaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.echoWebSocketListener;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.home.bean.callBackInfo;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;
import com.example.odm.securitydetectionapp.module.home.contract.homeContract;
import com.example.odm.securitydetectionapp.module.home.presenter.homePresenter;
import com.example.odm.securitydetectionapp.module.home.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.WebSocket;

/**
 * The type Home model.
 *
 * @author: ODM
 * @date: 2019 /7/25
 */
public class homeModel extends BaseModel<homePresenter> implements homeContract.Model {

    WebSocket webSocket;

    private List<capInfo> capInfoList;
    private DaoSession historyDaoSession;
    boolean isDuplicated;
    public homeModel() {
        webSocket = SecurityDetectionAPP.getWebSocket(SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK,""));
        capInfoList = new ArrayList<>();
        historyDaoSession = GreenDaoManager.getInstance().getDaoSession();
    }


    @Override
    public void addCapInfoList(String capInfoData , capInfoAdapter adapter) {

        //先将JSon数据转换成 子模块对象
        capInfo mCapInfo = GsonUtil.GsonToBean(capInfoData ,capInfo.class);

        //子模块的在线状态为正常，则放在列表最上面
        if(mCapInfo.isStatus()) {
            //若有相同的地址，则修改；没有相同的地址，则直接添加进列表
            for(int i = 0 ; i < capInfoList.size() ; i++) {
                if(mCapInfo.getAddress().equals(capInfoList.get(i).getAddress())) {
                    capInfoList.get(i).setData(mCapInfo.getData());
                    isDuplicated = true;
                    break;
                }
            }
            String loggerString = "准备加入adapter   "+mCapInfo.getAddress()+ "  " + mCapInfo.isStatus() + " 重复？  " +isDuplicated;
            Logger.d("准备加入adapter   "+mCapInfo.getAddress()+ "  " + mCapInfo.isStatus() + " 重复？  " +isDuplicated);
            Log.e("Model" ,loggerString);
            //不重复，说明初次出现的子模块
            if(!isDuplicated) {
                capInfoList.add(mCapInfo);
                Logger.d("adapetr数量" + adapter.getItemCount()+mCapInfo.getAddress()+ "  " + mCapInfo.isStatus() + " 重复？  " +isDuplicated);
              }
            adapter.setNewData(capInfoList);

            }

            //当子模块的异常信息非空时。需要添加进历史消息对应的数据库,而且是新的子模块消息
            if(! "".equals(mCapInfo.getData())  ) {
                historyErrorMsg msg = new historyErrorMsg();
                msg.setTime(TimeUtil.showCurrentTime(System.currentTimeMillis()));
                msg.setAddress(mCapInfo.getAddress());
                msg.setErrorMsg(mCapInfo.getData());
                historyDaoSession.insert(msg);
            }
            isDuplicated = false;
            //通知Adapter更新
            //adapter.notifyDataSetChanged();
        }


    /**
     * 清空Adapter里面的数据
     */
    @Override
    public void clearAllCapInfo(capInfoAdapter adapter) {
        //addressList.clear();
        capInfoList.clear();
        adapter.setNewData(capInfoList);
    }

    @Override
    public int checkPosition(String address) {
//        //地址集合有相同的，说明子模块需要更新信息
//        for(int positon = 0; positon < addressList.size() ; positon++){
//            if (address.equals(addressList.get(positon))){
//                return positon;
//            }
//        }
//        //没有找到相同的，说明子模块是初次出现，返回-1
//        return  -1;
        return  -1;
    }

    /*
     *  将反馈对象包装成 JSON 发送给 服务器
     *  请求类型：搜救
     *
     */

    @Override
    public void  sendCallBack (String address) {
        callBackInfo mCallBack = new callBackInfo(false,true,address,"");
        String callBackJsonString = GsonUtil.GsonString(mCallBack);
        Logger.d(callBackJsonString);

        if(webSocket != null) {
            webSocket.send(callBackJsonString);
        } else {
            ToastUtil.showShortToastCenter("尚未连接服务器，发送反馈失败");
        }
    }

   //切换服务器
    @Override
    public void switchWebSocket(String newUrl ,capInfoAdapter adapter) {
        WebSocket newSocket = SecurityDetectionAPP.getWebSocket(newUrl);

        ToastUtil.showLongToastCenter("连接中");
        if (newSocket == null) {
            ToastUtil.showLongToastCenter("切换服务器失败");
            Logger.d("切换服务器失败: "+ newUrl);
        } else {
            //更换服务器的存储地址 ，清空子模块列表
            SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.WEBSOCK, newUrl);
            clearAllCapInfo(adapter);
            webSocket = newSocket;
        }
    }

}
