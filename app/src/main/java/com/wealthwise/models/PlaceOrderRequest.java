package com.wealthwise.models;

import com.google.gson.annotations.SerializedName;

public class PlaceOrderRequest {

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("side")
    private String side;

    @SerializedName("type")
    private String type;

    @SerializedName("qty")
    private double quantity;

    @SerializedName("limit_price")
    private Double limitPrice;

    @SerializedName("time_in_force")
    private String timeInForce;

    public PlaceOrderRequest(String symbol, String side, String type, double quantity, Double limitPrice) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.limitPrice = limitPrice;
        this.timeInForce = "day";
    }

    public String getSymbol() { return symbol; }
    public String getSide() { return side; }
    public String getType() { return type; }
    public double getQuantity() { return quantity; }
    public Double getLimitPrice() { return limitPrice; }
    public String getTimeInForce() { return timeInForce; }
}