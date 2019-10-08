package com.example.odm.securitydetectionapp.core;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;

/**
 * 数据路由类，管理通过WebSocket获取到的服务器数据
 * <p>
 * author: ODM
 * date: 2019/9/5
 */
public class DataManager {

    private DataManager(){}

    /**
     * 获取DataManager的单例
     *
     * @return the instance
     */
    public static DataManager getInstance() {
        return  DataManagerHolder.INSTANCE;
    }

    private static class DataManagerHolder {
        private static final DataManager INSTANCE = new DataManager();
    }

    /**
     * 处理WebSocket接收的数据，进行数据分析后分发事件
     *
     * @param data websocket传入数据
     */
    public void dataRoute(String data) {
        if(data.contains(Constant.TAGTOSTATION)) {
            //Json 包含着 tagToStation 字段 , 发送 定位信息事件
            sendLocationEvent(data);
        }else if(data.contains(Constant.ADDRESS)) {
            //Json包含着 address 字段，发送 模块信息事件
            //加入全局帽子模块信息管理
            CapModuleInfoManager.addCapInfo(data);
            sendCapEvent(data);
        } else if(data.startsWith("连") || Constant.SUCCESS.equals(data)) {
            sendConnectSuccessEvent();
        } else if(Constant.FAILURE.equals(data)) {
            sendConnectErrorEvent();
        } else {
            if(data.startsWith("嵌")) {
                sendOfflineEvent(data);
            }
        }
    }

    /**
     * 分发WebSocket连接异常事件
     */
    public  void sendConnectErrorEvent() {
//        BaseEvent baseEvent = EventFactory.getInstance();
//        baseEvent.type = Constant.STATUS;
//        baseEvent.content = Constant.FAILURE;
//        EventBusUtils.postSticky(baseEvent);
        LiveEventBus
                .get(Constant.FAILURE)
                .post(Constant.FAILURE);
    }


    /**
     * 分发WebSocket连接正常事件
     */
    private void sendConnectSuccessEvent() {
//        BaseEvent baseEvent = EventFactory.getInstance();
//        baseEvent.type = Constant.STATUS;
//        baseEvent.content = Constant.SUCCESS;
//        EventBusUtils.postSticky(baseEvent);
//        Logger.d("发送 状态正常事件");

        LiveEventBus
                .get(Constant.SUCCESS)
                .post(Constant.SUCCESS);
    }

    /**
     * 发送事件类型为 模块事件信息处理 的事件
     * @param data 模块数据
     */
    private  void sendCapEvent(String data) {
        //发送服务器数据给CapModuleInfoManager，发送事件类型为 cap 的事件

//        BaseEvent baseEvent = EventFactory.getInstance();
//        baseEvent.type = Constant.CAP;
//        baseEvent.content = capData;
//        EventBusUtils.postSticky(baseEvent);

        LiveEventBus
                .get(Constant.ADDRESS)
                .post(data);
    }

    /**
     * 发送事件类型为设备离线的事件
     * @param data 固定值  "嵌入式设备下线"
     */
    private void sendOfflineEvent(String  data) {
        LiveEventBus
                .get(Constant.OFFLINE)
                .post(data);
    }

    /**
     * 发送事件类型为 处理定位信息 的事件
     * @param data
     */
    private void sendLocationEvent(String data) {
        LiveEventBus
                .get(Constant.LOCATION)
                .post(data);
    }


}
