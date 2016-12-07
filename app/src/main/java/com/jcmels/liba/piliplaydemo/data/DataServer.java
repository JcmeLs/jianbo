package com.jcmels.liba.piliplaydemo.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcmels.liba.piliplaydemo.bean.LiveStreamsDataBean;
import com.jcmels.liba.piliplaydemo.bean.PiliStatusBean;
import com.jcmels.liba.piliplaydemo.network.Stream;
import com.jcmels.liba.piliplaydemo.network.UriServer;
import com.jcmels.liba.piliplaydemo.bean.BaseRequestBean;
import com.jcmels.liba.piliplaydemo.bean.msgEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LIBA on 11/18/2016.
 */

public class DataServer {
   private Retrofit retrofit;
   private Stream stream;
    public void start_retrofit(){
        retrofit=new Retrofit.Builder()
                .baseUrl(UriServer.getBASEURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         stream=retrofit.create(Stream.class);
    }
    public void getLiveStreams(){
        final Gson gson=new Gson();
        Call<BaseRequestBean> call = stream.getStreams();
        call.enqueue(new Callback<BaseRequestBean>() {
            @Override
            public void onResponse(Call<BaseRequestBean> call, Response<BaseRequestBean> response) {
                System.out.println("==================");

                if(response.body().getResultcode().equals("200")) {

                    Type userType = new TypeToken<BaseRequestBean<LiveStreamsDataBean>>(){}.getType();
                    BaseRequestBean<LiveStreamsDataBean> dataBeanLiveStreamsBean=gson.fromJson(gson.toJson(response.body()),userType);
                    LiveStreamsDataBean liveStreamDataBean=dataBeanLiveStreamsBean.getData();
                    EventBus.getDefault().post(new LiveStreamsDataBean(liveStreamDataBean.getList()));
                }
                else {System.out.println("当前没有直播!!!!!!!!!!!!!!!");
                EventBus.getDefault().post(new msgEvent("当前没有直播,请稍后再试！"));

                }
                System.out.println("=========================");
            }

            @Override
            public void onFailure(Call<BaseRequestBean> call, Throwable t) {
                System.out.println("==================");
                t.printStackTrace();
                System.out.println("=========================");
            }
        });
    }
    public void getLiveInfo(String streamkey){
         Retrofit retrofit;
         Stream stream;
        retrofit=new Retrofit.Builder()
                .baseUrl(UriServer.getBASEURL()+"stream/"+streamkey+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stream=retrofit.create(Stream.class);
        final Gson gson=new Gson();
        Call<BaseRequestBean> call=stream.getStreamInfo();
        call.enqueue(new Callback<BaseRequestBean>() {
            @Override
            public void onResponse(Call<BaseRequestBean> call, Response<BaseRequestBean> response) {
                    EventBus.getDefault().post(new PiliStatusBean(response.body().getResultcode()));
            }
            @Override
            public void onFailure(Call<BaseRequestBean> call, Throwable t) {
                System.out.println("==================");
                t.printStackTrace();
                System.out.println("=========================");
            }
        });

    }



}
