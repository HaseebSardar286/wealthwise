package com.wealthwise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wealthwise.R;
import com.wealthwise.models.PortfolioItem;
import com.wealthwise.utils.Utils; // Assuming Utils has formatCurrency

import java.util.List;
import java.util.Locale;
import java.util.Map; // Needed for calculating P/L with current prices

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    private List<PortfolioItem> portfolioItemList;
    private Map<String, Double> currentPrices;
    private Context context;
    private OnPortfolioItemClickListener listener;

    public interface OnPortfolioItemClickListener {
        void onPortfolioItemClick(PortfolioItem item);
    }

    public PortfolioAdapter(Context context, List<PortfolioItem> portfolioItemList, Map<String, Double> currentPrices, OnPortfolioItemClickListener listener) {
        this.context = context;
        this.portfolioItemList = portfolioItemList;
        this.currentPrices = currentPrices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_portfolio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PortfolioItem item = portfolioItemList.get(position);

        holder.textViewPortfolioSymbol.setText(item.getSymbol());
        holder.textViewPortfolioName.setText(item.getName());
        holder.textViewPortfolioQuantityValue.setText(String.format(Locale.US, "%.4f", item.getQuantity()));
        holder.textViewPortfolioAvgPriceValue.setText(Utils.formatCurrency(item.getAveragePrice()));

        Double currentPrice = currentPrices.get(item.getSymbol());
        if (currentPrice != null) {
            double currentValue = item.getQuantity() * currentPrice;
            double costBasis = item.getQuantity() * item.getAveragePrice();
            double profitLoss = currentValue - costBasis;
            double profitLossPercent = (costBasis == 0) ? 0 : (profitLoss / costBasis) * 100;

            holder.textViewPortfolioValue.setText(Utils.formatCurrency(currentValue));
            String plText = String.format(Locale.US, "P/L: %s%.2f (%.2f%%)",
                    (profitLoss >= 0 ? "+" : ""), profitLoss, profitLossPercent);
            holder.textViewPortfolioPL.setText(plText);

            if (profitLoss >= 0) {
                holder.textViewPortfolioPL.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            } else {
                holder.textViewPortfolioPL.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            }
            holder.textViewPortfolioPL.setVisibility(View.VISIBLE);
        } else {
            holder.textViewPortfolioValue.setText("N/A");
            holder.textViewPortfolioPL.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPortfolioItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return portfolioItemList == null ? 0 : portfolioItemList.size();
    }

    public void updateData(List<PortfolioItem> newPortfolioItems, Map<String, Double> newCurrentPrices) {
        this.portfolioItemList.clear();
        if (newPortfolioItems != null) {
            this.portfolioItemList.addAll(newPortfolioItems);
        }
        this.currentPrices.clear();
        if (newCurrentPrices != null) {
            this.currentPrices.putAll(newCurrentPrices);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPortfolioSymbol;
        public TextView textViewPortfolioName;
        public TextView textViewPortfolioValue;
        public TextView textViewPortfolioQuantityLabel;
        public TextView textViewPortfolioQuantityValue;
        public TextView textViewPortfolioAvgPriceLabel;
        public TextView textViewPortfolioAvgPriceValue;
        public TextView textViewPortfolioPL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPortfolioSymbol = itemView.findViewById(R.id.textViewPortfolioSymbol);
            textViewPortfolioName = itemView.findViewById(R.id.textViewPortfolioName);
            textViewPortfolioValue = itemView.findViewById(R.id.textViewPortfolioValue);
            textViewPortfolioQuantityLabel = itemView.findViewById(R.id.textViewPortfolioQuantityLabel);
            textViewPortfolioQuantityValue = itemView.findViewById(R.id.textViewPortfolioQuantityValue);
            textViewPortfolioAvgPriceLabel = itemView.findViewById(R.id.textViewPortfolioAvgPriceLabel);
            textViewPortfolioAvgPriceValue = itemView.findViewById(R.id.textViewPortfolioAvgPriceValue);
            textViewPortfolioPL = itemView.findViewById(R.id.textViewPortfolioPL);
        }
    }
}

