package com.beyond.noteserver.controller;

import com.beyond.jgit.util.JsonUtils;
import com.beyond.noteserver.model.Message;
import com.beyond.noteserver.model.Todo;
import com.beyond.noteserver.model.TodoReplaceUnit;
import com.beyond.noteserver.util.WebSocketUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/message")
public class MessageEndPoint extends AbstractEndPoint {
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void sendToRepo(String repoAbsPath, Object message) throws IOException {
        Session session = sessionMap.get(repoAbsPath);
        if (session != null){
            session.getBasicRemote().sendText(JsonUtils.writeValueAsString(Message.notify(message)));
        }
    }

    public void reloadRepo(String repoAbsPath) throws IOException {
        Session session = sessionMap.get(repoAbsPath);
        if (session != null){
            session.getBasicRemote().sendText(JsonUtils.writeValueAsString(Message.reload(repoAbsPath)));
        }
    }


    @Override
    protected Map<String, Session> getSessionMap() {
        return sessionMap;
    }

    @Override
    public void onMessageInternal(String message, Session session) throws IOException, ParseException {
        Properties properties = WebSocketUtils.parseQueryString(session);
        String repoAbsPath = properties.getProperty("repoAbsPath");

        TodoReplaceUnit unit = JsonUtils.readValue(message, TodoReplaceUnit.class);
        if (StringUtils.equals(unit.getRepoAbsPath(), repoAbsPath)){
            File file = new File(unit.getFilePath());
            String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            Todo todo = Todo.parseFrom(unit.getContent());
            todo.setReminded(true);
            FileUtils.writeStringToFile(file, StringUtils.replace(s, unit.getContent(), todo.toFormattedLine()), StandardCharsets.UTF_8);
            reloadRepo(repoAbsPath);
        }
    }
}
