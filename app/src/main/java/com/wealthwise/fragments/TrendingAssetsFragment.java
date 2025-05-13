package com.wealthwise.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wealthwise.R;
import com.wealthwise.activities.BuyCoinActivity;
import com.wealthwise.adapters.TrendingAssetsAdapter;
import com.wealthwise.models.Asset; // Asset model will be used for placeholder data
// import com.wealthwise.network.ApiClient; // Removed
// import com.wealthwise.network.TradingViewApiService; // Removed

import java.util.ArrayList;
import java.util.List;

public class TrendingAssetsFragment extends Fragment implements TrendingAssetsAdapter.OnAssetClickListener {

    private RecyclerView recyclerViewTrendingAssets;
    private TrendingAssetsAdapter trendingAssetsAdapter;
    private ProgressBar progressBarTrending;
    private List<Asset> assetList = new ArrayList<>();

    public TrendingAssetsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trending_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewTrendingAssets = view.findViewById(R.id.recyclerViewTrendingAssets);
        progressBarTrending = view.findViewById(R.id.progressBarTrending);
        progressBarTrending.setVisibility(View.GONE);

        recyclerViewTrendingAssets.setLayoutManager(new LinearLayoutManager(getContext()));
        populateStaticAssets();
        trendingAssetsAdapter = new TrendingAssetsAdapter(getContext(), assetList, this);
        recyclerViewTrendingAssets.setAdapter(trendingAssetsAdapter);


        recyclerViewTrendingAssets.setVisibility(View.VISIBLE);
    }

    private void populateStaticAssets() {
        assetList.clear();
        assetList.add(new Asset("BTC", "Bitcoin", 60000.00, "Bitcoin", "crypto", 2.5));
        assetList.add(new Asset("ETH", "Ethereum", 4000.00, "Ethereum", "crypto", -1.2));
        assetList.add(new Asset("AAPL", "Apple Inc.", 170.00, "Apple Inc.", "stock", 0.5));
        assetList.add(new Asset("TSLA", "Tesla Inc.", 700.00, "Tesla Inc.", "stock", 3.1));
        assetList.add(new Asset("GOLD", "Gold Spot", 2300.00, "Gold Spot", "commodity", 0.2));

    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAssetClick(Asset asset) {
        Toast.makeText(getContext(), "Clicked on: " + asset.getName() + ", navigating to Buy screen.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), BuyCoinActivity.class);
        intent.putExtra("ASSET_SYMBOL", asset.getSymbol());
        intent.putExtra("CURRENT_PRICE", asset.getCurrentPrice());
        startActivity(intent);
    }
}

