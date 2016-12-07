package com.jcmels.liba.piliplaydemo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LIBA on 11/21/2016.
 */
public class LiveStreamItem {
    @SerializedName(value = "key", alternate = {"streamkey"}) private String key;
    private String hub;
    private String snapurl;
    private String rtmpurl;
    private String pilier;
    private String piliheaderimgurl;
    private String piliroomname;

    public String getPiliroomname() {
        return piliroomname;
    }

    public void setPiliroomname(String piliroomname) {
        this.piliroomname = piliroomname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHub() {
        return hub;
    }

    public String getPilier() {
        return pilier;
    }

    public void setPilier(String pilier) {
        this.pilier = pilier;
    }

    public String getPiliheaderimgurl() {
        return piliheaderimgurl;
    }

    public void setPiliheaderimgurl(String piliheaderimgurl) {
        this.piliheaderimgurl = piliheaderimgurl;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getSnapurl() {
        return snapurl;
    }

    public void setSnapurl(String snapurl) {
        this.snapurl = snapurl;
    }

    public String getRtmpurl() {
        return rtmpurl;
    }

    public void setRtmpurl(String rtmpurl) {
        this.rtmpurl = rtmpurl;
    }
}
