package com.kami.learneverything;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kami.learneverything.com.kami.part.DownloadActivity;
import com.kami.learneverything.com.kami.part.NotificationActivity;
import com.kami.learneverything.com.kami.part.TestMyView;
import com.kami.learneverything.com.kami.part.UsingActionBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_notifaction;
    private Button btn_actionbar;
    private Button btn_download;
    private Button btn_scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        btn_notifaction = (Button) findViewById(R.id.learnNotification);
        btn_notifaction.setOnClickListener(this);

        btn_actionbar = (Button) findViewById(R.id.learnActionbar);
        btn_actionbar.setOnClickListener(this);

        btn_download = (Button) findViewById(R.id.download);
        btn_download.setOnClickListener(this);

        btn_scrollView = (Button) findViewById(R.id.testScrollView);
        btn_scrollView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.learnNotification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.learnActionbar:
                startActivity(new Intent(this, UsingActionBar.class));
                break;
            case R.id.download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;
            case R.id.testScrollView:
                startActivity(new Intent(this, TestMyView.class));
                break;

        }
    }


}
