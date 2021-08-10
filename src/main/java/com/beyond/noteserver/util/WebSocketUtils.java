package com.beyond.noteserver.util;

import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;
import java.util.Properties;

public class WebSocketUtils {

    public static Properties parseQueryString(String queryString){
        String[] params = StringUtils.split(queryString, "&");
        Properties properties = org.springframework.util.StringUtils.splitArrayElementsIntoProperties(params, "=");
        if (properties == null){
            throw new IllegalArgumentException("queryString is illegal");
        }
        return properties;
    }

    public static Properties parseQueryString(Session session){
        String queryString = session.getQueryString();
        String[] params = StringUtils.split(queryString, "&");
        Properties properties = org.springframework.util.StringUtils.splitArrayElementsIntoProperties(params, "=");
        if (properties == null){
            throw new IllegalArgumentException("queryString is illegal");
        }
        return properties;
    }
}

