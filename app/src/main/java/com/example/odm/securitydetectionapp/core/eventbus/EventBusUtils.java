package com.example.odm.securitydetectionapp.core.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class EventBusUtils {

    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void post(BaseEvent baseEvent) {
        EventBus.getDefault().post(baseEvent);
    }

    public static void postSticky(BaseEvent baseEvent) {
        EventBus.getDefault().postSticky(baseEvent);
    }
}