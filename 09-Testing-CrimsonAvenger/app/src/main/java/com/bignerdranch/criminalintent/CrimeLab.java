package com.bignerdranch.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.criminalintent.model.Crime;
import com.bignerdranch.criminalintent.model.CrimeDao;
import com.bignerdranch.criminalintent.model.DaoMaster;
import com.bignerdranch.criminalintent.model.DaoSession;

import java.io.File;
import java.util.List;

/**
 * Created by Pieter-Jan on 22/12/2015.
 */
public class CrimeLab
{
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private CrimeDao crimeDao;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
            sCrimeLab.initDb();
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
    }

    private void initDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "crime-db", null);
        mDatabase = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        DaoSession daoSession = daoMaster.newSession();
        crimeDao = daoSession.getCrimeDao();
    }

    public void addCrime(Crime c) {
        crimeDao.insertOrReplace(c);
    }

    public void deleteCrime(Crime c) {
        crimeDao.delete(c);
    }

    public List<Crime> getCrimes() {
        return crimeDao.queryBuilder().orderAsc(CrimeDao.Properties.Id).list();
    }

    public Crime getCrime(Long id) {
        return crimeDao.queryBuilder().where(CrimeDao.Properties.Id.eq(id)).unique();
    }

    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime) {
        crimeDao.update(crime);
    }
}
