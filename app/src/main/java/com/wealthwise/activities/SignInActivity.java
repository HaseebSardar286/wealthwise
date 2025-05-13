package com.wealthwise.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
// Import R class from your package
import com.wealthwise.R;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonSignIn;
    private TextView textViewGoToSignUp;
    private ProgressBar progressBarSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewGoToSignUp = findViewById(R.id.textViewGoToSignUp);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);
        progressBarSignIn.setVisibility(View.GONE); // Hide progress bar as it's not used

        buttonSignIn.setOnClickListener(v -> {
            Toast.makeText(SignInActivity.this, "Navigating to Main Screen (Sign-In Bypassed)", Toast.LENGTH_SHORT).show();
            navigateToMain();
        });

        textViewGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void navigateToMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        // Clear back stack so user can't navigate back to SignIn after "logging in"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

