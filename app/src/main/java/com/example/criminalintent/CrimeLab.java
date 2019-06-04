package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeBaseHelper;
import com.example.criminalintent.database.CrimeCursorWrapper;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable.Columns;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class CrimeLab {
    private static CrimeLab sCrimeLab;
    private final SQLiteDatabase mDatabase;
    private Context mContext;
//    private List<Crime> mCrimes;
//    private HashMap<UUID, Crime> mCrimeMap;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();
//        mCrimeMap = new HashMap<>();
    }

    static CrimeLab getInstance(Context context) {
        if (null == sCrimeLab) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    List<Crime> getCrimes() {
//        return mCrimes;
        List<Crime> crimes = new ArrayList<>();
        try (CrimeCursorWrapper cursor = queryCrimes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        return crimes;
    }

    Crime getCrime(UUID uuid) {
//        return mCrimeMap.get(uuid);
        try (CrimeCursorWrapper cursor = queryCrimes(
                Columns.UUID + " = ?",
                new String[]{uuid.toString()}
        )) {
            if (0 != cursor.getCount()) {
                cursor.moveToFirst();
                return cursor.getCrime();
            }
        }
        return null;
    }

    int getCrimePosition(UUID uuid) {
        try (CrimeCursorWrapper cursor = queryCrimes(
                Columns.UUID + " = ?",
                new String[]{uuid.toString()}
        )) {
            if (0 != cursor.getCount()) {
                cursor.moveToFirst();
                return cursor.getPosition();
            }
        }
        return 0;
    }

    void addCrime(Crime crime) {
//        mCrimes.add(crime);
//        mCrimeMap.put(crime.getId(), crime);
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,
                Columns.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Columns.UUID, crime.getId().toString());
        values.put(Columns.TITLE, crime.getTitle());
        values.put(Columns.DATE, crime.getDate().getTime());
        values.put(Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(Columns.POLICE_REQUIRED, crime.isPoliceRequired() ? 1 : 0);
        values.put(Columns.SUSPECT, crime.getSuspect());
        values.put(Columns.PHONE, crime.getPhoneNumber());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    void deleteCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        mDatabase.delete(CrimeTable.NAME,
                Columns.UUID + " = ?",
                new String[]{uuidString});
    }
}
