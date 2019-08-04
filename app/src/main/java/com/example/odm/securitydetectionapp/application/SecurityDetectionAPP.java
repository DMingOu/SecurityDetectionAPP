package com.example.odm.securitydetectionapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.xuexiang.xui.XUI;

import java.util.Timer;
import java.util.TimerTask;



/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class SecurityDetectionAPP extends Application {

    private  static Context mContext;
    static WebSocket  mWebsocket;
    private static final String TAG = "SecurityDetectionAPP";
    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary内存泄漏检测初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            //此过程专用于LeakCanary进行堆分析。在此过程中不应初始化应用程序。
            return;
        }
        LeakCanary.install(this);
        //初始化UI框架
        XUI.init(this);
        XUI.debug(true);
        mContext = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
        initWebSocket("ws://47.102.125.28:8080/websocket");
        initGreenDao();
    }

    public static  Context getContext(){
        return  mContext;
    }

    /**
     * 初始化WebSocket
     *
     * @param urlString the url string
     * @return the web socket
     */
    private static WebSocket initWebSocket(String  urlString ) {
        AsyncHttpClient.getDefaultInstance().websocket(
                urlString, "8080", new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            ex.printStackTrace();
                            return;
                         }
                        webSocket.setStringCallback(new com.koushikdutta.async.http.WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(String data) {
                                Logger.d("回调信息" + data);
                                //成功连接后，服务器会自动发送消息
                                if(data.startsWith("当") || "SUCCESS".equals(data)) {
                                    BaseEvent baseEvent = EventFactory.getInstance();
                                    baseEvent.type = Constant.STATUS;
                                    baseEvent.content = Constant.SUCCESS;
                                    EventBusUtils.post(baseEvent);
                                } else {
                                    BaseEvent baseEvent = EventFactory.getInstance();
                                    baseEvent.type = "CAP";
                                    baseEvent.content = data;
                                    EventBusUtils.post(baseEvent);
                                }
                            }
                        });
                        webSocket.setDataCallback(new DataCallback() {
                            @Override
                            public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                                Logger.d("I got some bytes!");
                                byteBufferList.recycle();
                            }
                        });
                        mWebsocket = webSocket;
                        // webSocket获取成功后，会覆盖之前的 地址存储
                        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.WEBSOCK ,urlString);
                    }
                });

       return mWebsocket;
    }

    /**
     * 获取WebSocket
     *外部获取WebSocket的方法
     *
     * @param urlString the url string
     * @return the web socket
     */
    public static WebSocket getWebSocket(String urlString) {
        String spUrlString = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK, "");

        if (! urlString.equals(spUrlString)) {
            //有新的服务器需要接入，尝试更换websocket
            try {
                WebSocket socketTmp = mWebsocket;
                mWebsocket = initWebSocket(urlString);
                //若更换成功，则返回成功后的webocket
                if(mWebsocket == null) {
                    mWebsocket = socketTmp;
                    return  null;
                } else {
                    return mWebsocket;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }
        //如果传进来的地址与目前存储的地址相同，则判定为获取单例socket，返回单例
            return mWebsocket;

    }


    /**
     * 全局初始化GreenDao.
     */
    public void  initGreenDao(){
        GreenDaoManager.getInstance().init(getApplicationContext());
    }


}
