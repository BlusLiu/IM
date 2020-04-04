package com.example.im2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.im2.activites.MainActivity;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 20:56 2020/4/4
 */

/**
 * 个推接收消息的IntentService，用以接收具体的消息信息
 * 替换之前老版本的消息广播
 */
public class MessageReceiverService extends GTIntentService {
    static final String TAG = "Message(gt)";

    @Override
    public void onReceiveServicePid(Context context, int i) {
        // 返回消息接收进程id，当前APP中就是AppPushService进程id
        Log.i(TAG, "onReceiveServicePid() called with: context = [" + context + "], i = [" + i + "]");
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        // 当设备成功在个推注册时返回个推唯一设备码
        Log.i(TAG, "onReceiveClientId() called with: context = [" + context + "], s = [" + s + "]");
        // 当Id初始化的时候
        // 获取设备Id
        onClientInit(s);
    }


    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        // 当接收到透传消息时回调，跟之前广播接收消息一样
        Log.i(TAG, "onReceiveMessageData() called with: context = [" + context + "], gtTransmitMessage = [" + gtTransmitMessage + "]");

        // 拿到透传消息的实体信息转换为字符串
        byte[] payload = gtTransmitMessage.getPayload();
        if (payload != null) {
            String message = new String(payload);
            onMessageArrived(message);
            //createNotificationChannel(context, 1, message);
            displayNotificaton();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        // 当设备状态变化时回调，是否在线
        Log.i(TAG, "onReceiveOnlineState() called with: context = [" + context + "], b = [" + b + "]");
    }


    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        // 当个推Command命名返回时回调
        Log.i(TAG, "onReceiveCommandResult() called with: context = [" + context + "], gtCmdMessage = [" + gtCmdMessage + "]");
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        // 当通知栏消息达到时回调
        Log.i(TAG, "onNotificationMessageArrived() called with: context = [" + context + "], gtNotificationMessage = [" + gtNotificationMessage + "]");
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        // 当通知栏消息点击时回调
        Log.i(TAG, "onNotificationMessageClicked() called with: context = [" + context + "], gtNotificationMessage = [" + gtNotificationMessage + "]");
    }


    /**
     * 当Id初始化的试试
     *
     * @param cid 设备Id
     */
    private void onClientInit(String cid) {
        // 设置设备Id
        Account.setPushId(cid);
        if (Account.isLogin()) {
            // 账户登录状态，进行一次PushId绑定
            // 没有登录是不能绑定PushId的
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息达到时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        // 交给Factory处理
        Factory.dispatchPush(message);
    }


    public void createNotificationChannel(Context context, int notifactionId, String message) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = String.valueOf(notifactionId);
            CharSequence channelName = "channelName";
            String channelDescription = "channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            // 设置描述 最长30字符
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            //notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi), null);


            notificationManager.createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(context);
            notification.setChannelId(channelId);
            notification.setContentTitle("Only Chat");
            notification.setContentText("New message arrived");
            notification.setPriority(Notification.PRIORITY_MAX);
            notification.setVisibility(Notification.VISIBILITY_PUBLIC);
            notification.setDefaults(Notification.DEFAULT_ALL);
            //notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi));
            notification.setSmallIcon(R.drawable.ic_icon3).build();

        } else {
            notification = new Notification.Builder(context);
            notification.setAutoCancel(true)
                    .setContentText("New message arrived")
                    .setContentTitle("Only Chat")
                    .setSmallIcon(R.drawable.ic_icon3)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_ALL);
            //notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi));
        }
        notificationManager.notify(1024, notification.getNotification());
    }

    public void createNotification(Context context, int buiderID) {
        //第一步：实例化通知栏构造器Notification.Builder：
        Notification.Builder builder = new Notification.Builder(context);//实例化通知栏构造器Notification.Builder，参数必填（Context类型），为创建实例的上下文
        //第二步：获取状态通知栏管理：
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取状态栏通知的管理类（负责发通知、清除通知等操作）
        //第三步：设置通知栏PendingIntent（点击动作事件等都包含在这里）:
        Intent push = new Intent(context, MainActivity.class);//新建一个显式意图，第一个参数 Context 的解释是用于获得 package name，以便找到第二个参数 Class 的位置
        //PendingIntent可以看做是对Intent的包装，通过名称可以看出PendingIntent用于处理即将发生的意图，而Intent用来用来处理马上发生的意图
        //本程序用来响应点击通知的打开应用,第二个参数非常重要，点击notification 进入到activity, 使用到pendingIntent类方法，PengdingIntent.getActivity()的第二个参数，即请求参数，实际上是通过该参数来区别不同的Intent的，如果id相同，就会覆盖掉之前的Intent了
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        //第四步：对Builder进行配置：
        builder
                .setContentTitle("Only Chat")//标题
                .setContentText("New Message Arrived")// 详细内容
                .setContentIntent(contentIntent)//设置点击意图
                .setTicker("New message")//第一次推送，角标旁边显示的内容
                .setSmallIcon(R.drawable.ic_icon3)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))//设置大图标
                .setDefaults(Notification.DEFAULT_ALL);//打开呼吸灯，声音，震动，触发系统默认行为
        /*Notification.DEFAULT_VIBRATE    //添加默认震动提醒  需要VIBRATE permission
        Notification.DEFAULT_SOUND    //添加默认声音提醒
        Notification.DEFAULT_LIGHTS//添加默认三色灯提醒
        Notification.DEFAULT_ALL//添加默认以上3种全部提醒*/
        //.setLights(Color.YELLOW, 300, 0)//单独设置呼吸灯，一般三种颜色：红，绿，蓝，经测试，小米支持黄色
        //.setSound(url)//单独设置声音
        //.setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })//单独设置震动
        //比较手机sdk版本与Android 5.0 Lollipop的sdk
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder
                    /*android5.0加入了一种新的模式Notification的显示等级，共有三种：
                    VISIBILITY_PUBLIC只有在没有锁屏时会显示通知
                    VISIBILITY_PRIVATE任何情况都会显示通知
                    VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知*/
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                    .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
//.setColor(context.getResources().getColor(R.color.small_icon_bg_color))//设置smallIcon的背景色
                    .setFullScreenIntent(contentIntent, true)//将Notification变为悬挂式Notification
                    .setSmallIcon(R.drawable.ic_icon3);//设置小图标
        } else {
            builder
                    .setSmallIcon(R.mipmap.ic_launcher);//设置小图标
        }
//第五步：发送通知请求：
        Notification notify = builder.build();//得到一个Notification对象
        mNotifyMgr.notify(buiderID, notify);//发送通知请求
    }

    public void createNotification2(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //Toast.makeText(context, "此类通知在Android 5.0以上版本才会有横幅有效！", Toast.LENGTH_SHORT).show();
        }
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Only Chat");
        builder.setContentText("New Message Arrived");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        builder.setSmallIcon(R.drawable.ic_icon3);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon3));
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setFullScreenIntent(pIntent, true);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        notificationManager.notify(1, notification);//注意这里 1 为当前通知栏的 Id 号，和 Fragment 设置 Id 是一样的

        // 设置 heads-up 消失任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("start the cancel task....");
                notificationManager.cancel(1); // 根据之前设置的通知栏 Id 号，让相关通知栏消失掉
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }

    private void displayNotificaton() {
        String CHANNEL_ID = "RazerPayChannel";// The id of the channel.
        CharSequence name = "channelName";// The user-visible name of the channel.
        /**
         * Oreo不用Priority了，用importance
         * IMPORTANCE_NONE 关闭通知
         * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
         * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
         * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
         * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
         */
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //关键代码：版本sdk大于或等于8.0.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            manager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Only Chat")
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500})
                .setOnlyAlertOnce(true)
                .setContentText("New Message Arrived")
                .setChannelId("RazerPayChannel")
                .setPriority(NotificationManager.IMPORTANCE_HIGH);


        int mNotificationId = (int) System.currentTimeMillis();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        if (msg.getMessageId() != null) {
//            intent.putExtra(Const.KAIZO_PUBLIC_NOTIFY, msg.getMessageId());
//        }
        //intent.putExtra(Const.KAIZO_PUBLIC_NOTIFY, msg.getMessageId());
        PendingIntent contentIntent = PendingIntent.getActivity(this, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        manager.notify(mNotificationId, builder.build());
    }
}


