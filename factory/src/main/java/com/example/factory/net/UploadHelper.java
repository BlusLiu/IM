package com.example.factory.net;


import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.factory.Factory;
import com.example.utils.HashUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by liuzhen on 2019/10/13
 * For ali_oss
 */
public class UploadHelper {
    private static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String BUCKET_NAME = "liu-im";
    private static final String TAG = "UploadHelper";

    private static OSS getClient(){
        //String stsServer = "STS应用服务器地址，例如http://abc.com"

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI4FcEsA7dh2eyo5ayWbjD", "F5SrxWxDdqttfqu1GvIb5iSekFisLq");
        // 不同的验证请求需要的参数不同

        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);

        //return oss;
    }

    private static String upload(String objKEY, String path){
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKEY, path);

        try{
            OSS oss = getClient();
                PutObjectResult result = oss.putObject(request);

                String url = oss.presignPublicObjectURL(BUCKET_NAME, objKEY);
                Log.d(TAG, "upload: "+url);
                return url;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String uploadImage(String path){
        String key = getImageObjkey(path);
        return upload(key, path);
    }

    public static String uploadPortrait(String path){
        String key = getPortraitObjkey(path);
        return upload(key, path);
    }

    public static String uploadAudio(String path){
        String key = getAudioObjkey(path);
        return upload(key, path);
    }

    private static String getDateString(){

        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");

        return dateFormat.format(date).toString();

    }

    private static String getImageObjkey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg",dateString,fileMd5);
    }

    private static String getPortraitObjkey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("Portrait/%s/%s.jpg",dateString,fileMd5);
    }

    private static String getAudioObjkey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("Audio/%s/%s.mp3",dateString,fileMd5);

    }
}
