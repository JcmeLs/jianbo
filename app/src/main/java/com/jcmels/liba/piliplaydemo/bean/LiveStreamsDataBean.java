package com.jcmels.liba.piliplaydemo.bean;

import com.jcmels.liba.piliplaydemo.bean.LiveStreamItem;

import java.util.List;

/**
 * Created by LIBA on 11/21/2016.
 */
public class LiveStreamsDataBean {
    /**
     * key : posttest
     * hub : jcme-live
     * snapurl : http://pili-live-snapshot.jcmels.top/jcme-live/posttest.jpg
     * rtmpurl : rtmp://pili-live-rtmp.jcmels.top/jcme-live/posttest
     */

    private List<LiveStreamItem> list;

    public LiveStreamsDataBean(List<LiveStreamItem> list) {
        this.list = list;
    }

    public LiveStreamsDataBean() {
    }

    public List<LiveStreamItem> getList() {
        return list;
    }

    public void setList(List<LiveStreamItem> list) {
        this.list = list;
    }

}
