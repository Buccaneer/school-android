package com.pieter_jan.kappagrrm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pieter_jan.kappagrrm.model.*;
import com.pieter_jan.kappagrrm.model.Character;

import java.util.List;

/**
 * Created by Pieter-Jan on 22/12/2015.
 */
public class GRRM
{
    private static GRRM grrm;
    private SQLiteDatabase mDatabase;
    private CharacterDao characterDao;

    public static GRRM getInstance(Context mContext)
    {
        if (grrm == null)
            grrm = new GRRM(mContext);
        return grrm;
    }

    private GRRM(Context mContext)
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "got-db", null);
        mDatabase = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        DaoSession daoSession = daoMaster.newSession();
        characterDao = daoSession.getCharacterDao();
    }

    public void init(List<Character> characters)
    {
        for (Character c : characters)
            characterDao.insert(c);
    }

    public List<Character> getRemaining()
    {
        return characterDao.queryBuilder().list();
    }

    public void killCharacter(Character c)
    {
        characterDao.delete(c);
    }

}
