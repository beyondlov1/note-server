package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class Message {
    private int type;
    private Object data;

    public static Message notify(Object data){
        Message message = new Message();
        message.type = 1;
        message.data = data;
        return message;
    }

    public static Message reload(String repoAbsPath){
        Message message = new Message();
        message.type = 2;
        message.data = repoAbsPath;
        return message;
    }
}
