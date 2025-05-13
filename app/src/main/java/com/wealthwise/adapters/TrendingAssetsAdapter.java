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
import com.wealthwise.models.Asset;
import com.wealthwise.utils.Utils; // Assuming Utils has formatCurrency

import java.util.List;
import java.util.Locale;

public class TrendingAssetsAdapter extends RecyclerView.Adapter<TrendingAssetsAdapter.ViewHolder> {

    private List<Asset> assetList;
    private Context context;
    private OnAssetClickListener listener;

    public interface OnAssetClickListener {
        void onAssetClick(Asset asset);
    }

    public TrendingAssetsAdapter(Context context, List<Asset> assetList, OnAssetClickListener listener) {
        this.context = context;
        this.assetList = assetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_asset, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Asset asset = assetList.get(position);

        holder.textViewAssetSymbol.setText(asset.getSymbol());
        holder.textViewAssetName.setText(asset.getName());
        holder.textViewAssetPrice.setText(Utils.formatCurrency(asset.getCurrentPrice()));

        double change = asset.getDailyChangePercentage();
        String changeText = String.format(Locale.US, "%.2f%%", change);
        holder.textViewAssetChange.setText(changeText);

        if (change >= 0) {
            holder.textViewAssetChange.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.textViewAssetChange.setText("+" + changeText);
        } else {
            holder.textViewAssetChange.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssetClick(asset);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetList == null ? 0 : assetList.size();
    }

    public void updateData(List<Asset> newAssetList) {
        this.assetList.clear();
        if (newAssetList != null) {
            this.assetList.addAll(newAssetList);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewAssetSymbol;
        public TextView textViewAssetName;
        public TextView textViewAssetPrice;
        public TextView textViewAssetChange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssetSymbol = itemView.findViewById(R.id.textViewAssetSymbol);
            textViewAssetName = itemView.findViewById(R.id.textViewAssetName);
            textViewAssetPrice = itemView.findViewById(R.id.textViewAssetPrice);
            textViewAssetChange = itemView.findViewById(R.id.textViewAssetChange);
        }
    }
}

