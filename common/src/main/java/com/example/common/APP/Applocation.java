package com.example.common.APP;

import android.app.Application;
import android.os.SystemClock;

import java.io.File;

/**
 * Created by liuzhen on 2019/10/12
 */
public class Applocation extends Application {



    //单例？
    private static Applocation instance;

    public static Applocation getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }



    /**
     * 获取缓存文件夹地址
     * @return
     */
    public static File getCaheDirFile(){
        return instance.getCacheDir();
    }
    public static File getPortaitTmpFile(){
        File dir = new File(getCaheDirFile(),"portrait");
        dir.mkdirs();

        File[] files = dir.listFiles();
        if (files != null && files.length > 0){
            for (File file : files){
                file.delete();
            }
        }
        // 返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis()+".jpg");
        return path.getAbsoluteFile();
    }
}
