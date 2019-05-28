package com.example.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.criminalintent.Crime;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable.Columns;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(Columns.UUID));
        String title = getString(getColumnIndex(Columns.TITLE));
        long date = getLong(getColumnIndex(Columns.DATE));
        int isSolved = getInt(getColumnIndex(Columns.SOLVED));
        int isPoliceRequired = getInt(getColumnIndex(Columns.POLICE_REQUIRED));
        String suspect = getString(getColumnIndex(Columns.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(0 != isSolved);
        crime.setPoliceRequired(0 != isPoliceRequired);
        crime.setSuspect(suspect);

        return crime;
    }
}
