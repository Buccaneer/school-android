package com.pieter_jan.diary.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.pieter_jan.diary.DiaryEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class MyDB
{
    private SQLiteDatabase db;
    private MyDBHelper helper;

    public MyDB(Context context) {
        helper = new MyDBHelper(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public long insert(String tableName, String entry, ContentValues values) {
        return db.insert(tableName, entry, values);
    }

    public int update(String tableName, ContentValues values, String s, String[] selectionArgs) {
        return db.update(tableName, values, s, selectionArgs);
    }

    public int delete(String tableName, String where, String[] whereArgs) {
        return db.delete(tableName, where, whereArgs);
    }

    public Cursor getDiaryEntries(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return qb.query(db, projection, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void insertDiaryEntry(DiaryEntry entry) {
        ContentValues values = new ContentValues();
        values.put(MetaData.EntryTable.TITLE, entry.getTitle());
        values.put(MetaData.EntryTable.CONTENT, entry.getContent());
        values.put(MetaData.EntryTable.DATE, System.currentTimeMillis());
        db.insert(MetaData.EntryTable.TABLE_NAME, null, values);
    }

    public List<DiaryEntry> getDiaryEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(MetaData.EntryTable.TABLE_NAME, MetaData.EntryTable.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            DiaryEntry entry = new DiaryEntry();
            entry.setId(cursor.getInt(cursor.getColumnIndex(MetaData.EntryTable._ID)));
            entry.setTitle(cursor.getString(cursor.getColumnIndex(MetaData.EntryTable.TITLE)));
            entry.setContent(cursor.getString(cursor.getColumnIndex(MetaData.EntryTable.CONTENT)));
            entry.setDate(cursor.getLong(cursor.getColumnIndex(MetaData.EntryTable.DATE)));
            entries.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }
}
