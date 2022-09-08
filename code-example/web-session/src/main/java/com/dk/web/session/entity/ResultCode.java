package com.dk.web.session.entity;


/**
 * @author dkay
 * @version 1.0
 */
public class ResultCode<T> {
    private String msg;
    private Integer code;
    private T data;

    public static ResultCode<String> defaultFailResultCode() {
        ResultCode<String> result = new ResultCode<>();
        result.setCode(1000101);
        result.setMsg("login fail");
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
