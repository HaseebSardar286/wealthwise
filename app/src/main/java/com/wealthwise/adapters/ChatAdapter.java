package com.wealthwise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.wealthwise.R;
import com.wealthwise.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<ChatMessage> messageList;
    private Context context;
    private String currentUserId;

    // Constructor
    public ChatAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        if (currentUserId != null && currentUserId.equals(message.getSenderId())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_received, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        holder.textViewMessageText.setText(message.getText());

        if (message.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            holder.textViewMessageTimestamp.setText(sdf.format(message.getTimestamp()));
            holder.textViewMessageTimestamp.setVisibility(View.VISIBLE);
        } else {
            holder.textViewMessageTimestamp.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public void updateData(List<ChatMessage> newMessageList) {
        this.messageList.clear();
        if (newMessageList != null) {
            this.messageList.addAll(newMessageList);
        }
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessageText;
        public TextView textViewMessageTimestamp;
        // Optional: ImageView for sender profile pic in received messages

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageText = itemView.findViewById(R.id.textViewMessageText);
            textViewMessageTimestamp = itemView.findViewById(R.id.textViewMessageTimestamp);
        }
    }
}
