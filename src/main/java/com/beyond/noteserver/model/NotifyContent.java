package com.beyond.noteserver.model;

import com.beyond.jgit.util.JsonUtils;
import lombok.Data;

@Data
public class NotifyContent {
    private String msg;
    private Object attachment;

    public static NotifyContent of(String msg, Object attachment){
        NotifyContent content = new NotifyContent();
        content.setMsg(msg);
        content.setAttachment(attachment);
        return content;
    }

    public static NotifyContent of(String msg){
        NotifyContent content = new NotifyContent();
        content.setMsg(msg);
        return content;
    }

    public String toJsonString() {
        return JsonUtils.writeValueAsString(this);
    }
}
