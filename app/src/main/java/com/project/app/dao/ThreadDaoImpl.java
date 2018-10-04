package com.project.app.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.app.bean.ThreadInfo;
import com.project.app.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ThreadDaoImpl implements ThreadDao {

    private DBHelper dbHelper;

    public ThreadDaoImpl(Context context){
        dbHelper=new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        Object[] objects=new Object[]{threadInfo.id,threadInfo.url,threadInfo.start,threadInfo.end,threadInfo.finished};
        String sql="insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql,objects);
        sqLiteDatabase.close();
    }

    @Override
    public void deleteThread(String url, int threadId) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        Object[] objects=new Object[]{url,threadId};
        String sql="delete from thread_info where url=? and thread_id=?";
        sqLiteDatabase.execSQL(sql,objects);
        sqLiteDatabase.close();
    }

    @Override
    public void deleteThread(String url) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        Object[] objects=new Object[]{url};
        String sql="delete from thread_info where url=?";
        sqLiteDatabase.execSQL(sql,objects);
        sqLiteDatabase.close();
    }

    @Override
    public void updateThread(String url, int threadId,int finished) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        Object[] objects=new Object[]{finished,url,threadId};
        String sql="update thread_info set finished=? where url=? and thread_id=?";
        sqLiteDatabase.execSQL(sql,objects);
        sqLiteDatabase.close();

    }


    @Override
    public List<ThreadInfo> queryThread(String url) {
        List<ThreadInfo> list=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        String sql="select * from thread_info where url=?";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,new String[]{url});
        while (cursor.moveToNext()){
            ThreadInfo threadInfo=new ThreadInfo();
            threadInfo.id=cursor.getInt(cursor.getColumnIndex("thread_id"));
            threadInfo.url=cursor.getString(cursor.getColumnIndex("url"));
            threadInfo.start=cursor.getInt(cursor.getColumnIndex("start"));
            threadInfo.end=cursor.getInt(cursor.getColumnIndex("end"));
            threadInfo.finished=cursor.getInt(cursor.getColumnIndex("finished"));
            list.add(threadInfo);
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    @Override
    public boolean threadIsExist(String url, int threadId) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        String sql="select * from thread_info where url=? and thread_id=?";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,new String[]{url,threadId+""});
        boolean isExist=cursor.moveToNext();
        cursor.close();
        sqLiteDatabase.close();
        return isExist;
    }
}
