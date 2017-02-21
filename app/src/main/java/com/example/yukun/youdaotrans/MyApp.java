package com.example.yukun.youdaotrans;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by yukun on 16-12-9.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
//                .readTimeout(15000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor(""))
//                .hostnameVerifier(new HostnameVerifier()
//                {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session)
//                    {
//                        return true;
//                    }
//                })
//                .build();
//        OkHttpUtils.initClient(okHttpClient);
    }
}
