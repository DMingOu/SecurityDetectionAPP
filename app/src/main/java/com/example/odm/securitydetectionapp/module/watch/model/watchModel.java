package com.example.odm.securitydetectionapp.module.watch.model;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.*;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.base.model.BaseModel;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.module.history.bean.historyErrorMsg;
import com.example.odm.securitydetectionapp.bean.callBackInfo;
import com.example.odm.securitydetectionapp.bean.capInfo;
import com.example.odm.securitydetectionapp.module.watch.contract.watchContract;
import com.example.odm.securitydetectionapp.module.watch.presenter.watchPresenter;
import com.example.odm.securitydetectionapp.module.watch.ui.capInfoAdapter;
import com.example.odm.securitydetectionapp.util.GsonUtil;
import com.example.odm.securitydetectionapp.util.RegexUtil;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.TimeUtil;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.koushikdutta.async.http.WebSocket;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 监控页面 Model层
 *
 * author: ODM
 * date: 2019 /7/25
 */
public class watchModel extends BaseModel<watchPresenter> implements watchContract.Model {

    WebSocket webSocket;

    private List<capInfo> capInfoList;
    private DaoSession historyDaoSession;
    boolean isDuplicated;
    public watchModel() {
        webSocket = SecurityDetectionAPP.getWebSocket(SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK,""),
                                                      "");
        capInfoList = new ArrayList<>();
        historyDaoSession = GreenDaoManager.getInstance().getDaoSession();
    }


    @Override
    public void addCapInfoList(String capInfoData , capInfoAdapter adapter) {

        //先将JSon数据转换成 子模块对象
        capInfo mCapInfo = GsonUtil.GsonToBean(capInfoData ,capInfo.class);
        if(mCapInfo.getData() == null) {
            Logger.d("类型不符合capInfo");
            return;
        }

        if( mCapInfo.getData()!= null) {

            //若有相同的地址，则修改；没有相同的地址，则直接添加进列表
            for(int i = 0 ; i < capInfoList.size() ; i++) {
                if(mCapInfo.getAddress().equals(capInfoList.get(i).getAddress())) {
                    //子模块的在线状态为正常，则放在列表最上面，若为不正常，则在列表中查找并删除它
                    if(mCapInfo.isStatus() ) {
                        capInfoList.get(i).setData(mCapInfo.getData());
                    } else {
                        capInfoList.remove(i) ;
                    }
                    isDuplicated = true;
                    break;
                }
            }
            //不重复，说明初次出现的子模块，需要加入子模块列表
            if(!isDuplicated) {
                capInfoList.add(mCapInfo);
              }
            adapter.setNewData(capInfoList);
            }
            //当子模块的异常信息非空时。需要添加进历史消息对应的数据库,而且是新的子模块消息
            if(! "".equals(mCapInfo.getData()) && mCapInfo.getStatus() ) {
                historyErrorMsg msg = new historyErrorMsg();
                msg.setTime(TimeUtil.showCurrentTime(System.currentTimeMillis()));
                msg.setAddress(mCapInfo.getAddress());
                String errorInfo = mCapInfo.getData();
                errorInfo = errorInfo.replaceAll("\r | \n" ,"");
                msg.setErrorMsg(errorInfo);
                historyDaoSession.insert(msg);
            }
            isDuplicated = false;
        }


    /**
     * 清空Adapter适配器里面的数据
     */
    @Override
    public void clearAllCapInfo(capInfoAdapter adapter) {

        capInfoList.clear();
        adapter.setNewData(capInfoList);
    }


    /*
     *  将反馈对象包装成 JSON 发送给 服务器
     *  请求类型：搜救
     */
    @Override
    public void  sendCallBack (String address) {
        callBackInfo mCallBack = new callBackInfo(false,true,address,"");
        String callBackJsonString = GsonUtil.GsonString(mCallBack);
        Logger.d("反馈"+callBackJsonString);

        if(webSocket != null) {
            webSocket.send(callBackJsonString);
        } else {
            ToastUtil.showShortToastCenter("尚未连接服务器，发送反馈失败");
        }
    }

   /*
    * 切换服务器
    */

    @Override
    public void switchWebSocket(String newUrl ,capInfoAdapter adapter) {
        WebSocket  newSocket;
        String pattern = "(^ws:)+(.*websocket.*)";
        boolean isMatch = Pattern.matches(pattern , newUrl);
//        //捕获新Websocket地址的端口值
//        List<String> protocolList = RegexUtil.getAllSatisfyStr(newUrl , "(?<=:)[0-9]*(?=\\/)");
        //匹配 以 ws 开头，且字符串包含 websocket 的地址
        if(isMatch) {
//            ToastUtil.showLongToastCenter("连接中");
            newSocket = SecurityDetectionAPP.getWebSocket(newUrl , "");
            if (newSocket == null) {
                ToastUtil.showLongToastCenter("切换服务器失败");
            } else {
                //清空子模块列表
                clearAllCapInfo(adapter);
                webSocket = newSocket;
            }
        } else {
            ToastUtil.showLongToastCenter("无法识别此地址");
        }
    }

}
