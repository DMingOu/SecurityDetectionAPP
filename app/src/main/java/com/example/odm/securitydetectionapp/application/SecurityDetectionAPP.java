package com.example.odm.securitydetectionapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.echoWebSocketListener;
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
import com.xuexiang.xui.XUI;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class SecurityDetectionAPP extends Application {

    private  static Context mContext;
//    static echoWebSocketListener listener = new echoWebSocketListener();
//    static OkHttpClient client = new OkHttpClient();
    static WebSocket  mWebsocket;
    private static final String TAG = "SecurityDetectionAPP";
    @Override
    public void onCreate() {

        super.onCreate();
        //初始化UI框架
        XUI.init(this);
        XUI.debug(true);
        mContext = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
        //云服务器 ws://121.40.165.18:8800
        //ws://47.102.125.28:8080/websocket
        //ws://echo.websocket.org
        initWebSocket("ws://47.102.125.28:8080/websocket");
        initGreenDao();
    }

    public static  Context getContext(){
        return  mContext;
    }

    public static WebSocket initWebSocket(String  urlString ) {
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
                                if(data.startsWith("当") || data.startsWith("S")) {
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
                                // note that this data has been read
                                byteBufferList.recycle();
                            }
                        });
                        mWebsocket = webSocket;
                    }
                });
       SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.WEBSOCK ,urlString);
       return mWebsocket;
    }

    public static WebSocket getWebSocket(String urlString) {
        String spUrlString = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK, "");

        if (! urlString.equals(spUrlString)) {
            //有新的服务器需要接入，更换websocket
            try {
//                Request request = new Request.Builder().url(urlString).build();
//                socket = client.newWebSocket(request, listener);
                //调用 client.newWebSocket()后会自动连接webSocket
                //如果 SP 里面存有 地址，而且与 传进来的新地址不一样，则用新地址
                WebSocket socketTmp = mWebsocket;
                mWebsocket = initWebSocket(urlString);
                //若更换成功，则返回成功后的webocket
                if(mWebsocket == null) {
                    mWebsocket = socketTmp;
                    return  null;
                } else {
                    SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.WEBSOCK, urlString);
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

    //全局初始化GreenDao
    private void  initGreenDao(){
        GreenDaoManager.getInstance().init(getApplicationContext());
    }


}
