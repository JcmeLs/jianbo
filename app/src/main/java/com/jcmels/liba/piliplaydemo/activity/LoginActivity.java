package com.jcmels.liba.piliplaydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jcmels.liba.piliplaydemo.PiliPlayApplication;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.BaseRequestBean;
import com.jcmels.liba.piliplaydemo.bean.UserInfoBean;
import com.jcmels.liba.piliplaydemo.bean.UserInfosBean;
import com.jcmels.liba.piliplaydemo.network.Stream;
import com.jcmels.liba.piliplaydemo.network.UriServer;
import com.jcmels.md5library.MD5;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private com.rengwuxian.materialedittext.MaterialEditText etloginusername;
    private com.rengwuxian.materialedittext.MaterialEditText etloginpassword;
    private android.widget.Button btnlogin;
    private TextView tb_title;
    private TextView tb_text;
    private Retrofit retrofit;
    private Stream stream;
    private Boolean IsEMLogin=false;
    private Boolean IsServerLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initRetrofit();

        tb_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etloginusername.getText().toString().trim();
                String password = MD5.MD5(etloginpassword.getText().toString().trim()).toLowerCase();
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        Log.d("LoginActivity", "login server");
        loginEM(username,password);
        Log.d("LoginActivity", "login server");
        Call<BaseRequestBean> call = stream.login(username, password);
        call.enqueue(new Callback<BaseRequestBean>() {
            @Override
            public void onResponse(Call<BaseRequestBean> call, Response<BaseRequestBean> response) {
                Gson gson = new Gson();
                if (response.body().getResultcode().equals("200")) {
                    Type userType = new TypeToken<BaseRequestBean<UserInfosBean>>() {
                    }.getType();
                    BaseRequestBean<UserInfosBean> loginData = gson.fromJson(gson.toJson(response.body()), userType);
                    UserInfoBean userInfo = loginData.getData().getList();
                    PiliPlayApplication piliPlayApplication = (PiliPlayApplication) getApplication();
                    piliPlayApplication.userInfo = userInfo;
                    EventBus.getDefault().post(userInfo);
                    IsServerLogin=true;
                    if(IsEMLogin&&IsServerLogin)
                    finish();
                } else if (response.body().getResultcode().equals("404")) {
                    Toast.makeText(LoginActivity.this, "账户不存在，请核对后输入！", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResultcode().equals("500")) {
                    Toast.makeText(LoginActivity.this, "密码错误，请核对后输入！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseRequestBean> call, Throwable t) {

            }
        });
    }
    private void loginEM(String username, String password) {
        Log.d("LoginActivity", "login em");
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                IsEMLogin=true;
                if(IsEMLogin&&IsServerLogin)
                finish();

            }

            @Override
            public void onError(int i, String s) {
                Log.e("LoginActivity", s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(UriServer.getBASEURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stream = retrofit.create(Stream.class);
    }

    private void initData() {
        tb_text.setText("注册");
        tb_title.setText("登录");
    }

    private void initView() {
        tb_text = (TextView) findViewById(R.id.tb_text);
        tb_title = (TextView) findViewById(R.id.tb_title);
        btnlogin = (Button) findViewById(R.id.btn_login);
        etloginpassword = (MaterialEditText) findViewById(R.id.et_login_password);
        etloginusername = (MaterialEditText) findViewById(R.id.et_login_username);
    }
}
