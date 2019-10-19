package com.example.common.APP;

import android.app.Application;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;


import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.StringRes;


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

    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        // Toast 只能在主线程中显示，所有需要进行线程转换，
        // 保证一定是在主线程进行的show操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递的是字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }
}
