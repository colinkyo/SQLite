package com.example.a7yan.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import static android.R.attr.version;

/**
 * Created by 7Yan on 2016/12/29.
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME="presons.db";
    private static final int DB_VERSION=1;
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE person(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name CHAR(10),nickname CHAR(10))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            String sql="DROP TABLE IF EXITS person";
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
