package com.jcmels.liba.piliplaydemo.bean;

/**
 * Created by LIBA on 11/22/2016.
 */

public class UserInfosBean {
    private UserInfoBean list;

    public UserInfosBean(UserInfoBean list) {
        this.list = list;
    }

    public UserInfosBean() {
    }

    public UserInfoBean getList() {
        return list;
    }

    public void setList(UserInfoBean list) {
        this.list = list;
    }
}
