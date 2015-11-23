package com.pieter_jan.diary.persistence;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class DiaryContentProvider extends ContentProvider
{
    private static final int ENTRIES = 1;
    private static final int ENTRY_ID = 2;
    private static HashMap<String, String> sNotesProjectionMap;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MetaData.AUTHORITY, "entry", ENTRIES);
        sUriMatcher.addURI(MetaData.AUTHORITY, "entry/#", ENTRY_ID);

        sNotesProjectionMap = new HashMap<>();
        sNotesProjectionMap.put(MetaData.EntryTable._ID, MetaData.EntryTable._ID);
        sNotesProjectionMap.put(MetaData.EntryTable.TITLE, MetaData.EntryTable.TITLE);
        sNotesProjectionMap.put(MetaData.EntryTable.CONTENT, MetaData.EntryTable.CONTENT);
        sNotesProjectionMap.put(MetaData.EntryTable.DATE, MetaData.EntryTable.DATE);
    }

    private MyDBHelper helper;

    @Override
    public boolean onCreate() {
        helper = new MyDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MetaData.EntryTable.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case ENTRIES:
                qb.setProjectionMap(sNotesProjectionMap);
                break;

            case ENTRY_ID:
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(MetaData.EntryTable._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = MetaData.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ENTRIES:
                return MetaData.CONTENT_TYPE_ENTRY;
            case ENTRY_ID:
                return MetaData.CONTENT_TYPE_ENTRY;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != ENTRIES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // Make sure that the fields are all set
        if (!values.containsKey(MetaData.EntryTable.TITLE)) {
            values.put(MetaData.EntryTable.TITLE, "TODO");
        }

        if (!values.containsKey(MetaData.EntryTable.CONTENT)) {
            values.put(MetaData.EntryTable.CONTENT, "TODO");
        }

        if (!values.containsKey(MetaData.EntryTable.DATE)) {
            values.put(MetaData.EntryTable.DATE, "TODO");
        }


        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId = db.insert(MetaData.EntryTable.TABLE_NAME, MetaData.EntryTable.CONTENT, values);
        if (rowId > 0) {
            Uri entryUri = ContentUris.withAppendedId(MetaData.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(entryUri, null);
            return entryUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case ENTRIES:
                count = db.delete(MetaData.EntryTable.TABLE_NAME, where, whereArgs);
                break;

            case ENTRY_ID:
                String entryId = uri.getPathSegments().get(1);
                count = db.delete(MetaData.EntryTable.TABLE_NAME, MetaData.EntryTable._ID + "=" + entryId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case ENTRIES:
                count = db.update(MetaData.EntryTable.TABLE_NAME, values, where, whereArgs);
                break;

            case ENTRY_ID:
                String entryId = uri.getPathSegments().get(1);
                count = db.update(MetaData.EntryTable.TABLE_NAME, values, MetaData.EntryTable._ID + "=" + entryId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
