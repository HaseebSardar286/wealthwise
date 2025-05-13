package com.wealthwise.models;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("id")
    private String orderId;

    @SerializedName("status")
    private String status;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("side")
    private String side;

    @SerializedName("type")
    private String type;

    @SerializedName("qty")
    private double quantity;

    @SerializedName("filled_qty")
    private double filledQuantity;

    @SerializedName("avg_fill_price")
    private Double averageFillPrice;

    @SerializedName("rejected_reason")
    private String message;

    public OrderResponse() {}

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getSymbol() { return symbol; }
    public String getSide() { return side; }
    public String getType() { return type; }
    public double getQuantity() { return quantity; }
    public double getFilledQuantity() { return filledQuantity; }
    public Double getAverageFillPrice() { return averageFillPrice; }
    public String getMessage() { return message; }
}