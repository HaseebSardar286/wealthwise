package com.wealthwise.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wealthwise.R;
import com.wealthwise.activities.SellCoinActivity;
import com.wealthwise.adapters.PortfolioAdapter;
import com.wealthwise.models.PortfolioItem; // PortfolioItem model will be used for placeholder data
import com.wealthwise.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PortfolioFragment extends Fragment implements PortfolioAdapter.OnPortfolioItemClickListener {

    private static final String TAG = "PortfolioFragment";

    private RecyclerView recyclerViewPortfolio;
    private PortfolioAdapter portfolioAdapter;
    private ProgressBar progressBarPortfolio;
    private TextView textViewTotalBalanceValue;
    private TextView textViewEmptyPortfolio;


    private List<PortfolioItem> portfolioItems = new ArrayList<>();
    private Map<String, Double> currentPrices = new HashMap<>();

    public PortfolioFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPortfolio = view.findViewById(R.id.recyclerViewPortfolio);
        progressBarPortfolio = view.findViewById(R.id.progressBarPortfolio);
        textViewTotalBalanceValue = view.findViewById(R.id.textViewTotalBalanceValue);
        textViewEmptyPortfolio = view.findViewById(R.id.textViewEmptyPortfolio);

        progressBarPortfolio.setVisibility(View.GONE);

        recyclerViewPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
        populateStaticPortfolioData(); // Populate with static data
        portfolioAdapter = new PortfolioAdapter(getContext(), portfolioItems, currentPrices, this);
        recyclerViewPortfolio.setAdapter(portfolioAdapter);

        if (portfolioItems.isEmpty()) {
            textViewEmptyPortfolio.setVisibility(View.VISIBLE);
            recyclerViewPortfolio.setVisibility(View.GONE);
            textViewTotalBalanceValue.setText(Utils.formatCurrency(0.0));
        } else {
            textViewEmptyPortfolio.setVisibility(View.GONE);
            recyclerViewPortfolio.setVisibility(View.VISIBLE);
            updatePortfolioUI();
        }
    }

    private void populateStaticPortfolioData() {
        portfolioItems.clear();
        currentPrices.clear();


        PortfolioItem item1 = new PortfolioItem("Bitcoin", "BTC", 0.5, 55000.00);
        portfolioItems.add(item1);
        currentPrices.put("BTC", 60500.00); // Placeholder current price

        PortfolioItem item2 = new PortfolioItem("Ethereum", "ETH", 2.0, 3800.00);
        portfolioItems.add(item2);
        currentPrices.put("ETH", 4050.00); // Placeholder current price

        PortfolioItem item3 = new PortfolioItem("Apple Inc.", "AAPL", 10.0, 160.00);
        portfolioItems.add(item3);
        currentPrices.put("AAPL", 175.00); // Placeholder current price
    }


    private void updatePortfolioUI() {
        portfolioAdapter.updateData(portfolioItems, currentPrices);
        calculateAndDisplayTotalBalance();
    }

    private void calculateAndDisplayTotalBalance() {
        double totalBalance = 0.0;
        for (PortfolioItem item : portfolioItems) {
            Double currentPrice = currentPrices.get(item.getSymbol());
            if (currentPrice != null) {
                totalBalance += item.getQuantity() * currentPrice;
            }
        }
        textViewTotalBalanceValue.setText(Utils.formatCurrency(totalBalance));
    }

    private void showError(String message) {
        Log.e(TAG, message);
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPortfolioItemClick(PortfolioItem item) {
        Toast.makeText(getContext(), "Clicked on: " + item.getName() + ", navigating to Sell screen.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), SellCoinActivity.class);
        intent.putExtra("ASSET_SYMBOL", item.getSymbol());
        intent.putExtra("CURRENT_PRICE", currentPrices.getOrDefault(item.getSymbol(), 0.0)); // Pass current price if available
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

