package com.project.app.thread;

import com.project.app.bean.FileInfo;
import com.project.app.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class InitThread extends Thread{
    private FileInfo fileInfo;

    public InitThread(FileInfo fileInfo){
        this.fileInfo=fileInfo;
    }

    @Override
    public void run() {
        RandomAccessFile randomAccessFile=null;
        HttpURLConnection connection=null;
        try {
            //开启网络连接
            URL url=new URL(fileInfo.url);
            connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.connect();

            //获取下载文件长度
            int length=-1;
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                length=connection.getContentLength();
            }
            if(length<=0){
                return;
            }

            //在本地创建文件
            File dir=new File(Constant.DOWNLOAD_PATH);
            if(!dir.exists()){
                dir.mkdir();
            }

            File file=new File(dir,fileInfo.fileName);
            //设置文件长度
            randomAccessFile=new RandomAccessFile(file,"rwd");
            randomAccessFile.setLength(length);
            fileInfo.length=length;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                randomAccessFile.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
