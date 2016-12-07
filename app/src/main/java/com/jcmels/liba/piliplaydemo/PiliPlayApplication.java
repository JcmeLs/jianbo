package com.jcmels.liba.piliplaydemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.jcmels.liba.piliplaydemo.bean.UserInfoBean;
import com.qiniu.pili.droid.streaming.StreamingEnv;

/**
 * Created by LIBA on 11/18/2016.
 */

public class PiliPlayApplication extends Application {
    public UserInfoBean userInfo;
    private static PiliPlayApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        EaseUI.getInstance().init(this,initOption());
        EMClient.getInstance().setDebugMode(true);
        Fresco.initialize(this);
        StreamingEnv.init(getApplicationContext());
    }

    private EMOptions initOption() {
        EMOptions options = new EMOptions();
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        return options;
    }
    public static PiliPlayApplication getInstance(){
        return instance;
    }
}
