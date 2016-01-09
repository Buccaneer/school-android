package com.pieter_jan.redditzor.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pieter-Jan on 9/01/2016.
 */
public class DaoHelper
{
    private static SQLiteDatabase db = null;
    private static DaoSession session = null;

    public static DaoSession getSession(Context context)
    {
        if (session == null)
        {
            session = getMaster(context).newSession();
        }
        return session;
    }

    private static DaoMaster getMaster(Context context)
    {
        if (db == null)
        {
            db = getDatabase(context);
        }
        return new DaoMaster(db);
    }

    private static SQLiteDatabase getDatabase(Context context)
    {
        SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(context, "reddit-db", null);
        return helper.getWritableDatabase();
    }
}
