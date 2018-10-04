package com.project.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.project.app.bean.FileInfo;
import com.project.app.services.DownloadService;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btStart;
    private Button btPause;

    private FileInfo fileInfo;


    //下载地址
    private String url="http://www.imooc.com/mobile/mukewang.apk";
//    private String url="http://gdown.baidu.com/data/wisegame/79fb2f638cc11043/oldoffender.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addListener();

    }


    private void initView() {
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        btStart=(Button)findViewById(R.id.btStart);
        btPause=(Button)findViewById(R.id.btPause);

        fileInfo=new FileInfo(1,url,"mukewang.apk",0,0);

        //注册广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOADING);
        intentFilter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(receiver,intentFilter);

    }
    private void addListener(){


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DownloadService.class);
                intent.setAction(DownloadService.ACTION_PAUSE);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case DownloadService.ACTION_DOWNLOADING:
                    //正在下载
                    int progress=intent.getIntExtra("finished",0);
                    progressBar.setProgress(progress);

                    break;
                case DownloadService.ACTION_FINISHED:
                    //下载完成
                    Log.i("TAG","下载完成");
                    break;
            }
        }
    };

}
