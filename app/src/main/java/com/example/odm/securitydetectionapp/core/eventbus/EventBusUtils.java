package com.example.odm.securitydetectionapp.core.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * The type Event bus utils.
 *
 * @author: ODM
 * @date: 2019 /7/25
 */
public class EventBusUtils {

    /**
     * 取消注册EventBus
     *
     * @param subscriber the subscriber
     */
    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 注册EventBus
     *
     * @param subscriber the subscriber
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 将事件发送出去
     *
     * @param baseEvent the base event
     */
    public static void post(BaseEvent baseEvent) {
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 将粘性事件发送出去
     *
     * @param baseEvent the base event
     */
    public static void postSticky(BaseEvent baseEvent) {
        EventBus.getDefault().postSticky(baseEvent);
    }
}