package com.example.odm.securitydetectionapp.core.eventbus;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class EventFactory {
    public static BaseEvent getInstance() {
        return new BaseEvent();
    }
}