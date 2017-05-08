package com.passer.bean;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

public class SpotBean implements Serializable {
    private String mName;
    private String mSnippet;
    private double mLatitude;
    private double mLongitude;

    public LatLng getLatLng(){
        return new LatLng(mLatitude,mLongitude);
    }

    public boolean isStart = false, isDestination = false;

    public SpotBean(String name, double latitude, double longitude, String snippet) {
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

    @Override
    public String toString() {
        return "SpotBean{" +
                "mName='" + mName + '\'' +
                ", mSnippet='" + mSnippet + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", isStart=" + isStart +
                ", isDestination=" + isDestination +
                '}';
    }
}
