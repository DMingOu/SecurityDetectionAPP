package com.example.odm.securitydetectionapp.core;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;

/**
 * 路由类，管理通过WebSocket获取到的服务器数据
 *
 * @author: ODM
 * @date: 2019/9/5
 */
public class DataManager {




    public static  void dataRoute(String data) {
        if(data.startsWith("连") || Constant.SUCCESS.equals(data)) {
            DataManager.sendConnectSuccessEvent();
        } else if(Constant.FAILURE.equals(data)) {
            DataManager.sendConnectErrorEvent();
        } else {
            if(PageStatusManager.getPageStatus() == PageStatusManager.PAGE_WATCH_CURRENT) {
                //发送服务器数据给监控页面，类型为 cap
                BaseEvent baseEvent = EventFactory.getInstance();
                baseEvent.type = "CAP";
                baseEvent.content = data;
                EventBusUtils.postSticky(baseEvent);
            }
            if(PageStatusManager.getPageStatus() == PageStatusManager.PAGE_LOCATION_CURRENT) {
                //发送服务器数据给定位页面， 类型为 基站数据 ，模块数据
            }
        }
    }

    public  static  void sendConnectErrorEvent() {
        BaseEvent baseEvent = EventFactory.getInstance();
        baseEvent.type = Constant.STATUS;
        baseEvent.content = Constant.FAILURE;
        EventBusUtils.postSticky(baseEvent);
    }

    public static  void sendConnectSuccessEvent() {
        BaseEvent baseEvent = EventFactory.getInstance();
        baseEvent.type = Constant.STATUS;
        baseEvent.content = Constant.SUCCESS;
        EventBusUtils.postSticky(baseEvent);
    }


}
