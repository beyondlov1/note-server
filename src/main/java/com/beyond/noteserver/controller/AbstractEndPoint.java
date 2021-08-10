package com.beyond.noteserver.controller;

import com.beyond.noteserver.util.WebSocketUtils;

import javax.websocket.*;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractEndPoint {

    

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("open");
        Properties properties = WebSocketUtils.parseQueryString(session);
        String repoAbsPath = properties.getProperty("repoAbsPath");
        getSessionMap().put(repoAbsPath, session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("message received:"+message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("error");
    }

    @OnClose
    public void onClose(Session session) {
        Properties properties = WebSocketUtils.parseQueryString(session);
        String repoAbsPath = properties.getProperty("repoAbsPath");
        getSessionMap().remove(repoAbsPath);
        System.out.println("close");
    }
    
    protected abstract Map<String,Session> getSessionMap();
}
