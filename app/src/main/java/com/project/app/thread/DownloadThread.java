package com.project.app.thread;

import com.project.app.bean.FileInfo;
import com.project.app.bean.ThreadInfo;
import com.project.app.constant.Constant;
import com.project.app.dao.ThreadDao;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadThread extends Thread {
    private ThreadDao threadDao;
    private ThreadInfo threadInfo;
    private FileInfo fileInfo;
    private int finished;

    public DownloadThread(ThreadDao threadDao, ThreadInfo threadInfo, FileInfo fileInfo){
            this.threadDao=threadDao;
            this.threadInfo=threadInfo;
            this.fileInfo=fileInfo;
    }

    @Override
    public void run() {
        //向数据库插入线程信息
        if(threadDao.threadIsExist(threadInfo.url,threadInfo.id)){
            threadDao.insertThread(threadInfo);
        }

        //开启网络连接
        HttpURLConnection connection=null;
        RandomAccessFile randomAccessFile=null;
        InputStream inputStream=null;

        try {
            URL url=new URL(threadInfo.url);
            connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");

            int start=threadInfo.start+threadInfo.finished;
            connection.setRequestProperty("Range","bytes="+start+"-"+threadInfo.end);

            //在本地创建文件
            File file=new File(Constant.DOWNLOAD_PATH,fileInfo.fileName);
            randomAccessFile=new RandomAccessFile(file,"rwd");
            randomAccessFile.seek(start);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
