package com.project.app.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.project.app.MainActivity;
import com.project.app.bean.FileInfo;

public class DownloadService extends Service {
    //开始下载
    public static final String ACTION_START="DOWNLOAD_START";
    //暂停下载
    public static final String ACTION_PAUSE="DOWNLOAD_PAUSE";
    //正在下载
    public static final String ACTION_DOWNLOADING="ACTION_DOLOADING";
    //下载完成
    public static final String ACTION_FINISHED="ACTION_FINSHED";

    private DownloadTask downloadTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action=intent.getAction();
        FileInfo fileInfo=(FileInfo) intent.getSerializableExtra("fileInfo");
        switch (action){
            case ACTION_START:
                //开始下载
                Log.i("TAG","开始下载url===="+fileInfo.url);
                downloadTask=new DownloadTask(DownloadService.this,fileInfo);
                downloadTask.download();
                break;
            case ACTION_PAUSE:
                //暂停
                Log.i("TAG","暂停下载url===="+fileInfo.url);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }



}
