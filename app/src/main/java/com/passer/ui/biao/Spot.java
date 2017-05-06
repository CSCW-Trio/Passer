package com.passer.ui.biao;

import java.io.Serializable;

public class Spot implements Serializable {
    private String mName;
    private String mSnippet;
    private double mLatitude;
    private double mLongitude;

    public boolean isStart = false, isDestination = false;

    public Spot(String name, double latitude, double longitude, String snippet) {
        this.mName = name;
        this.mSnippet = snippet;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public void setSnippet(String snippet) {
        this.mSnippet = snippet;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }
}
