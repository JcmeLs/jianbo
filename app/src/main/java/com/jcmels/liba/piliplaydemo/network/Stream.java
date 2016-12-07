package com.jcmels.liba.piliplaydemo.network;


import com.jcmels.liba.piliplaydemo.bean.BaseRequestBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by LIBA on 11/10/2016.
 */

public interface Stream {
    @GET("streams")
    Call<BaseRequestBean> getStreams();

    @POST("users/mobile/register")
    @FormUrlEncoded
    Call<BaseRequestBean> register(@Field("username")String username,@Field("password")String password,@Field("email")String emial);

    @POST("users/mobile/login")
    @FormUrlEncoded
    Call<BaseRequestBean> login(@Field("username")String username,@Field("password")String password);


    @GET("live/info")
    Call<BaseRequestBean> getStreamInfo();

    @POST("stream/piliroomname")
    @FormUrlEncoded
    Call<BaseRequestBean> updateRoomName(@Field("streamkey")String streamkey,@Field("piliroomname")String piliroomname);
}
