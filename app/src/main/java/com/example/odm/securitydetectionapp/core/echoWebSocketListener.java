package com.example.odm.securitydetectionapp.core;

import android.widget.Toast;

import com.example.odm.securitydetectionapp.application.SecurityDetectionAPP;
import com.example.odm.securitydetectionapp.common.Constant;
import com.example.odm.securitydetectionapp.core.eventbus.BaseEvent;
import com.example.odm.securitydetectionapp.core.eventbus.EventBusUtils;
import com.example.odm.securitydetectionapp.core.eventbus.EventFactory;
import com.orhanobut.logger.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * The type Echo web socket listener.
 *
 * @author: ODM
 * @date: 2019 /7/25
 */
public class echoWebSocketListener extends WebSocketListener {



    //onOpen当WebSocket和远程建立连接时回调
    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        webSocket.send("我是客户端1");
        Logger.d("连接成功！！！");
        BaseEvent baseEvent = EventFactory.getInstance();
        baseEvent.type = Constant.STATUS;
        baseEvent.content = Constant.SUCCESS;
        EventBusUtils.post(baseEvent);
    }

    //onMessage:接收到消息时回调，只是消息内容的类型不同
    @Override
    public void onMessage(WebSocket webSocket, String text) {

        if (text != null) {
            BaseEvent baseEvent = EventFactory.getInstance();
            baseEvent.type = "CAP";
            baseEvent.content = text;
            EventBusUtils.post(baseEvent);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Logger.d("onMessage byteString: " + bytes);
    }

    //onClosing:当远程端暗示没有数据交互时回调（即此时准备关闭，但连接还没有关闭）
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        Logger.d("onClosing: " + code + "/" + reason);
    }

    //onClosed:当连接已经释放的时候被回调
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Logger.d("onClosed: " + code + "/" + reason);
    }

    //onFailure:失败时被回调（包括连接失败，发送失败等）
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Logger.d("onFailure: " + t.getMessage());
        BaseEvent baseEvent = EventFactory.getInstance();
        baseEvent.type = Constant.STATUS;
        baseEvent.content = Constant.FAILURE;
        EventBusUtils.post(baseEvent);

    }



}
