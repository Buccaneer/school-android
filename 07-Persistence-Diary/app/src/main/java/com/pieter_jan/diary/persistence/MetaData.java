package com.pieter_jan.diary.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class MetaData
{
    public static final String AUTHORITY = "com.pieter_jan.diary.provider";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/entry"
    );

    public static final String DATABASE_NAME = "diary.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CONTENT_TYPE_ENTRIES_LIST = "vnd.android.cursor.dir/com.pieter_jan.provider.entries";
    public static final String CONTENT_TYPE_ENTRY = "vnd.android.cursor.item/com.pieter_jan.provider.entries";
    public static final String DEFAULT_SORT_ORDER = "_id DESC";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE "
            + EntryTable.TABLE_NAME + " ("
            + EntryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EntryTable.TITLE + " TEXT,"
            + EntryTable.CONTENT + " TEXT,"
            + EntryTable.DATE + " LONG"
            + ");";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + EntryTable.TABLE_NAME;

    public static class EntryTable implements BaseColumns
    {
        public static final String TABLE_NAME = "entries";
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
        public static final String[] ALL_COLUMNS = { _ID, TITLE, CONTENT, DATE };
    }
}
