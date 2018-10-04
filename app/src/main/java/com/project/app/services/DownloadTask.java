package com.project.app.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.app.bean.FileInfo;
import com.project.app.bean.ThreadInfo;
import com.project.app.constant.Constant;
import com.project.app.dao.ThreadDao;
import com.project.app.dao.ThreadDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadTask {
    private Context context;
    private FileInfo fileInfo;
    private ThreadDao threadDao;
    private int finished=0;
    private boolean isPause=false;

    public DownloadTask(Context context,FileInfo fileInfo){
        this.context=context;
        this.fileInfo=fileInfo;
        threadDao=new ThreadDaoImpl(context);
    }


    class DownloadThread extends Thread{
        private ThreadInfo threadInfo;

        public DownloadThread(ThreadInfo threadInfo){
            this.threadInfo=threadInfo;
        }

        @Override
        public void run() {
            //开启网络连接
            HttpURLConnection connection=null;
            RandomAccessFile randomAccessFile=null;
            InputStream inputStream=null;

            if(!threadDao.threadIsExist(threadInfo.url,threadInfo.id)){
                threadDao.insertThread(threadInfo);
            }

            try {
                URL url=new URL(threadInfo.url);
                connection=(HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                //设置文件写入位置
                int start=threadInfo.start+threadInfo.finished;
                connection.addRequestProperty("Range","bytes="+start+"-"+threadInfo.finished);
//                connection.addRequestProperty("Accept-Encoding", "identity");
                connection.addRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");

                //在本地创建文件
                File dir=new File(Constant.DOWNLOAD_PATH);
                if(!dir.exists()){
                    dir.mkdir();
                }
                File file=new File(Constant.DOWNLOAD_PATH,fileInfo.fileName);
                randomAccessFile=new RandomAccessFile(file,"rwd");
                randomAccessFile.seek(start);
                //无条件GET包含Range请求头，响应会以状态码206（PartialContent）返回而不是以200 （OK）
                if(connection.getResponseCode()==HttpURLConnection.HTTP_PARTIAL){
                //开始下载
                finished=+threadInfo.finished;

//                fileInfo.length=connection.getContentLength();
                    fileInfo.length=(int)13.4*1024*1024;//因为服务器没有返回content-length的相关信息,所以在已知文件大小的情况下,用一个模拟数据写死
                Log.i("TAG","fileInfo.length====="+fileInfo.length);
                inputStream=connection.getInputStream();
                byte[] b=new byte[50*1024];
                int len=inputStream.read(b);
                long time=System.currentTimeMillis();
                while (len!=-1){
                    randomAccessFile.write(b,0,len);
                    finished+=len;
                    if(System.currentTimeMillis()-time>1000){
                        Intent intent=new Intent(DownloadService.ACTION_DOWNLOADING);
                        intent.putExtra("finished",finished*100/fileInfo.length);
                        Log.i("TAG","finished====="+finished);
                        Log.i("TAG","progress====="+finished*100/fileInfo.length);
                        context.sendBroadcast(intent);
                    }

                }

                //下载结束,删除线程信息
                    Intent intent=new Intent(DownloadService.ACTION_FINISHED);
                    context.sendBroadcast(intent);
                    threadDao.deleteThread(threadInfo.url,threadInfo.id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {

                try {
                    connection.disconnect();
                    inputStream.close();
                    randomAccessFile.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //下载文件
    public void download(){
        List<ThreadInfo> list=threadDao.queryThread(fileInfo.url);
        ThreadInfo threadInfo=null;
        if(list.size()==0){
            threadInfo=new ThreadInfo();
            threadInfo.url=fileInfo.url;
        }else {
            threadInfo=list.get(0);
        }

        new DownloadThread(threadInfo).start();
    }

}
