package com.example.yukun.youdaotrans.util;

import android.util.Log;

import java.io.File;

/**
 * Created by yukun on 16-11-29.
 */
public class FileDele {
    //删除所有文件
    public static void deleteAll(){
        File file=new File(StringUtils.TSPATH);
        if (file.isDirectory()) {// 处理目录
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {

                if(!file.renameTo(file)) {
                    System.out.println("正在被别人使用中.");
                }else {
                    files[i].delete();
                }
                Log.i("------deleteAll",files[i]+"");
            }
        }
    }

    //删除单个文件
    public static void delete(String path){
        File file=new File(StringUtils.TSPATH+"/"+path.trim());
        if(file.exists()){       // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                boolean delete = file.delete(); // delete()方法 你应该知道是删除的意思;
            }
            Log.i("------delete",path+"==");
        }
    }
}
