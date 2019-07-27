package com.example.odm.securitydetectionapp.core;

import com.orhanobut.logger.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


/**
 * @author: ODM
 * @date: 2019/7/25
 */
public class webSocketClient extends WebSocketClient {

    public webSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.d("webSocketClient已连接");
    }

    @Override
    public void onMessage(String message) {
        Logger.d("onMessage     "+ message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.d("onClose");
    }

    @Override
    public void onError(Exception ex) {
        Logger.d("onError");
    }
}
