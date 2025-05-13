package com.wealthwise.models;

import com.google.gson.annotations.SerializedName;

public class Bar {

    @SerializedName("t")
    private long timestamp;

    @SerializedName("o")
    private double open;

    @SerializedName("h")
    private double high;

    @SerializedName("l")
    private double low;

    @SerializedName("c")
    private double close;

    @SerializedName("v")
    private long volume;

    public Bar() {}

    public Bar(long timestamp, double open, double high, double low, double close, long volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public long getVolume() {
        return volume;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}