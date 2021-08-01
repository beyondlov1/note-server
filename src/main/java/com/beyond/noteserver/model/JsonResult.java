package com.beyond.noteserver.model;

import lombok.Data;

@Data
public class JsonResult<T> {
    private int code = 0;
    private String msg;
    private T data;

    public static <T> JsonResult<T> success(String msg, T t) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.code = 0;
        jsonResult.msg = msg;
        jsonResult.data = t;
        return jsonResult;
    }

    public static <T> JsonResult<T> success(T t) {
        return JsonResult.success(null, t);
    }

    public static <T> JsonResult<T> error(String msg) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.code = -1;
        jsonResult.msg = msg;
        return jsonResult;
    }

    public static <T> JsonResult<T> exception(int code, String msg) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.code = code;
        jsonResult.msg = msg;
        return jsonResult;
    }

    public static final int CODE_NO_REPO = 400;
}