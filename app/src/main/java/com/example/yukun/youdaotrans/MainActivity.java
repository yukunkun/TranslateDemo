package com.example.yukun.youdaotrans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yukun.youdaotrans.util.NetUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private String search;
    private String API_KEY="1304642984";
    private String keyfrom="TransApplication";
    private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
    private String DaoType = "data";
    private String DaoDoctype = "json";
    private String DaoVersion = "1.1";
    private TextView textViewCon;

//    API key：1304642984
//    keyfrom：TransApplication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    private void init() {
        editText = (EditText) findViewById(R.id.edittext);
        textView = (TextView) findViewById(R.id.search);
        textViewCon = (TextView) findViewById(R.id.text);
    }

    private void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
    }

    private void Search() {

        //拼接地址
        search = editText.getText().toString().trim();

        String DaoUrl = YouDaoBaseUrl + "?keyfrom=" + keyfrom
                + "&key=" + API_KEY + "&type=" + DaoType + "&doctype="
                + DaoDoctype + "&type=" + DaoType + "&version="
                + DaoVersion + "&q=" + search;        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        getUri(DaoUrl);

    }

    //网络请求
    private void getUri(String daoUrl) {
        NetUtil.get(daoUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String srt2=new String(responseBody,"UTF-8");
                    textViewCon.setText(srt2);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    public void BaiduTrans(View view) {
        Intent intent=new Intent(this,BaiDuTransActivity.class);
        startActivity(intent);
    }

    public void QQPop(View view) {
        Intent intent=new Intent(this, com.example.yukun.youdaotrans.floatwindowdemo.MainActivity.class);
        startActivity(intent);
    }

    public void M3U8(View view) {
        Intent intent=new Intent(this, M3U8Activity.class);
        startActivity(intent);
    }
}
