package com.qingcheng.entity;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private int code;
    private T data;
    private String msg;

    public Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result(int code, String msg) {
        this(code, null, msg);
    }


    public Result(T data) {
        this(200, data, "success");
    }

    public Result() {
        this(200, null, "success");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
