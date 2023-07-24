package com.bignerdranch.android.cafeguide;

import java.util.Date;
import java.util.UUID;

public class Cafe {

    private UUID mID;
    private String mCafeName;
    private Date mVisitedDate;
    private boolean mRecommended;
    private String mReview;

    public Cafe() {
        this(UUID.randomUUID());
    }

    public Cafe(UUID id) {
        mID = id;
        mVisitedDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public String getCafeName() {
        return mCafeName;
    }

    public void setCafeName(String cafeName) {
        mCafeName = cafeName;
    }

    public Date getVisitedDate() {
        return mVisitedDate;
    }

    public String getVisitedDateByFormat(String format) {
        return String.valueOf(android.text.format.DateFormat.format(format, mVisitedDate));
    }

    public void setVisitedDate(Date dateVisited) {
        mVisitedDate = dateVisited;
    }

    public boolean isRecommended() {
        return mRecommended;
    }

    public void setRecommended(boolean recommended) {
        mRecommended = recommended;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

    // Adding the filename-derived property (unique file name)
    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
}
