package com.pieter_jan.diary.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pieter_jan.diary.DiaryEntry;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class MyDB
{
    private SQLiteDatabase db;
    private MyDBHelper helper;
    private String[] allColumns = { MetaData.EntryTable._ID, MetaData.EntryTable.TITLE, MetaData.EntryTable.CONTENT, MetaData.EntryTable.DATE };

    public MyDB(Context context) {
        helper = new MyDBHelper(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public void insertDiaryEntry(DiaryEntry entry) {
        ContentValues values = new ContentValues();
        values.put(MetaData.EntryTable.TITLE, entry.getTitle());
        values.put(MetaData.EntryTable.CONTENT, entry.getContent());
        values.put(MetaData.EntryTable.DATE, new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()));
        db.insert(MetaData.EntryTable.TABLE_NAME, null, values);
    }

    public List<DiaryEntry> getDiaryEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(MetaData.EntryTable.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            DiaryEntry entry = new DiaryEntry();
            entry.setId(cursor.getInt(cursor.getColumnIndex(MetaData.EntryTable._ID)));
            entry.setTitle(cursor.getString(cursor.getColumnIndex(MetaData.EntryTable.TITLE)));
            entry.setContent(cursor.getString(cursor.getColumnIndex(MetaData.EntryTable.CONTENT)));
            entry.setDate(new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").parse(cursor.getString(cursor.getColumnIndex(MetaData.EntryTable.DATE)), new ParsePosition(0)));
            entries.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }
}
