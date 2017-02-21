package com.example.yukun.youdaotrans;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.yukun.youdaotrans.util.DownloadTs;
import com.example.yukun.youdaotrans.util.EventInitView;
import com.example.yukun.youdaotrans.util.EventPosition;
import com.example.yukun.youdaotrans.util.FileDele;
import com.example.yukun.youdaotrans.util.M3U8Service;
import com.example.yukun.youdaotrans.util.NetUtil;

import com.example.yukun.youdaotrans.util.ParaTs;
import com.example.yukun.youdaotrans.util.VideoControl;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class M3U8Activity extends AppCompatActivity {
//    String uri="http://qv.gaiamount.com/playlist/u6100/v16347_2k.m3u8?e=1481018928&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:SnbQbEBQga7yC07ZwzbpgbxUX0U=";
//    String uri="http://qv.gaiamount.com/playlist/u10/v10171_720.m3u8";
    String uri="http://qv.gaiamount.com/playlist/u1638/v17474_fhd_src.m3u8?e=1480507318&amp;token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:emAAbGKplNXZluuypR2X29guOhg=";

    private VideoView videoView;
    private DownloadTs downloadTs;
    private ArrayList<String> names;
    private ArrayList<String> times;
    private ArrayList<Integer> downloadTag=new ArrayList<>();
    private ParaTs paraTs;
    private String uri_1;
    private SeekBar seekBar;
    private double maxTime=0; //视频的最大长度
    private ArrayList<String> timeAll;
    private int seekChange=0; //进度条的长度
    private int downLoadOver=3; //控制下载片数
    private int sprit_num=3;
    boolean tag=true; //控制最大下载量的tag
    int load_time=0;//删除头的控制
    int load_num=0;//下载头的控制
    int initView=0;//初始化播放器的控制
    private VideoControl videoControl;
    private ArrayList<String> tsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m3_u8);
        EventBus.getDefault().register(this);
        videoView = (VideoView) findViewById(R.id.videoview);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        M3U8Service.execute();
        videoControl = new VideoControl();
        String format = String.format("http://localhost:%d", M3U8Service.PORT);
        uri_1 = format+"/gaiamount/gaia/test.m3u8";
        listener();
        getUri(uri);
    }

    private void listener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pos, boolean b) {
                if(b){
                    Log.i("-------seekto",pos+"");
                    seekChange= pos;
                    load_num=pos/5;
                    initView=load_num;
                    load_time=pos/5;
                    sprit_num=load_num+3;
                    downloadTag.clear();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("-------seekto", "pause");
                if(videoView!=null&&videoView.isPlaying()){
                    videoView.stopPlayback();
                }

                FileDele.delete("test.m3u8");
                videoControl.setHandlerStop(1);
                videoControl.setHandlerStop(2);
                videoControl.setHandlerStop(3);

                FileDele.deleteAll();
                videoControl.setDownloadStop();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(videoView!=null){
                    videoView.stopPlayback();
                }
                FileDele.delete("test.m3u8");
                videoControl.writeHead();
                downLoadOver=3;
                Log.i("-------seekto","start");

            }
        });
    }


    private void initView() {

            //判断是否没有文件
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaiamount/gaia/test.m3u8");
            Log.i("--uri",file.exists()+"==="+file.toString());
            if(file.exists()){
                videoView.setVideoPath((uri_1));
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        seekBar.setProgress((int) maxTime);
                    }
                });

                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        Toast.makeText(M3U8Activity.this, "播放错误", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
        }
    }

    private String srt2;

    private String getUri(final String uri) {
        NetUtil.get(uri, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    srt2 = new String(responseBody,"UTF-8");
                    videoControl.setM3u8String(srt2);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
        return srt2;
    }

    //下载好了,初始化播放器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInitView(EventInitView event) {
        int play = event.play;
        initView();
    }

    boolean threadCon=true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final EventPosition event) {
        tsString = videoControl.getTsString();
        videoControl.setDownloadTag(event.position);//下载一次就加入一个值
        Log.i("----posit+initView+size",event.position+"+"+initView+"+"+ tsString.size());
        if(tsString.size()>3){
            if(event.position< tsString.size()){
                if(event.position==2+initView){
                    videoControl.setHandlerStart(1);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if(tsString.size()==2){
            if(event.position< tsString.size()){
                if(event.position==1+initView){
                    videoControl.setHandlerStart(2);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if(tsString.size()==3) {
            if (event.position < tsString.size()) {
                if (event.position == 1 + initView) {
                    videoControl.setHandlerStart(4);
                }
                videoControl.load();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoControl.setDownloadStop();
        videoControl.setHandlerStop(1);
        videoControl.setHandlerStop(2);
        videoControl.setHandlerStop(3);
        threadCon=false;
        if(downloadTs!=null){
            downloadTs.setThreadCon(false);
        }
        M3U8Service.finish();
        tag=false;
        EventBus.getDefault().unregister(this);
        if(videoView!=null&&videoView.isPlaying()){
            videoView.stopPlayback();
            videoView=null;
        }

        FileDele.delete("test.m3u8");
        FileDele.deleteAll();
    }

    public void pun(View view) {
        FileDele.deleteAll();
        if(videoView!=null){
            videoView.stopPlayback();
        }

        videoView.setVideoPath((uri));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }

    private String getUri_1( String uri) {
        NetUtil.get(uri, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    srt2 = new String(responseBody,"UTF-8");
                    Log.i("----srt2",srt2);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return srt2;
    }

    public void play(View view) {
        if(!videoView.isPlaying()&&videoView!=null){
            videoView.start();
        }
    }

    public void pause(View view) {
        if(videoView.isPlaying()||videoView!=null){
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FileDele.deleteAll();
    }
}
