package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    private HashMap<UUID, Crime> mCrimeMap;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        mCrimeMap = new HashMap<>();
    }

    static CrimeLab getInstance(Context context) {
        if (null == sCrimeLab) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    List<Crime> getCrimes() {
        return mCrimes;
    }

    Crime getCrime(UUID uuid) {
        return mCrimeMap.get(uuid);
    }

    void addCrime(Crime crime) {
        mCrimes.add(crime);
        mCrimeMap.put(crime.getId(), crime);
    }
}
