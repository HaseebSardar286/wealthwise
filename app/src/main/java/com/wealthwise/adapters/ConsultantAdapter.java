package com.wealthwise.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import Glide or Picasso if you use them for image loading
// import com.bumptech.glide.Glide;

import com.wealthwise.R;
import com.wealthwise.activities.ConsultantProfileActivity; // Activity to open on click
import com.wealthwise.models.Consultant;

import java.util.List;

public class ConsultantAdapter extends RecyclerView.Adapter<ConsultantAdapter.ViewHolder> {

    private List<Consultant> consultantList;
    private Context context;

    public ConsultantAdapter(Context context, List<Consultant> consultantList) {
        this.context = context;
        this.consultantList = consultantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_consultant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consultant consultant = consultantList.get(position);

        holder.textViewConsultantName.setText(consultant.getName());
        holder.textViewConsultantSpecialty.setText(consultant.getSpecialty());
        holder.ratingBarConsultant.setRating((float) consultant.getRating());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ConsultantProfileActivity.class);
            intent.putExtra("CONSULTANT_UID", consultant.getUid());
            intent.putExtra("CONSULTANT_NAME", consultant.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return consultantList == null ? 0 : consultantList.size();
    }

    public void updateData(List<Consultant> newConsultantList) {
        this.consultantList.clear();
        if (newConsultantList != null) {
            this.consultantList.addAll(newConsultantList);
        }
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewConsultantPic;
        public TextView textViewConsultantName;
        public TextView textViewConsultantSpecialty;
        public RatingBar ratingBarConsultant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewConsultantPic = itemView.findViewById(R.id.imageViewConsultantPic);
            textViewConsultantName = itemView.findViewById(R.id.textViewConsultantName);
            textViewConsultantSpecialty = itemView.findViewById(R.id.textViewConsultantSpecialty);
            ratingBarConsultant = itemView.findViewById(R.id.ratingBarConsultant);
        }
    }
}