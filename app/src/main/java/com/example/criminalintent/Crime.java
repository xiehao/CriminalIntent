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
    private String mSuspect;
    private String mPhoneNumber;

    Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid) {
        mId = uuid;
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

    public void setTitle(String title) {
        mTitle = title;
    }

    Date getDate() {
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

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    boolean isPoliceRequired() {
        return mPoliceRequired;
    }

    public void setPoliceRequired(boolean policeRequired) {
        mPoliceRequired = !mSolved && policeRequired;
    }

    String getFormattedTime() {
        return DateFormat.getTimeInstance(DateFormat.MEDIUM).format(mDate);
    }

    String getFormattedDateTime() {
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(mDate);
    }

    String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
