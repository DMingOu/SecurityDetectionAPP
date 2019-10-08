package com.example.odm.securitydetectionapp.application;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.example.odm.securitydetectionapp.R;
import com.example.odm.securitydetectionapp.core.DataManager;
import com.example.odm.securitydetectionapp.core.GreenDaoManager;
import com.example.odm.securitydetectionapp.module.MainActivity;
import com.example.odm.securitydetectionapp.util.SharedPreferencesUtils;
import com.fm.openinstall.OpenInstall;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.xuexiang.xui.XUI;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * description: ApplicationLike类
 * author: ODM
 * date: 2019/9/15
 */
public class SecurityDetectionAPPLike extends DefaultApplicationLike {

    private static final String TAG = "SecurityDetectionAPPLike";

    static WebSocket  mWebsocket;

    public SecurityDetectionAPPLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBugly();
        initCrashPage();
        initGreenDao();
        initLiveEventBus();
        initWebSocket("ws://192.168.1.138:8888/websocket" , "");
        initXUIFramework();
        Logger.addLogAdapter(new AndroidLogAdapter());


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    /**
     * 初始化WebSocket
     *
     * @param urlString the url string
     * @return the web socket
     */
    private static WebSocket initWebSocket(String  urlString , String protocol ) {
        AsyncHttpClient.getDefaultInstance().websocket(
                urlString, "", new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            ex.printStackTrace();
                            DataManager.getInstance().sendConnectErrorEvent();
                            Logger.d("出问题了？！错误信息："+ex.getMessage());
                            return;
                        }
                        webSocket.setStringCallback(new com.koushikdutta.async.http.WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(String data) {
                                //成功连接后，服务器会自动发送消息
                                DataManager.getInstance().dataRoute(data);
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
     * 外部获取WebSocket的方法
     *
     * @param urlString the url string
     * @param protocol  the protocol
     * @return the web socket
     */
    public static WebSocket getWebSocket(String urlString , String protocol) {
        String spUrlString = SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.WEBSOCK, "");

        if (! urlString.equals(spUrlString)) {
            //有新的服务器需要接入，尝试更换websocket
            try {
                WebSocket socketTmp = mWebsocket;
                mWebsocket = initWebSocket(urlString , protocol);
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
        GreenDaoManager.getInstance().init(getApplication());
    }


    //

    /**
     * 让用户使用时不直接闪退，而是跳到一个Activity，可以重启应用，但无法阻止或保存或上传闪退信息
     */
    private void initCrashPage(){
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_CRASH)
                .enabled(true)
                .showErrorDetails(true)
                .showRestartButton(true)
                .logErrorOnRestart(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                .errorDrawable(R.drawable.customactivityoncrash_error_image)
                .restartActivity(MainActivity.class)
                .errorActivity(null)
                .eventListener(null)
                .apply();
    }

    /**
     * 初始化UI框架
     * XUI
     */
    private void initXUIFramework() {
        //初始化UI框架
        XUI.init(getApplication());
        XUI.debug(true);
    }

    /**
     * 初始化消息总线
     * LiveEventBus
     */
    private void initLiveEventBus() {
        LiveEventBus
                .config()
                .supportBroadcast(getApplication())
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false);
    }

    private void initBugly() {
        Bugly.init(getApplication(), "91f7de2058", true);
    }
}
