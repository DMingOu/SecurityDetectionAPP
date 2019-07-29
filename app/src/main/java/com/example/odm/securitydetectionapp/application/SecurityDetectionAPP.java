package com.example.odm.securitydetectionapp.application;

import android.app.Application;
import android.content.Context;

import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.core.echoWebSocketListener;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.example.odm.securitydetectionapp.util.ToastUtil;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.XUI;

import java.util.Timer;
import java.util.TimerTask;

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
        initWebSocket("ws://ce0656b6.ngrok.io/websocket");
        initGreenDao();
    }

    public static  Context getContext(){
        return  mContext;
    }

    public void initWebSocket(String  urlString ) {
        //获取Sharedprefences 里面存储的 WebSocket地址，如果没有则返回默认的""
        String spUrlString = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK ,"");
        if((! "".equals(spUrlString) )&& (spUrlString!= null)) {
            urlString = spUrlString;
        } else {
            SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.WEBSOCK,urlString);
        }

        Request request = new Request.Builder().url(urlString).build();
        socket = client.newWebSocket(request , listener);
        //出现极端内存不足的情况，可以使用以下代码释放内存
        client.dispatcher().executorService().shutdown();
    }

    public static WebSocket getWebSocket(String urlString) {
        String spUrlString = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK, "");
        if (!urlString.equals(spUrlString)) {
            //有新的服务器需要接入，更换websocket
            try {
                Request request = new Request.Builder().url(urlString).build();

                //调用 client.newWebSocket()后会自动连接webSocket
                return client.newWebSocket(request, listener);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }
        //如果传进来的地址与目前存储的地址相同，则判定为获取单例socket，返回单例
            return socket;

    }

    //全局初始化GreenDao
    private void  initGreenDao(){
        GreenDaoManager.getInstance().init(getApplicationContext());
    }


}
