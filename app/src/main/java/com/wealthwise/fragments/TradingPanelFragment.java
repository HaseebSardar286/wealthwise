package com.wealthwise.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.mikephil.charting.charts.CandlestickChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.CandlestickData;
//import com.github.mikephil.charting.data.CandlestickDataSet;
//import com.github.mikephil.charting.data.CandlestickEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wealthwise.R;
import com.wealthwise.activities.BuyCoinActivity;
import com.wealthwise.activities.SellCoinActivity;
import com.wealthwise.models.Asset;
import com.wealthwise.models.Bar;
import com.wealthwise.network.ApiClient;
import com.wealthwise.network.TradingViewApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;

public class TradingPanelFragment extends Fragment {

    private static final String TAG = "TradingPanelFragment";
    private static final String ARG_SYMBOL = "asset_symbol";

    private TextView textViewTradingSymbol, textViewTradingPrice, textViewTradingChange;
    private Button buttonBuy, buttonSell;
    private FrameLayout chartContainer;
    private TradingViewApiService apiService;
    private WebSocket webSocket;

    private String currentSymbol;
    private Asset currentAssetData;

    public TradingPanelFragment() {}

    public static TradingPanelFragment newInstance(String symbol) {
        TradingPanelFragment fragment = new TradingPanelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SYMBOL, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentSymbol = getArguments().getString(ARG_SYMBOL);
        }
        apiService = ApiClient.getApiService();
        // Log to verify MPAndroidChart availability
        try {
            Class.forName("com.github.mikephil.charting.charts.CandlestickChart");
            Log.d(TAG, "MPAndroidChart CandlestickChart class loaded successfully");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "MPAndroidChart not found: " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trading_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        textViewTradingSymbol = view.findViewById(R.id.textViewTradingSymbol);
        textViewTradingPrice = view.findViewById(R.id.textViewTradingPrice);
        textViewTradingChange = view.findViewById(R.id.textViewTradingChange);
        buttonBuy = view.findViewById(R.id.buttonBuy);
        buttonSell = view.findViewById(R.id.buttonSell);
        chartContainer = view.findViewById(R.id.chartContainer);
        textViewTradingSymbol.setText(currentSymbol != null ? currentSymbol : "N/A");

        buttonBuy.setOnClickListener(v -> openBuyScreen());
        buttonSell.setOnClickListener(v -> openSellScreen());

        setupChart();
        if (currentSymbol != null) {
            fetchCurrentQuote(currentSymbol);
            setupWebSocket();
        }
    }

    private void setupChart() {
        if (getContext() == null) {
            Log.e(TAG, "Context is null, cannot setup chart");
            return;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String endDate = sdf.format(new Date());
        long thirtyDaysAgo = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000;
        String startDate = sdf.format(new Date(thirtyDaysAgo));

        apiService.getHistoricalBars(currentSymbol, "1D", startDate, endDate)
                .enqueue(new Callback<List<Bar>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Bar>> call, @NonNull retrofit2.Response<List<Bar>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "Received " + response.body().size() + " bars");
                            int index = 0;
                        } else {
                            showError("Failed to load chart data. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Bar>> call, @NonNull Throwable t) {
                        showError("Failed to load chart data: " + t.getMessage());
                    }
                });
    }

    private void setupWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("wss://stream.data.alpaca.markets/v2/iex")
                .build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                webSocket.send("{\"action\":\"auth\",\"key\":\"YOUR_API_KEY_ID\",\"secret\":\"YOUR_API_SECRET_KEY\"}");
                webSocket.send("{\"action\":\"subscribe\",\"quotes\":[\"" + currentSymbol + "\"]}");
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                try {
                    Gson gson = new Gson();
                    JsonObject json = gson.fromJson(text, JsonObject.class);
                    if (json.has("stream") && json.get("stream").getAsString().equals("quotes")) {
                        JsonObject data = json.getAsJsonObject("data");
                        double price = data.has("bp") ? data.get("bp").getAsDouble() : 0.0;
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                textViewTradingPrice.setText(String.format(Locale.US, "$%.2f", price));
                            });
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "WebSocket parsing error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> showError("WebSocket error: " + t.getMessage()));
                }
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                webSocket.close(1000, null);
            }
        });
    }

    private void fetchCurrentQuote(String symbol) {
        Call<List<Asset>> call = apiService.getQuotes(symbol);
        call.enqueue(new Callback<List<Asset>>() {
            @Override
            public void onResponse(@NonNull Call<List<Asset>> call, @NonNull retrofit2.Response<List<Asset>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentAssetData = response.body().get(0);
                    updatePriceUI();
                } else {
                    showError("Failed to load quote. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Asset>> call, @NonNull Throwable t) {
                showError("Network error loading quote: " + t.getMessage());
            }
        });
    }

    private void updatePriceUI() {
        if (currentAssetData == null || getContext() == null) return;

        textViewTradingPrice.setText(String.format(Locale.US, "$%.2f", currentAssetData.getPrice()));

        double change = currentAssetData.getChangePercent24h();
        String changeText = String.format(Locale.US, "%.2f%%", change);

        if (change >= 0) {
            textViewTradingChange.setText("+" + changeText);
            textViewTradingChange.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        } else {
            textViewTradingChange.setText(changeText);
            textViewTradingChange.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        }
    }

    private void openBuyScreen() {
        if (currentSymbol == null) {
            showError("Cannot initiate buy: Symbol not loaded.");
            return;
        }
        Intent intent = new Intent(getActivity(), BuyCoinActivity.class);
        intent.putExtra("ASSET_SYMBOL", currentSymbol);
        if (currentAssetData != null) {
            intent.putExtra("CURRENT_PRICE", currentAssetData.getPrice());
        }
        startActivity(intent);
    }

    private void openSellScreen() {
        if (currentSymbol == null) {
            showError("Cannot initiate sell: Symbol not loaded.");
            return;
        }
        Intent intent = new Intent(getActivity(), SellCoinActivity.class);
        intent.putExtra("ASSET_SYMBOL", currentSymbol);
        if (currentAssetData != null) {
            intent.putExtra("CURRENT_PRICE", currentAssetData.getPrice());
        }
        startActivity(intent);
    }

    private void showError(String message) {
        Log.e(TAG, message);
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webSocket != null) {
            webSocket.close(1000, "Fragment destroyed");
        }
    }
}