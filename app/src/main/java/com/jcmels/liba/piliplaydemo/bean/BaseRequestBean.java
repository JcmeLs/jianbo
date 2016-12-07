package com.jcmels.liba.piliplaydemo.bean;

/**
 * Created by LIBA on 11/18/2016.
 */

public class BaseRequestBean<T> {


    /**
     * resultcode : 200
     * reason : success
     * error_code : 0
     * data : {"list":[{"key":"posttest","hub":"jcme-live","snapurl":"http://pili-live-snapshot.jcmels.top/jcme-live/posttest.jpg","rtmpurl":"rtmp://pili-live-rtmp.jcmels.top/jcme-live/posttest"}]}
     */

    private String resultcode;
    private String reason;
    private String error_code;
    private T data;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
