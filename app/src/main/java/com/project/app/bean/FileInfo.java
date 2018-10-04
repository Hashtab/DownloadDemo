package com.project.app.bean;

import java.io.Serializable;

public class FileInfo implements Serializable{
    //文件id
    public int id;
   // 文件对应的下载地址
    public String url;
    // 文件名
    public String fileName;
    //下载文件长度
    public int length;
    //下载完成进度
    public int finished;

    public FileInfo(int id, String url, String fileName, int length, int finished) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finished = finished;
    }





}
