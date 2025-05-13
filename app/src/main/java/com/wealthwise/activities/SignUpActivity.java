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

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private TextView textViewGoToSignIn;
    private ProgressBar progressBarSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewGoToSignIn = findViewById(R.id.textViewGoToSignIn);
        progressBarSignUp = findViewById(R.id.progressBarSignUp);
        progressBarSignUp.setVisibility(View.GONE); // Hide progress bar as it's not used

        buttonSignUp.setOnClickListener(v -> {
            Toast.makeText(SignUpActivity.this, "Navigating to Main Screen (Sign-Up Bypassed)", Toast.LENGTH_SHORT).show();
            navigateToMain();
        });

        textViewGoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

