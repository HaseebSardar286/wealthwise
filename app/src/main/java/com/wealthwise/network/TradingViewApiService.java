package com.wealthwise.network;

import com.wealthwise.models.Asset;
import com.wealthwise.models.Bar; // Import the new Bar class
import com.wealthwise.models.OrderResponse;
import com.wealthwise.models.PlaceOrderRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TradingViewApiService {

    @GET("quotes")
    Call<List<Asset>> getQuotes(@Query("symbols") String symbols);

    @GET("assets")
    Call<List<Asset>> getTrendingAssets();

    @POST("orders")
    Call<OrderResponse> placeOrder(@Body PlaceOrderRequest orderRequest);

    @GET("bars/day")
    Call<List<Bar>> getHistoricalBars(
            @Query("symbols") String symbol,
            @Query("timeframe") String timeframe,
            @Query("start") String start,
            @Query("end") String end
    );
}