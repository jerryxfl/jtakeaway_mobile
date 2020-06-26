package com.jerry.jtakeaway.websocket;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.utils.LogPrint;

import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private webSocketListener webSocketListener;

    public WebSocketClient(URI serverUri,webSocketListener webSocketListener) {
        super(serverUri);
        this.webSocketListener = webSocketListener;
     }

    public WebSocketClient(URI serverUri, Draft protocolDraft,webSocketListener webSocketListener) {
        super(serverUri, protocolDraft);
        this.webSocketListener = webSocketListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LogPrint.print("链接开启",LogPrint.logType.Waring);
        if(webSocketListener!=null)webSocketListener.onOpen(handshakedata);
    }

    @Override
    public void onMessage(String message) {
        LogPrint.print("消息来了:"+message,LogPrint.logType.Waring);
        JSONObject json = JSONObject.parseObject(message);
        System.out.println("json:"+json.toString());
        Msg msg = new Msg();
        msg.setId(json.getInteger("id"));
        msg.setAcceptuserid(json.getInteger("acceptuserid"));
        msg.setSenduserid(json.getInteger("senduserid"));
        msg.setContent(json.getString("content"));
        msg.setSendTime(json.getTimestamp("sendTime"));
        if(webSocketListener!=null)webSocketListener.onMessage(msg);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogPrint.print("链接关闭",LogPrint.logType.Waring);
        if(webSocketListener!=null)webSocketListener.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        LogPrint.print("链接错误",LogPrint.logType.Waring);
        if(webSocketListener!=null)webSocketListener.onError(ex);
    }

    public interface webSocketListener{
        void onOpen(ServerHandshake handshakedata);
        void onMessage(Msg message);
        void onClose(int code, String reason, boolean remote);
        void onError(Exception ex);

    }
}
