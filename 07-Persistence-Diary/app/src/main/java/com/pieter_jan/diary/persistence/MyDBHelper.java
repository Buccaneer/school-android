package com.pieter_jan.diary.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, MetaData.EntryTable.TABLE_NAME, null, MetaData.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MetaData.EntryTable.TABLE_NAME + " ("
                + MetaData.EntryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MetaData.EntryTable.TITLE + " TEXT,"
                + MetaData.EntryTable.CONTENT + " TEXT,"
                + MetaData.EntryTable.DATE + " LONG,"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MetaData.EntryTable.TABLE_NAME);
        onCreate(db);
    }
}
