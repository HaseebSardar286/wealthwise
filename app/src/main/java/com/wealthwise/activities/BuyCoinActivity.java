package com.wealthwise.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wealthwise.R;
import com.wealthwise.utils.Utils;

public class BuyCoinActivity extends AppCompatActivity {

    private TextView textViewBuyTitle, textViewCurrentPriceValue;
    private EditText editTextQuantity;
    private Button buttonConfirmBuy;
    private ProgressBar progressBarBuy;
    private String symbol;
    private double currentPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coin);

        textViewBuyTitle = findViewById(R.id.textViewBuyTitle); // Corrected ID for symbol display
        textViewCurrentPriceValue = findViewById(R.id.textViewCurrentPriceValue); // Corrected ID for price display
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonConfirmBuy = findViewById(R.id.buttonConfirmBuy);
        progressBarBuy = findViewById(R.id.progressBarBuy);
        progressBarBuy.setVisibility(View.GONE);

        symbol = getIntent().getStringExtra("ASSET_SYMBOL");
        currentPrice = getIntent().getDoubleExtra("CURRENT_PRICE", 0.0);

        textViewBuyTitle.setText(symbol != null ? "Buy " + symbol : "Buy DUMMY_COIN"); // Updated to reflect it's a title
        textViewCurrentPriceValue.setText(currentPrice > 0 ? Utils.formatCurrency(currentPrice) : Utils.formatCurrency(123.45));

        buttonConfirmBuy.setOnClickListener(v -> confirmBuy());
    }

    private void confirmBuy() {
        String quantityStr = editTextQuantity.getText().toString();
        if (quantityStr.isEmpty()) {
            editTextQuantity.setError("Enter quantity (e.g., 1.0)");
            editTextQuantity.requestFocus();
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
            if (quantity <= 0) {
                editTextQuantity.setError("Quantity must be positive");
                editTextQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Invalid quantity");
            editTextQuantity.requestFocus();
            return;
        }

        Toast.makeText(BuyCoinActivity.this, "Buy action for " + quantity + " of " + (symbol != null ? symbol : "DUMMY_COIN") + " simulated.", Toast.LENGTH_LONG).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

