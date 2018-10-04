package com.project.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="download.db";
    private static final int version=1;

    private static final String CREATE_TABLE_TREADINFO="create table thread_info(_id integer primary key autoincrement,thread_id integer,url text,start integer,end integer,finished integer)";

    private static final String DROP_TABLE_TREADINFO="drop table if exists thread_info";


    public DBHelper(Context context){
        super(context,DB_NAME,null,version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TREADINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
         sqLiteDatabase.execSQL(DROP_TABLE_TREADINFO);
         sqLiteDatabase.execSQL(CREATE_TABLE_TREADINFO);
    }


}
