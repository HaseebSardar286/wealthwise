package com.wealthwise.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

// Placeholder ChatMessage model for Firebase Firestore
@IgnoreExtraProperties
public class ChatMessage {
    private String messageId; // Document ID
    private String senderId;
    private String recipientId;
    private String text;
    @ServerTimestamp
    private Date timestamp;

    public ChatMessage() {}

    public ChatMessage(String senderId, String recipientId, String text) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
    }

    public ChatMessage(String currentUserId, String recipientUid, String messageText, Date date) {
    }

    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getRecipientId() { return recipientId; }
    public String getText() { return text; }
    public Date getTimestamp() { return timestamp; }

    public void setMessageId(String messageId) { this.messageId = messageId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public void setText(String text) { this.text = text; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
