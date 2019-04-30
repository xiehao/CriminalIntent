package com.example.criminalintent;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mPoliceRequired;

    Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    String getFormattedDate() {
        return DateFormat.getDateInstance(DateFormat.FULL).format(mDate);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    boolean isSolved() {
        return mSolved;
    }

    void setSolved(boolean solved) {
        mSolved = solved;
    }

    boolean isPoliceRequired() {
        return mPoliceRequired;
    }

    void setPoliceRequired(boolean policeRequired) {
        mPoliceRequired = policeRequired;
    }
}
