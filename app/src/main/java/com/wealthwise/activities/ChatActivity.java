package com.wealthwise.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

// Import R class from your package
import com.wealthwise.R;
import com.wealthwise.adapters.ChatAdapter;
import com.wealthwise.models.ChatMessage;

import java.util.ArrayList;
import java.util.Date; // For timestamping local messages
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessageInput;
    private ImageButton buttonSendMessage;
    private Toolbar toolbarChat;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    private String recipientUid;
    private String recipientName;
    private String currentUserId = "currentUserPlaceholderUid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recipientUid = getIntent().getStringExtra("RECIPIENT_UID");
        recipientName = getIntent().getStringExtra("RECIPIENT_NAME");

        if (recipientUid == null) {
            recipientUid = "recipient_placeholder_uid";
        }
        if (recipientName == null) {
            recipientName = "Placeholder Consultant";
        }

        toolbarChat = findViewById(R.id.toolbarChat);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessageInput = findViewById(R.id.editTextMessageInput);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        setSupportActionBar(toolbarChat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipientName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarChat.setNavigationOnClickListener(v -> onBackPressed());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(this, messageList);
        recyclerViewMessages.setAdapter(chatAdapter);

        buttonSendMessage.setOnClickListener(v -> sendMessage());

        addPlaceholderMessages();
    }


    private void sendMessage() {
        String messageText = editTextMessageInput.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        ChatMessage message = new ChatMessage(currentUserId, recipientUid, messageText, new Date());

        messageList.add(message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        editTextMessageInput.setText("");

        Toast.makeText(this, "Message sent (simulated)", Toast.LENGTH_SHORT).show();
    }

    private void addPlaceholderMessages() {
        messageList.add(new ChatMessage(recipientUid, currentUserId, "Hello! How can I help you today?", new Date(System.currentTimeMillis() - 60000 * 5)));
        messageList.add(new ChatMessage(currentUserId, recipientUid, "Hi, I have a question about my investments.", new Date(System.currentTimeMillis() - 60000 * 4)));
        chatAdapter.notifyDataSetChanged();
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
    }

    private void showError(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

