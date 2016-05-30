package com.kami.learneverything.com.kami.part;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.kami.learneverything.MainActivity;
import com.kami.learneverything.R;

public class NotificationActivity extends AppCompatActivity {

    static final int NOTIFICATION_ID = 0x123;
    NotificationManager notificationManager;
    RemoteViews remoteViews;
    Notification notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //获取系统的Notification服务
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remoteViews.setProgressBar(R.id.pb_donwnload_doc,100,70,false);
//                remoteViews.setTextViewText(R.id.tv_on_download,"哈哈");
                notify.contentView.setProgressBar(R.id.pb_donwnload_doc, 100, 80, false);
                notify.contentView.setTextViewText(R.id.tv_on_download, "哈哈");
                notificationManager.notify(NOTIFICATION_ID,notify);
//                notificationManager.cancel(NOTIFICATION_ID);
            }
        });

    }

    //发送通知
    private void sendNotification() {
        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);

        remoteViews = new RemoteViews(getPackageName(),R.layout.item_download);
        remoteViews.setProgressBar(R.id.pb_donwnload_doc,100,30,false);

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

}
