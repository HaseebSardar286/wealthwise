package com.wealthwise.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

// Import R class from your package
import com.wealthwise.R;

public class ConsultantProfileActivity extends AppCompatActivity {

    private static final String TAG = "ConsultantProfileAct";

    private ImageView imageViewProfilePic;
    private TextView textViewProfileName, textViewProfileSpecialty, textViewProfileBio;
    private RatingBar ratingBarProfile;
    private Button buttonStartChat;
    private ProgressBar progressBarProfile;

    private String consultantUid;
    private String consultantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_profile);

        consultantUid = getIntent().getStringExtra("CONSULTANT_UID");
        consultantName = getIntent().getStringExtra("CONSULTANT_NAME");

        imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        textViewProfileName = findViewById(R.id.textViewProfileName);
        textViewProfileSpecialty = findViewById(R.id.textViewProfileSpecialty);
        textViewProfileBio = findViewById(R.id.textViewProfileBio);
        ratingBarProfile = findViewById(R.id.ratingBarProfile);
        buttonStartChat = findViewById(R.id.buttonStartChat);
        progressBarProfile = findViewById(R.id.progressBarProfile);
        progressBarProfile.setVisibility(View.GONE); // Hide progress bar

        buttonStartChat.setOnClickListener(v -> startChat());

        displayPlaceholderData();
    }

    private void displayPlaceholderData() {
        textViewProfileName.setText(consultantName != null ? consultantName : "Dr. Placeholder Expert");
        textViewProfileSpecialty.setText("Financial Advice & Planning");
        ratingBarProfile.setRating(4.5f); // Placeholder rating
        textViewProfileBio.setText("This is a placeholder biography for the consultant. They have many years of experience and can help you with your financial goals. Contact them to learn more!");

        buttonStartChat.setEnabled(true); // Enable chat button
    }


    private void startChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("RECIPIENT_UID", consultantUid != null ? consultantUid : "consultant_placeholder_uid");
        intent.putExtra("RECIPIENT_NAME", textViewProfileName.getText().toString()); // Use the name displayed
        startActivity(intent);
    }

    private void showError(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

