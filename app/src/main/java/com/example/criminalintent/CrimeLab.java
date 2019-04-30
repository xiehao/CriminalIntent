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
        for (int i = 0; i < 100; ++i) {
            Crime crime = new Crime();
            crime.setTitle("陋习编号" + i);
            crime.setSolved(0 == i % 2);
            crime.setPoliceRequired(0 == i % 3);
            mCrimes.add(crime);
            mCrimeMap.put(crime.getId(), crime);
        }
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
}
