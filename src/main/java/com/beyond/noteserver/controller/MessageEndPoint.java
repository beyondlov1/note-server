package com.beyond.noteserver.controller;

import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/message")
public class MessageEndPoint extends AbstractEndPoint {
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void sendToRepo(String repoAbsPath, String message) throws IOException {
        Session session = sessionMap.get(repoAbsPath);
        if (session != null){
            session.getBasicRemote().sendText(message);
        }
    }

    @Override
    protected Map<String, Session> getSessionMap() {
        return sessionMap;
    }
}
