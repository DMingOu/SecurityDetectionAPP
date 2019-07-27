package com.example.odm.securitydetectionapp.application;

import android.app.Application;
import android.content.Context;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.common.WebSocketUrlManager;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.echoWebSocketListener;
import com.xuexiang.xui.XUI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class SecurityDetectionAPP extends Application {

    private  static Context mContext;
    static echoWebSocketListener listener = new echoWebSocketListener();
    static OkHttpClient client = new OkHttpClient();
    static WebSocket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化UI框架
        XUI.init(this);
        XUI.debug(true);
        mContext = getApplicationContext();
        initWebSocket(WebSocketUrlManager.url);
        initGreenDao();
    }

    public static  Context getContext(){
        return  mContext;
    }

    public void initWebSocket(String  urlString ) {
        Request request = new Request.Builder().url(urlString).build();
        //调用 client.newWebSocket()后会自动连接webSocket
        WebSocket socket = client.newWebSocket(request , listener);
        //出现极端内存不足的情况，可以使用以下代码释放内存
        client.dispatcher().executorService().shutdown();

    }

    public static WebSocket getWebSocket(String urlString) {
        if(! urlString.equals(WebSocketUrlManager.url)){
            //有新的服务器需要接入，更换websocket
            Request request = new Request.Builder().url(urlString).build();
            //调用 client.newWebSocket()后会自动连接webSocket
            WebSocket newWebSocket = client.newWebSocket(request , listener);
            return newWebSocket;
        }

        return  socket;
    }

    //全局初始化GreenDao
    private void  initGreenDao(){
        GreenDaoManager.getInstance().init(getApplicationContext());
    }


}
