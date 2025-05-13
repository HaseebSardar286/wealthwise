package com.wealthwise.models;

public class Asset {
    private String symbol;
    private String name;
    private double price;
    private double changePercent24h;

    public Asset() {}

    public Asset(String btc, String bitcoin, double v, String bitcoin1, String crypto, double v1) {
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getChangePercent24h() { return changePercent24h; }
    public void setChangePercent24h(double changePercent24h) { this.changePercent24h = changePercent24h; }

    public double getCurrentPrice() {
        return 0;
    }

    public double getDailyChangePercentage() {
        return 0;
    }
}
