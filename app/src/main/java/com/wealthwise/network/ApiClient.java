package com.wealthwise.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://paper-api.alpaca.markets/v2/";

    private static final String API_KEY_ID = "YOUR_ALPACA_API_KEY_ID";
    private static final String API_SECRET_KEY = "YOUR_ALPACA_API_SECRET_KEY";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("APCA-API-KEY-ID", API_KEY_ID)
                                    .header("APCA-API-SECRET-KEY", API_SECRET_KEY)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static TradingViewApiService getApiService() {
        return getClient().create(TradingViewApiService.class);
    }
}