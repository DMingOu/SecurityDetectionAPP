package com.example.odm.securitydetectionapp.module.watch.model;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.*;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.application.SecurityDetectionAPPLike;
import com.example.odm.securitydetectionapp.base.model.BaseModel;
import com.example.odm.securitydetectionapp.core.CapModuleInfoManager;
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

    public watchModel() {
        webSocket = SecurityDetectionAPPLike.getWebSocket(SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK,""),
                                                      "");
        capInfoList = new ArrayList<>();
    }


    @Override
    public void addCapInfoList(capInfoAdapter adapter) {
            capInfoList.clear();
            capInfoList.addAll(CapModuleInfoManager.getCapInfoList());
            adapter.setNewData(capInfoList);
        }


    /**
     * 清空Adapter适配器里面的数据
     */
    @Override
    public void clearAllCapInfo(capInfoAdapter adapter) {

        capInfoList.clear();
        CapModuleInfoManager.setCapInfoList(capInfoList);
        adapter.replaceData(capInfoList);
    }


    @Override
    public void  handledAbnormalModule(int position , capInfoAdapter adapter) {
        capInfo normalCapInfo = new capInfo(capInfoList.get(position).getAddress(),"",true);
        capInfoList.clear();
        capInfoList.add(normalCapInfo);
        CapModuleInfoManager.getCapInfoList().set(position , normalCapInfo);
        adapter.setNewData(capInfoList);
    }

    @Override
    public void handleModuleOffline(capInfoAdapter adapter) {
        clearAllCapInfo(adapter);
    }


    /**
     * 将反馈对象包装成 JSON 发送给 服务器
     * @param address 具体要发送反馈的模块的地址
     */
    @Override
    public void  sendCallBack (String address) {
        callBackInfo mCallBack = new callBackInfo(true,address,"");
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
            newSocket = SecurityDetectionAPPLike.getWebSocket(newUrl , "");
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
