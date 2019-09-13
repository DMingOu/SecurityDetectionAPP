package com.example.odm.securitydetectionapp.core;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;

/**
 * 数据路由类，管理通过WebSocket获取到的服务器数据
 *
 * author: ODM
 * date: 2019/9/5
 */
public class DataManager {

    private DataManager(){}

    public static DataManager getInstance() {
        return  DataManagerHolder.INSTANCE;
    }

    private static class DataManagerHolder {
        private static final DataManager INSTANCE = new DataManager();
    }

    /**
     * 处理WebSocket接收的数据，进行数据分发
     * @param data websocket传入数据
     */
    public void dataRoute(String data) {
//        Logger.d("处理来自服务器的信息：  " + data);
        if(data.startsWith("连") || Constant.SUCCESS.equals(data)) {
            sendConnectSuccessEvent();
        } else if(Constant.FAILURE.equals(data)) {
            sendConnectErrorEvent();
        } else {
              if( ! data.startsWith("嵌")){
                CapModuleInfoManager.addCapInfo(data);
              }
              sendCapEvent(data);
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
     * 发送事件类型为 cap 的事件
     * @param capData 帽子数据
     */
    private  void sendCapEvent(String capData) {
        //发送服务器数据给CapModuleInfoManager，发送事件类型为 cap 的事件
//        BaseEvent baseEvent = EventFactory.getInstance();
//        baseEvent.type = Constant.CAP;
//        baseEvent.content = capData;
//        EventBusUtils.postSticky(baseEvent);

        LiveEventBus
                .get(Constant.CAP)
                .post(capData);
    }


}
