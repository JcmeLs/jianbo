package com.jcmels.liba.piliplaydemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.BaseRequestBean;
import com.jcmels.liba.piliplaydemo.network.Stream;
import com.jcmels.liba.piliplaydemo.network.UriServer;
import com.jcmels.md5library.MD5;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private TextView tb_title;
    private Retrofit retrofit;
    private Stream stream;
    private com.rengwuxian.materialedittext.MaterialEditText etregusername;
    private com.rengwuxian.materialedittext.MaterialEditText etregemail;
    private com.rengwuxian.materialedittext.MaterialEditText etregpassword;
    private android.widget.Button btnregregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initRetrofit();

        tb_title.setText("注册");


        btnregregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etregusername.getText().toString();
                String email = etregemail.getText().toString();
                String password = MD5.MD5(etregpassword.getText().toString()).toLowerCase();
                register(username,password,email);
            }
        });

    }

    private void register(String username, String password, String email) {
        Call<BaseRequestBean> call=stream.register(username,password,email);
        call.enqueue(new Callback<BaseRequestBean>() {
            @Override
            public void onResponse(Call<BaseRequestBean> call, Response<BaseRequestBean> response) {
                Toast.makeText(RegisterActivity.this, response.body().getData()+"，请登录！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<BaseRequestBean> call, Throwable t) {

            }
        });
    }

    private void initRetrofit() {
        retrofit=new Retrofit.Builder()
                .baseUrl(UriServer.getBASEURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stream=retrofit.create(Stream.class);
    }

    private void initView() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        this.btnregregister = (Button) findViewById(R.id.btn_reg_register);
        this.etregpassword = (MaterialEditText) findViewById(R.id.et_reg_password);
        this.etregemail = (MaterialEditText) findViewById(R.id.et_reg_email);
        this.etregusername = (MaterialEditText) findViewById(R.id.et_reg_username);

    }
}
