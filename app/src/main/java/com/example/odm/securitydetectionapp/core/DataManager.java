package com.example.odm.securitydetectionapp.core;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.orhanobut.logger.Logger;

/**
 * 路由类，管理通过WebSocket获取到的服务器数据
 *
 * @author: ODM
 * @date: 2019/9/5
 */
public class DataManager {




    public static  void dataRoute(String data) {
//        Logger.d("处理来自服务器的信息：  " + data);
        if(data.startsWith("连") || Constant.SUCCESS.equals(data)) {
            //EventBus 事件发送过快，粘性事件没有起作用，还是在Fragment的onCreateView 前收到了事件
            //需要让线程睡眠300ms再发送
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BaseEvent baseEvent = EventFactory.getInstance();
            baseEvent.type = Constant.STATUS;
            baseEvent.content = Constant.SUCCESS;
            EventBusUtils.postSticky(baseEvent);


        } else if(Constant.FAILURE.equals(data)) {
            DataManager.sendConnectErrorEvent();
        } else {
              if( ! data.startsWith("嵌")){
                CapModuleInfoManager.addCapInfo(data);
              }
                //发送服务器数据给CapModuleInfoManager，发送事件类型为 cap 的事件
                BaseEvent baseEvent = EventFactory.getInstance();
                baseEvent.type = Constant.CAP;
                baseEvent.content = data;
                EventBusUtils.postSticky(baseEvent);
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
        Logger.d("发送 状态正常事件");
    }


}
