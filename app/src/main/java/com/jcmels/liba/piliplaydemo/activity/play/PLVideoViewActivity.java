package com.jcmels.liba.piliplaydemo.activity.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.jcmels.liba.piliplaydemo.ExtConstants;
import com.jcmels.liba.piliplaydemo.data.DataServer;
import com.jcmels.liba.piliplaydemo.ui.LoadingDialog;
import com.jcmels.liba.piliplaydemo.bean.PiliStatusBean;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.ui.RoomUserDetailsDialog;
import com.jcmels.liba.piliplaydemo.ui.widget.BarrageLayout;
import com.jcmels.liba.piliplaydemo.ui.widget.LiveLeftGiftView;
import com.jcmels.liba.piliplaydemo.ui.widget.PeriscopeLayout;
import com.jcmels.liba.piliplaydemo.ui.widget.RoomMessagesView;
import com.jcmels.liba.piliplaydemo.utils.Utils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PLVideoViewActivity extends AppCompatActivity implements
        PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    private com.pili.pldroid.player.widget.PLVideoView PLVideoView;
    private String playurl;
    private AVOptions options;
    private DataServer dataServer;
    private LoadingDialog dialog;
    private com.facebook.drawee.view.SimpleDraweeView apvsimpleDraweeView;
    private String streamKey;
    private Button btn_back;
    private TextView tv_roomname;

    //集成聊天室
//    private LiveLeftGiftView leftGiftView;
//    private LiveLeftGiftView leftGiftView2;
//    private RelativeLayout root_layout;
//    private PeriscopeLayout periscopeLayout;
//    private RoomMessagesView messageView;
//    private View bottomBar;
//    private BarrageLayout barrageLayout;
//    private RecyclerView horizontalRecyclerView;
//    private ImageView newMsgNotifyImage;
//    //环信聊天室房间号
//    protected String chatroomId = "";
//    protected boolean isMessageListInited;
//    protected EMChatRoomChangeListener chatRoomChangeListener;
//    volatile boolean isGiftShowing = false;
//    volatile boolean isGift2Showing = false;
//    List<String> toShowList = Collections.synchronizedList(new LinkedList<String>());
//
//    protected EMChatRoom chatroom;
//    List<String> memberList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_plvideo_view);
        EventBus.getDefault().register(this);
        //环信
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

       // chatroomId = "272848939265294872";







        Intent intent = getIntent();
        streamKey = intent.getStringExtra("streamkey");
        dataServer = new DataServer();
        checkStreamInfo(streamKey);


        playurl = intent.getStringExtra("playurl");

        initView();
        String roomname = intent.getStringExtra("roomname");
        tv_roomname.setText(roomname);
        PLVideoView.setVisibility(View.GONE);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        PLVideoView = (com.pili.pldroid.player.widget.PLVideoView) findViewById(R.id.PLVideoView);
        apvsimpleDraweeView = (SimpleDraweeView) findViewById(R.id.apv_simpleDraweeView);
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_roomname = (TextView) findViewById(R.id.tv_roomname);
//        root_layout = (RelativeLayout) findViewById(R.id.activity_plvideo_view);
//        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope_layout);
//        root_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                periscopeLayout.addHeart();
//            }
//        });
//
//        leftGiftView= (LiveLeftGiftView) findViewById(R.id.left_gift_view1);
//        leftGiftView2= (LiveLeftGiftView) findViewById(R.id.left_gift_view2);
//        messageView= (RoomMessagesView) findViewById(R.id.message_view);
//        bottomBar=findViewById(R.id.bottom_bar);
//        bottomBar.findViewById(R.id.comment_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInputView();
//            }
//        });
//        bottomBar.findViewById(R.id.present_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.CMD);
//                message.setReceipt(chatroomId);
//                EMCmdMessageBody cmdMessageBody = new EMCmdMessageBody(ExtConstants.CMD_GIFT);
//                message.addBody(cmdMessageBody);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                EMClient.getInstance().chatManager().sendMessage(message);
//                showLeftGiftVeiw(EMClient.getInstance().getCurrentUser());
//            }
//        });




    }

    private void initPlayer(String playurl) {
        options = new AVOptions();


// 解码方式，codec＝1，硬解; codec=0, 软解
// 默认值是：0
        options.setInteger(AVOptions.KEY_MEDIACODEC, 1);

// 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
// 默认值是：无
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

// 读取视频流超时时间，单位是 ms
// 默认值是：10 * 1000
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

// 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
// 默认值是：0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);

// 是否开启"延时优化"，只在在线直播流中有效
// 默认值是：0
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

// 默认的缓存大小，单位是 ms
// 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

// 最大的缓存大小，单位是 ms
// 默认值是：4000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

// 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
// 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);
        PLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        PLVideoView.setAVOptions(options);
        PLVideoView.setVideoPath(playurl);
        PLVideoView.setOnPreparedListener(this);
        PLVideoView.setOnInfoListener(this);
        PLVideoView.setOnCompletionListener(this);
        PLVideoView.setOnVideoSizeChangedListener(this);
        PLVideoView.setOnErrorListener(this);
    }

    private void checkStreamInfo(String streamKey) {
        dataServer.getLiveInfo(streamKey);
        dialog = new LoadingDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        checkStreamInfo(streamKey);
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        switch (i) {
            case PLMediaPlayer.ERROR_CODE_IO_ERROR: {
                Toast.makeText(this, "直播已结束", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT: {
                Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED: {
                Toast.makeText(this, "连接不上服务器", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        dialog.dismiss();
//        EMClient.getInstance().chatroomManager().joinChatRoom(chatroomId, new EMValueCallBack<EMChatRoom>() {
//            @Override
//            public void onSuccess(EMChatRoom emChatRoom) {
//                Toast.makeText(PLVideoViewActivity.this, "加入聊天室成功", Toast.LENGTH_SHORT).show();
//                chatroom = emChatRoom;
//                addChatRoomChangeListenr();
//                onMessageListInit();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Toast.makeText(PLVideoViewActivity.this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!isFinishing()) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            periscopeLayout.addHeart();
//                        }
//                    });
//                    try {
//                        Thread.sleep(new Random().nextInt(400) + 200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
        Toast.makeText(this, "准备好了!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        PLVideoView.start();

//        //环信
//        if (isMessageListInited)
//            messageView.refresh();
//        EaseUI.getInstance().pushActivity(this);
//        // register the event listener when enter the foreground
//        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PLVideoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PLVideoView.stopPlayback();

//        //环信
//        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
//
//        // 把此activity 从foreground activity 列表里移除
//        EaseUI.getInstance().popActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

//        //环信
//        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomId);
//        if (chatRoomChangeListener != null) {
//            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPiliStatus(PiliStatusBean pilistatus) {
        switch (pilistatus.getStatus()) {
            case "200": {
                initPlayer(playurl);
                PLVideoView.setVisibility(View.VISIBLE);
            }
            break;
            case "404": {
                Toast.makeText(this, "主播离开了！", Toast.LENGTH_SHORT).show();
                PLVideoView.setVisibility(View.GONE);
                apvsimpleDraweeView.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
            break;
        }

    }


//    //==============================================================
//    //整合环信
//    //==============================================================
//
//
//    //==============================================================
//    //礼物展示
//    //==============================================================
//    protected synchronized void showLeftGiftVeiw(String name) {
//        if (!isGift2Showing) {
//            showGift2Derect(name);
//        } else if (!isGiftShowing) {
//            showGift1Derect(name);
//        } else {
//            toShowList.add(name);
//        }
//    }
//
//    private void showGift1Derect(final String name) {
//        isGiftShowing = true;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                leftGiftView.setVisibility(View.VISIBLE);
//                leftGiftView.setName(name);
//                leftGiftView.setTranslationY(0);
//                ViewAnimator.animate(leftGiftView)
//                        .alpha(0, 1)
//                        .translationX(-leftGiftView.getWidth(), 0)
//                        .duration(600)
//                        .thenAnimate(leftGiftView)
//                        .alpha(1, 0)
//                        .translationY(-1.5f * leftGiftView.getHeight())
//                        .duration(800)
//                        .onStop(new AnimationListener.Stop() {
//                            @Override
//                            public void onStop() {
//                                String pollName = null;
//                                try {
//                                    pollName = toShowList.remove(0);
//                                } catch (Exception e) {
//
//                                }
//                                if (pollName != null) {
//                                    showGift1Derect(pollName);
//                                } else {
//                                    isGiftShowing = false;
//                                }
//                            }
//                        })
//                        .startDelay(2000)
//                        .start();
//                ViewAnimator.animate(leftGiftView.getGiftImageView())
//                        .translationX(-leftGiftView.getGiftImageView().getX(), 0)
//                        .duration(1100)
//                        .start();
//            }
//        });
//    }
//
//    private void showGift2Derect(final String name) {
//        isGift2Showing = true;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                leftGiftView2.setVisibility(View.VISIBLE);
//                leftGiftView2.setName(name);
//                leftGiftView2.setTranslationY(0);
//                ViewAnimator.animate(leftGiftView2)
//                        .alpha(0, 1)
//                        .translationX(-leftGiftView2.getWidth(), 0)
//                        .duration(600)
//                        .thenAnimate(leftGiftView2)
//                        .alpha(1, 0)
//                        .translationY(-1.5f * leftGiftView2.getHeight())
//                        .duration(800)
//                        .onStop(new AnimationListener.Stop() {
//                            @Override
//                            public void onStop() {
//                                String pollName = null;
//                                try {
//                                    pollName = toShowList.remove(0);
//                                } catch (Exception e) {
//
//                                }
//                                if (pollName != null) {
//                                    showGift2Derect(pollName);
//                                } else {
//                                    isGift2Showing = false;
//                                }
//                            }
//                        })
//                        .startDelay(2000)
//                        .start();
//                ViewAnimator.animate(leftGiftView2.getGiftImageView())
//                        .translationX(-leftGiftView2.getGiftImageView().getX(), 0)
//                        .duration(1100)
//                        .start();
//            }
//        });
//    }
//
//    //===========================================
//    //聊天室监听
//    //===========================================
//    protected void addChatRoomChangeListenr() {
//        chatRoomChangeListener = new EMChatRoomChangeListener() {
//
//            @Override
//            public void onChatRoomDestroyed(String roomId, String roomName) {
//                if (roomId.equals(chatroomId)) {
//                    EMLog.e("EMC", " room : " + roomId + " with room name : " + roomName + " was destroyed");
//                }
//            }
//
//            @Override
//            public void onMemberJoined(String roomId, String participant) {
//                EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//                message.setReceipt(chatroomId);
//                message.setFrom(participant);
//                EMTextMessageBody textMessageBody = new EMTextMessageBody("来了");
//                message.addBody(textMessageBody);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                EMClient.getInstance().chatManager().saveMessage(message);
//                messageView.refreshSelectLast();
//
//                onRoomMemberAdded(participant);
//            }
//
//            @Override
//            public void onMemberExited(String roomId, String roomName, String participant) {
//                //                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
//                onRoomMemberExited(participant);
//            }
//
//            @Override
//            public void onMemberKicked(String roomId, String roomName, String participant) {
//                if (roomId.equals(chatroomId)) {
//                    String curUser = EMClient.getInstance().getCurrentUser();
//                    if (curUser.equals(participant)) {
//                        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
//                        Toast.makeText(PLVideoViewActivity.this, "你已被移除出此房间", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        //                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
//                        onRoomMemberExited(participant);
//                    }
//                }
//            }
//        };
//
//        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
//    }
//
//    //消息接收器，礼物弹幕通过拓展消息展现
//    EMMessageListener msgListener = new EMMessageListener() {
//
//        @Override
//        public void onMessageReceived(List<EMMessage> messages) {
//
//            for (EMMessage message : messages) {
//                String username = null;
//                // 群组消息
//                if (message.getChatType() == EMMessage.ChatType.GroupChat
//                        || message.getChatType() == EMMessage.ChatType.ChatRoom) {
//                    username = message.getTo();
//                } else {
//                    // 单聊消息
//                    username = message.getFrom();
//                }
//                // 如果是当前会话的消息，刷新聊天页面
//                if (username.equals(chatroomId)) {
//                    if (message.getBooleanAttribute(ExtConstants.EXTRA_IS_BARRAGE_MSG, false)) {
//                        barrageLayout.addBarrage(((EMTextMessageBody) message.getBody()).getMessage(),
//                                message.getFrom());
//                    }
//                    messageView.refreshSelectLast();
//                } else {
//                    if (message.getChatType() == EMMessage.ChatType.Chat && message.getTo().equals(EMClient.getInstance().getCurrentUser())) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                newMsgNotifyImage.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                    //// 如果消息不是和当前聊天ID的消息
//                    //EaseUI.getInstance().getNotifier().onNewMsg(message);
//                }
//            }
//        }
//
//        @Override
//        public void onCmdMessageReceived(List<EMMessage> messages) {
//            EMMessage message = messages.get(messages.size() - 1);
//            if (ExtConstants.CMD_GIFT.equals(((EMCmdMessageBody) message.getBody()).action())) {
//                showLeftGiftVeiw(message.getFrom());
//            }
//        }
//
//        @Override
//        public void onMessageReadAckReceived(List<EMMessage> messages) {
//            if (isMessageListInited) {
//                //                messageList.refresh();
//            }
//        }
//
//        @Override
//        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//            if (isMessageListInited) {
//                //                messageList.refresh();
//            }
//        }
//
//        @Override
//        public void onMessageChanged(EMMessage message, Object change) {
//            if (isMessageListInited) {
//                messageView.refresh();
//            }
//        }
//    };
//
//    protected void onMessageListInit() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                messageView.init(chatroomId);
//                messageView.setMessageViewListener(new RoomMessagesView.MessageViewListener() {
//                    @Override
//                    public void onMessageSend(String content) {
//                        EMMessage message = EMMessage.createTxtSendMessage(content, chatroomId);
//                        if (messageView.isBarrageShow) {
//                            message.setAttribute(ExtConstants.EXTRA_IS_BARRAGE_MSG, true);
//                            barrageLayout.addBarrage(content, EMClient.getInstance().getCurrentUser());
//                        }
//                        message.setChatType(EMMessage.ChatType.ChatRoom);
//                        EMClient.getInstance().chatManager().sendMessage(message);
//                        message.setMessageStatusCallback(new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                //刷新消息列表
//                                messageView.refreshSelectLast();
//                            }
//
//                            @Override
//                            public void onError(int i, String s) {
//                                Toast.makeText(PLVideoViewActivity.this, "消息发送失败！", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onProgress(int i, String s) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onItemClickListener(final EMMessage message) {
//                        //if(message.getFrom().equals(EMClient.getInstance().getCurrentUser())){
//                        //    return;
//                        //}
//                        String clickUsername = message.getFrom();
//                        showUserDetailsDialog(clickUsername);
//                    }
//
//                    @Override
//                    public void onHiderBottomBar() {
//                        bottomBar.setVisibility(View.VISIBLE);
//                    }
//                });
//                messageView.setVisibility(View.VISIBLE);
//                bottomBar.setVisibility(View.VISIBLE);
//                isMessageListInited = true;
//                updateUnreadMsgView();
////                showMemberList();
//            }
//        });
//    }
//
//    protected void updateUnreadMsgView() {
//        if (isMessageListInited) {
//            for (EMConversation conversation : EMClient.getInstance()
//                    .chatManager()
//                    .getAllConversations()
//                    .values()) {
//                if (conversation.getType() == EMConversation.EMConversationType.Chat
//                        && conversation.getUnreadMsgCount() > 0) {
//                    newMsgNotifyImage.setVisibility(View.VISIBLE);
//                    return;
//                }
//            }
//            newMsgNotifyImage.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    //展示用户
//    private void showUserDetailsDialog(String username) {
//        final RoomUserDetailsDialog dialog =
//                RoomUserDetailsDialog.newInstance(username);
//        dialog.setUserDetailsDialogListener(
//                new RoomUserDetailsDialog.UserDetailsDialogListener() {
//                    @Override
//                    public void onMentionClick(String username) {
//                        dialog.dismiss();
//                        messageView.getInputView().setText("@" + username + " ");
//                        showInputView();
//                    }
//                });
//        dialog.show(getSupportFragmentManager(), "RoomUserDetailsDialog");
//    }
//
//    //弹出输入框
//    private void showInputView() {
//        bottomBar.setVisibility(View.INVISIBLE);
//        messageView.setShowInputView(true);
//        messageView.getInputView().requestFocus();
//        messageView.getInputView().requestFocusFromTouch();
//
//        //IF CARRY RUN ON EventBus
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Utils.showKeyboard(messageView.getInputView());
//                    }
//                });
//            }
//        }).start();
//    }
//
////展示成员列表
////    void showMemberList() {
////        LinearLayoutManager layoutManager = new LinearLayoutManager(PLVideoViewActivity.this);
////        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
////        horizontalRecyclerView.setLayoutManager(layoutManager);
////        horizontalRecyclerView.setAdapter(new AvatarAdapter(PLVideoViewActivity.this, memberList));
////        new Thread(new Runnable() {
////            @Override public void run() {
////                try {
////                    chatroom =
////                            EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(chatroomId, true);
////                    memberList.addAll(chatroom.getMemberList());
////                } catch (HyphenateException e) {
////                    e.printStackTrace();
////                }
////                runOnUiThread(new Runnable() {
////                    @Override public void run() {
////                        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
////                    }
////                });
////            }
////        }).start();
////    }
//
//    //成员加入
//    private void onRoomMemberAdded(String name) {
//        if (!memberList.contains(name)) memberList.add(name);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
//            }
//        });
//    }
//
//    //成员推出
//    private void onRoomMemberExited(String name) {
//        memberList.remove(name);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
//            }
//        });
//    }
//
//
////    private class AvatarAdapter extends RecyclerView.Adapter<AvatarViewHolder> {
////        List<String> namelist;
////        Context context;
////
////
////        public AvatarAdapter(Context context, List<String> namelist) {
////            this.namelist = namelist;
////            this.context = context;
////
////        }
////
////        @Override public AvatarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            return new AvatarViewHolder(
////                    LayoutInflater.from(context).inflate(R.layout.avatar_list_item, parent, false));
////        }
////
////        @Override public void onBindViewHolder(AvatarViewHolder holder, final int position) {
////            holder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override public void onClick(View v) {
////                    showUserDetailsDialog(namelist.get(position));
////                }
////            });
////
////        }
////
////        @Override public int getItemCount() {
////            return namelist.size();
////        }
////    }
////
////    static class AvatarViewHolder extends RecyclerView.ViewHolder {
////        @BindView(R.id.avatar) ImageView Avatar;
////
////        public AvatarViewHolder(View itemView) {
////            super(itemView);
////            ButterKnife.bind(this, itemView);
////        }
////    }

}




































