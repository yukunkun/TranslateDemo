package com.example.yukun.youdaotrans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yukun.youdaotrans.util.MD5;
import com.example.yukun.youdaotrans.util.NetUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class BaiDuTransActivity extends AppCompatActivity {
//    APP ID: 20161108000031490
//
//    密钥: 596n46mg38iwVK7hiwPZ
    private String APP_ID="20161108000031490";
    private String Secret="596n46mg38iwVK7hiwPZ";
    private String baiDuUri="http://api.fanyi.baidu.com/api/trans/vip/translate?";
    private EditText editText;
    private TextView textView;
    private String search;
    private TextView textViewCon;
    private Spinner spinner;
    private String[] lan;
    private String lanage="en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_trans);
        init();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        String [] strings=new String[]{"zh_en(英语)","zh_kor(韩语)","zh_fra(法语)","zh_pt(葡萄牙)","zh_dan(丹麦)","zh_swe(瑞典)"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,strings);
        spinner.setAdapter(arrayAdapter);
    }

    private void init() {
        lan = new String[]{"en","kor","fra","pt","dan","swe"};
        editText = (EditText) findViewById(R.id.edittext);
        textView = (TextView) findViewById(R.id.search);
        textViewCon = (TextView) findViewById(R.id.text);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    private void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lanage=lan[i];
                Search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Search() {
        //拼接地址BaiDuUri
        search = editText.getText().toString().trim();
//        q=apple&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
//        appid=2015063000000001+q=apple+salt=1435660288+密钥=12345678
        Random random=new Random(10);

        if(search==null||search.length()==0){
            return;
        }
        String salt=random.nextInt()+"";
        String st_1=APP_ID+search+salt+Secret;
        String sign=new MD5().toMD5(st_1);
        try {

            String q = URLEncoder.encode(search, "UTF-8");//utf-8 编码
            String BaiDuUri=baiDuUri+"q="+search+"&from=auto"+"&to="+lanage+"&appid="+APP_ID+"&salt="+salt+"&sign="+sign;
            getUri(BaiDuUri);

//            Log.i("---uri",BaiDuUri);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void getUri(String baiDuUri) {
        NetUtil.get(baiDuUri, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String srt2=new String(responseBody,"UTF-8");
                    if(srt2!=null&&srt2.length()>0){
                        JSONObject jsonObject=new JSONObject(srt2);
//                        Log.i("---date",srt2);
                        JSONArray jsonArray = jsonObject.optJSONArray("trans_result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                            String src = jsonObject1.optString("src");
                            String s = jsonObject1.optString("dst");// 翻译的结果
                            textViewCon.setText(s);
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String srt2= null;
//                try {
////                    srt2 = new String(responseBody,"UTF-8");
////                    textViewCon.setText(srt2);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
