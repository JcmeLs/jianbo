package com.jcmels.liba.piliplaydemo.activity.pili;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zhouwei.library.CustomPopWindow;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.PiliStatus;
import com.jcmels.liba.piliplaydemo.ui.CameraPreviewFrameView;
import com.jcmels.liba.piliplaydemo.ui.RotateLayout;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;

import static android.content.ContentValues.TAG;

public class HWCameraStreamingActivity extends Activity implements StreamingStateChangedListener, CameraPreviewFrameView.Listener {
    private MediaStreamingManager streamingManager;
    private StreamingProfile streamingProfile;
    private MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    private Button btn_change;
    private CameraStreamingSetting setting;
    protected boolean mIsReady = false;
    private RotateLayout mRotateLayout;
    private Button btn_flashes;
    private Boolean IsTurnFlashes = false;
    private Boolean IsStreamStart = false;
    private Boolean IsMute = false;
    private Button btn_startstream;
    private Button btn_mute;
    private TextView tv_status;
    private Button btn_Screenshooter;
    private Screenshooter screenshooter;
    private Button btn_beauty;
    private SeekBar sb_beautylevel;
    private SeekBar sb_white;
    private SeekBar sb_red;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private int mCurrentCamFacingIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hwcamera_streaming);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);
        CameraPreviewFrameView cameraPreviewFrameView =
                (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(this);
        initView();
        screenshooter = new Screenshooter();
        String publishurl = intent.getStringExtra("piliurl");
        System.out.println(publishurl);
        streamingProfile = new StreamingProfile();

        try {
            streamingProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH1)
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
//                .setPreferredVideoEncodingSize(960, 544)
                    .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_480)
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY)
//                .setAVProfile(avProfile)
                    .setDnsManager(getMyDnsManager())
                    .setAdaptiveBitrateEnable(true)
                    .setFpsControllerEnable(true)
                    .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
                    .setPublishUrl(publishurl)
//                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                    .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));
            setting = new CameraStreamingSetting();
            setting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                    .setContinuousFocusModeEnabled(true)
                    .setResetTouchFocusDelayInMs(3000)
                    .setBuiltInFaceBeautyEnabled(true)
                    .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                    .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

            btn_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchCamera();

                }
            });
            //水印
            // WatermarkSetting watermarksetting = new WatermarkSetting(this, R.drawable.pili, WatermarkSetting.WATERMARK_LOCATION.SOUTH_WEST, WatermarkSetting.WATERMARK_SIZE.MEDIUM, 100); // 100 为 alpha 值
//watermarksetting.setCustomPosition(0.5f,0.5f);
            streamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView,
                    AVCodecType.HW_VIDEO_WITH_HW_AUDIO_CODEC); // hw codec  // soft codec
            mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
            mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);
            streamingManager.prepare(setting, mMicrophoneStreamingSetting, null, streamingProfile);
            streamingManager.setStreamingStateListener(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        btn_flashes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsTurnFlashes) {
                    streamingManager.turnLightOn();
                    streamingManager.mute(true);
                    IsTurnFlashes = true;
                } else {
                    streamingManager.turnLightOff();
                    IsTurnFlashes = false;
                }
            }
        });
        btn_startstream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsStreamStart) {
                    streamingManager.stopStreaming();
                    IsStreamStart = false;
                    setShutterButtonPressed(IsStreamStart);
                } else {
                    streamingManager.startStreaming();
                    IsStreamStart = true;
                    setShutterButtonPressed(IsStreamStart);
                }
            }
        });
        btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsMute) {
                    streamingManager.mute(false);
                    IsMute = false;
                    btn_mute.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unmute));
                    Toast.makeText(HWCameraStreamingActivity.this, "关闭禁音模式！", Toast.LENGTH_SHORT).show();
                } else {
                    streamingManager.mute(true);
                    IsMute = true;
                    btn_mute.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_mute));
                    Toast.makeText(HWCameraStreamingActivity.this, "开启禁音模式！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Screenshooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshooter.run();
            }
        });

        View contentView = LayoutInflater.from(this).inflate(R.layout.beauty_seekbar_layout,null);
        final CustomPopWindow popWindow=new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setOutsideTouchable(true)
                .setFocusable(true)
                .create();
        sb_beautylevel= (SeekBar) contentView.findViewById(R.id.beautyLevel_seekbar);
        sb_white= (SeekBar) contentView.findViewById(R.id.whiten_seekbar);
        sb_red= (SeekBar) contentView.findViewById(R.id.redden_seekBar);
      sb_beautylevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              CameraStreamingSetting.FaceBeautySetting fbsetting = setting.getFaceBeautySetting();
              fbsetting.beautyLevel = (progress*25) / 100.0f;
              streamingManager.updateFaceBeautySetting(fbsetting);
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });
        sb_white.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbsetting = setting.getFaceBeautySetting();
                fbsetting.whiten = (progress *25)/ 100.0f;
                streamingManager.updateFaceBeautySetting(fbsetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbsetting = setting.getFaceBeautySetting();

                fbsetting.redden = (progress*25) / 100.0f;
                streamingManager.updateFaceBeautySetting(fbsetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.showAsDropDown(btn_beauty,0,- ((btn_beauty.getHeight() + popWindow.getHeight()+50)));
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    private void initView() {
        btn_change = (Button) findViewById(R.id.btn_change);
        btn_flashes = (Button) findViewById(R.id.btn_flashes);
        btn_startstream = (Button) findViewById(R.id.btn_startstream);
        btn_mute = (Button) findViewById(R.id.btn_mute);
        tv_status = (TextView) findViewById(R.id.tv_status);
        btn_Screenshooter = (Button) findViewById(R.id.btn_Screenshooter);
        btn_beauty= (Button) findViewById(R.id.btn_beauty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        streamingManager.resume();
    }


    //手动对焦

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());

        if (mIsReady) {
            setFocusAreaIndicator();
            streamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            streamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }


    //切换摄像头
    private void switchCamera() {
        mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
        CameraStreamingSetting.CAMERA_FACING_ID facingId;
        if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        }
        Log.i(TAG, "switchCamera:" + facingId);
        streamingManager.switchCamera(facingId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // You must invoke pause here.
        streamingManager.pause();
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object o) {
        switch (streamingState) {
            case PREPARING:
                EventBus.getDefault().post(new PiliStatus("准备中..."));
                break;
            case READY:
                EventBus.getDefault().post(new PiliStatus("就绪"));
                mIsReady = true;
                // start streaming when READY
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (streamingManager != null) {
                            streamingManager.startStreaming();
                            IsStreamStart = true;
                            setShutterButtonPressed(IsStreamStart);
                        }
                    }
                }).start();
                break;
            case CONNECTING:
                break;
            case STREAMING:
                EventBus.getDefault().post(new PiliStatus("直播中..."));
                // The av packet had been sent.
                setShutterButtonPressed(true);
                break;
            case SHUTDOWN:
                EventBus.getDefault().post(new PiliStatus("直播停止"));
                // The streaming had been finished.
                IsStreamStart = false;
                break;
            case IOERROR:
                EventBus.getDefault().post(new PiliStatus("IO出错"));
                // Network connect error.
                IsStreamStart = false;
                break;
            case SENDING_BUFFER_EMPTY:
                break;
            case SENDING_BUFFER_FULL:
                break;
            case AUDIO_RECORDING_FAIL:
                // Failed to record audio.
                break;
            case OPEN_CAMERA_FAIL:
                // Failed to open camera.
                break;
            case DISCONNECTED:
                EventBus.getDefault().post(new PiliStatus("网络中断"));
                // The socket is broken while streaming
                break;
        }
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {//
                btn_startstream.setPressed(pressed);
            }
        });
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }


    @Override
    public boolean onZoomValueChanged(float factor) {
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatusChangeEvent(PiliStatus piliStatus) {
        tv_status.setText(piliStatus.getPiliStatus());
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "图片保存成功，图片目录:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(HWCameraStreamingActivity.this, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private class Screenshooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            streamingManager.captureFrame(1080, 1920, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }
}
