package com.example.yukun.youdaotrans.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by yukun on 16-11-23.
 */
public class ParaTs {
    ArrayList<String> tsString=new ArrayList<>();
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> timeString=new ArrayList<>();
    ArrayList<String> timeAll=new ArrayList<>();

    String MainURI="http://qv.gaiamount.com";
//     21 服务器上的ts片地址
//    String MainURI="http://stqnvod.gaiamount.com";
    String string;
    private StringBuilder builder;
    private String headTs;
    private static String tsKey;
    private static String ivs;
    private String keyUri;
    private String videoRato;

    public void setStrings(String string) {
        this.string = string;

        parasString();
    }

    public ArrayList<String> getTimes() {
        return timeString;
    }

    public String getHeadTs() {
        return headTs;
    }

    public static String getTsKey() {
        return tsKey;
    }

    public String getVideoRato() {
        return videoRato;
    }

    public void parasString() {

        String[] split = string.split("#");
        builder = new StringBuilder();
        for (int i = 1; i < 6; i++) {
            builder.append("#"+split[i]);
        }

        String[] split3 = split[6].split(",");
        ivs=split3[2].substring(3,split3[2].length());
        headTs = new String(builder);
        tsKey=split3[1].substring(5,split3[1].length()-1);

        //获取分辨录
        videoRato = tsKey.substring(tsKey.lastIndexOf("/") + 1, tsKey.length());
        Log.i("-----videoRatio", videoRato);
        haveFile();

        for (int i = 7; i < split.length - 1; i++) {
            //解析M3u8片
            String[] split1 = split[i].split(",");
            int i1 = split1[1].lastIndexOf("/");
            timeAll.add(split1[0].split(":")[1]);
            timeString.add(split1[0]);
            String substring = split1[1].substring(i1 + 1, split1[1].length());
            names.add(substring);
            int i2 = split1[1].indexOf("/");
            tsString.add(MainURI+split1[1].substring(i2,split1[1].length()).trim());
        }
        Log.i("-----timesall",timeAll.toString());
    }

    public ArrayList<String> getTimeAll() {
        return timeAll;
    }

    public ArrayList<String> returnTsUri(){
        return tsString;
    }

    public ArrayList<String> returnTsName(){
        return names;
    }
    public static String getTsIV(){
        return  ivs;
    }

    public synchronized void writeM3U8(String myreadline) {
        try{

            FileOutputStream fout =new FileOutputStream(new File(StringUtils.TSPATH+"/test.m3u8"),true);

            byte [] bytes = myreadline.getBytes();

            fout.write(bytes);
            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void haveFile() {
        File dir = new File(StringUtils.TSPATH);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File dir_1 = new File(StringUtils.TSPATH+"/test.m3u8");
        if (!dir_1.exists()) {
            try {
                //在指定的文件夹创建文件
                dir.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void splitTs(int number){
        //拼接中间的ts
        String builder=("#"+timeString.get(number)+","+"\n");
        builder=builder+(names.get(number)+"");
        writeM3U8((builder));

        //拼接最后一片
        if(number==tsString.size()-1){
            String builder2="#EXT-X-ENDLIST";
            writeM3U8(builder2);
        }
    }
}
