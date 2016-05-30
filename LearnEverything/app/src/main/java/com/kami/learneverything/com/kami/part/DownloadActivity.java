package com.kami.learneverything.com.kami.part;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.kami.learneverything.MainActivity;
import com.kami.learneverything.R;
import com.kami.learneverything.com.kami.utils.DownloadUtil;

public class DownloadActivity extends AppCompatActivity {



    NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0x111;

    RemoteViews remoteViews;
    Notification notify;

    private Button btn_download;
    private TextView tv;

    private int count = 0;

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0x123){
//                count++;
//                notify.contentView.setProgressBar(R.id.pb_donwnload_doc,100,count/3*100,false);
//                notificationManager.notify(NOTIFICATION_ID, notify);
//                Log.i("progress", count + "/3");
//                if (count == 3){
//                    tv.setText("Download Success!!");
////                    notificationManager.cancel(NOTIFICATION_ID);
//                }
//            }
//            super.handleMessage(msg);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        //获取系统的Notification服务
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        remoteViews = new RemoteViews(getPackageName(),R.layout.item_download);

        tv = (TextView) findViewById(R.id.tv);

        btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
                DownloadUtil downloadUtil = new DownloadUtil();
                downloadUtil.setOnDownloadListener(listener);
                downloadUtil.start(DownloadUtil.path);
            }
        });

    }

    //发送通知
    private void sendNotification() {
        Intent intent = new Intent(DownloadActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DownloadActivity.this, 0, intent, 0);

//        remoteViews.setProgressBar(R.id.pb_donwnload_doc,100,30,false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notify = new Notification.Builder(this)
                    .setContent(remoteViews)
                            //设置通知自动消失
                    .setAutoCancel(true)
                            //设置在通知栏闪过的文本
                    .setTicker("有新消息")
                            //设置通知的图标
                    .setSmallIcon(R.drawable.icon_download_file)
                            //设置通知内容的标题
                    .setContentTitle("一条新通知")
                            //设置通知内容
                    .setContentText("这是通知内容")
                            //设置使用系统默认的声音、默认LED灯
                    .setDefaults(Notification.DEFAULT_SOUND)
                            //设置通知将要启动程序的Intent
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.notify(NOTIFICATION_ID,notify);
        }
    }

    DownloadUtil.OnDownloadListener listener = new DownloadUtil.OnDownloadListener(){

        @Override
        public void onStart() {
            Log.i("onStart","下载开始...");
        }

        @Override
        public void onPublish() {
            Log.i("onPublish","某个线程下载完毕...");
                count++;
                notify.contentView.setProgressBar(R.id.pb_donwnload_doc,100,count/3*100,false);
                notificationManager.notify(NOTIFICATION_ID, notify);
        }

        @Override
        public void onSuccess() {
            Log.i("onSuccess","下载成功!");
//            tv.setText("Download Success!!");
        }

        @Override
        public void onError() {
            Log.i("onError","下载失败...");
        }
    };

}
