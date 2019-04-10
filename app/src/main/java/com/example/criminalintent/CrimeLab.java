package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            Crime crime = new Crime();
            crime.setTitle("陋习编号" + i);
            crime.setSolved(0 == i % 2);
            crime.setPoliceRequired(0 == i % 3);
            mCrimes.add(crime);
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

    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }
}
