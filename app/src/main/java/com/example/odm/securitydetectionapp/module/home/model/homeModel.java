package com.example.odm.securitydetectionapp.module.home.model;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.base.model.BaseModel;
import com.example.odm.securitydetectionapp.base.presenter.IBasePresenter;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.common.WebSocketUrlManager;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.echoWebSocketListener;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.module.home.bean.capInfo;
import com.example.odm.securitydetectionapp.module.home.contract.homeContract;
import com.example.odm.securitydetectionapp.module.home.presenter.homePresenter;
import com.example.odm.securitydetectionapp.module.home.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

/**
 * The type Home model.
 *
 * @author: ODM
 * @date: 2019 /7/25
 */
public class homeModel extends BaseModel<homePresenter> implements homeContract.Model {

    WebSocket webSocket;
    private List<String>  addressList;
    private List<capInfo> capInfoList;
    private DaoSession historyDaoSession;
    public homeModel() {
        webSocket = SecurityDetectionAPP.getWebSocket(WebSocketUrlManager.url);
        addressList = new ArrayList<>();
        capInfoList = new ArrayList<>();
        historyDaoSession = GreenDaoManager.getInstance().getDaoSession();
    }


    @Override
    public void addCapInfoList(String capInfoData , capInfoAdapter adapter) {
        //先将JSon数据转换成 子模块对象
        capInfo mCapInfo = GsonUtil.GsonToBean(capInfoData ,capInfo.class);
        int currentPosition = checkPosition(mCapInfo.getAddress());

        //已经存在，说明非初次出现的子模块,先删除掉过去的信息，再添加到最新
        if( -1 != currentPosition) {
            mCapInfo.setData(adapter.getData().get(currentPosition).getData() + mCapInfo.getData());
            addressList.remove(currentPosition);
            capInfoList.remove(currentPosition);
            adapter.remove(currentPosition);
            adapter.notifyDataSetChanged();
        }

        //子模块的在线状态为正常，则放在列表最上面
        if(mCapInfo.isStatus()) {
            addressList.add(mCapInfo.getAddress());
            capInfoList.add(mCapInfo);
            Logger.d(mCapInfo.toString());
            adapter.addData(mCapInfo);
            //当子模块的异常信息非空时。需要添加进历史消息对应的数据库
            if(! "".equals(mCapInfo.getData())) {
                historyErrorMsg msg = new historyErrorMsg();
                msg.setTime(TimeUtil.showCurrentTime(System.currentTimeMillis()));
                msg.setAddress(mCapInfo.getAddress());
                msg.setErrorMsg(mCapInfo.getData());
                historyDaoSession.insert(msg);
            }
            //通知Adapter更新
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空Adapter里面的数据
     */
    @Override
    public void clearAllCapInfo(capInfoAdapter adapter) {
        addressList.clear();
        capInfoList.clear();
        adapter.setNewData(capInfoList);
    }

    @Override
    public int checkPosition(String address) {
        //地址集合有相同的，说明子模块需要更新信息
        for(int positon = 0; positon < addressList.size() ; positon++){
            if (address.equals(addressList.get(positon))){
                return positon;
            }
        }
        //没有找到相同的，说明子模块是初次出现，返回-1
        return  -1;
    }

    @Override
    public void  sendCallBack (String msg) {
        if(webSocket != null) {
            webSocket.send(msg);
        }
    }

//    @Override
//    public String convertWarningData(String warningData , int position) {
//        String convertString = "";
//        String convertData = "";
//        //如果 异常信息为空字符串，说明子模块目前为正常状态,覆盖掉之前的异常信息
//        if("".equals(warningData)) {
//            convertData = "";
//            return convertData;
//        }
//        switch (warningData) {
//            case Constant.QUALITY :
//                convertString = "空气质量状态异常\n";
//                break;
//            case Constant.GAS:
//                convertString = "天然气状态异常\n";
//                break;
//            case Constant.CO:
//                convertString = "一氧化碳状态异常\n";
//                break;
//            case Constant.LIQUEFIED:
//                convertString = "液化气状态异常\n";
//                break;
//            case Constant.FELL :
//                convertString = "摔倒\n";
//                break;
//            case Constant.GOT :
//                convertString = "已收到\n";
//                break;
//            case  Constant.HELP :
//                convertString = "求救\n";
//                break;
//            default:
//
//                break;
//        }
//        //如果已经存在则叠加异常信息
//        if(-1 !=position) {
//             convertData = capInfoList.get(position).getData() + convertString;
//        } else {
//            //全新的子模块，不用叠加之前的异常信息
//            convertData = convertString;
//        }
//
//        return convertData;
//    }
}
