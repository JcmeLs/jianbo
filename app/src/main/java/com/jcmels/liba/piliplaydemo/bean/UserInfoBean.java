package com.jcmels.liba.piliplaydemo.bean;

/**
 * Created by LIBA on 11/22/2016.
 */

public class UserInfoBean {

    /**
     * _id : 5833ff4e3d74c7aa3cdb1a8f
     * username : barry
     * email : barry@oocl.com
     * password : 25d55ad283aa400af464c76d713c07ad
     * __v : 0
     * piliurl : rtmp://pili-publish.jcmels.top/jcme-live/5833ff4e3d74c7aa3cdb1a8f?e=1479802762&token=7ylDgTI4R60h1lU__GgpJwA-LKZHCWXDXB5gXmkb:9XRtPfSgob6mwpiMFJGmBsJx1RY=
     * streamkey : 5833ff4e3d74c7aa3cdb1a8f
     */

    private String _id;
    private String username;
    private String email;
    private String password;
    private int __v;
    private String piliurl;
    private String streamkey;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getPiliurl() {
        return piliurl;
    }

    public void setPiliurl(String piliurl) {
        this.piliurl = piliurl;
    }

    public String getStreamkey() {
        return streamkey;
    }

    public void setStreamkey(String streamkey) {
        this.streamkey = streamkey;
    }
}
