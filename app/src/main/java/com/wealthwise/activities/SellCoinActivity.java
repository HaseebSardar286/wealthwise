package com.wealthwise.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wealthwise.R;
// Removed Firebase, API client, and model imports not needed for simple navigation
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.firestore.FirebaseFirestore;
// import com.wealthwise.models.OrderResponse;
// import com.wealthwise.models.PlaceOrderRequest;
// import com.wealthwise.models.PortfolioItem;
// import com.wealthwise.network.ApiClient;
// import com.wealthwise.network.TradingViewApiService;
import com.wealthwise.utils.Utils; // Keep if used for formatting

import java.util.Locale;

public class SellCoinActivity extends AppCompatActivity {

    private static final String TAG = "SellCoinActivity";

    private TextView textViewSellTitle, textViewCurrentPriceValue, textViewAvailableQuantityValue, textViewEstimatedProceedsValue;
    private Spinner spinnerOrderType;
    private TextInputEditText editTextQuantity, editTextLimitPrice;
    private TextInputLayout textFieldLimitPriceLayout;
    private Button buttonConfirmSell;
    private ProgressBar progressBarSell;

    private String assetSymbol;
    private double currentPrice = 0.0;
    private double availableQuantity = 10.0;
    private String selectedOrderType = "Market";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_coin);

        assetSymbol = getIntent().getStringExtra("ASSET_SYMBOL");
        currentPrice = getIntent().getDoubleExtra("CURRENT_PRICE", 0.0);

        textViewSellTitle = findViewById(R.id.textViewSellTitle);
        textViewCurrentPriceValue = findViewById(R.id.textViewCurrentPriceValue);
        textViewAvailableQuantityValue = findViewById(R.id.textViewAvailableQuantityValue);
        textViewEstimatedProceedsValue = findViewById(R.id.textViewEstimatedProceedsValue);
        spinnerOrderType = findViewById(R.id.spinnerOrderType);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextLimitPrice = findViewById(R.id.editTextLimitPrice);
        textFieldLimitPriceLayout = findViewById(R.id.textFieldLimitPriceLayout);
        buttonConfirmSell = findViewById(R.id.buttonConfirmSell);
        progressBarSell = findViewById(R.id.progressBarSell);
        progressBarSell.setVisibility(View.GONE); // Hide progress bar

        textViewSellTitle.setText("Sell " + (assetSymbol != null ? assetSymbol : "DUMMY_ASSET"));
        textViewCurrentPriceValue.setText(currentPrice > 0 ? Utils.formatCurrency(currentPrice) : Utils.formatCurrency(98.76)); // Placeholder price
        textViewAvailableQuantityValue.setText(String.format(Locale.US, "%.4f %s", availableQuantity, (assetSymbol != null ? assetSymbol : "DUMMY_ASSET")));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderType.setAdapter(adapter);
        spinnerOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrderType = parent.getItemAtPosition(position).toString();
                textFieldLimitPriceLayout.setVisibility("Limit".equalsIgnoreCase(selectedOrderType) ? View.VISIBLE : View.GONE);
                updateEstimatedProceeds();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        TextWatcher proceedsWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateEstimatedProceeds(); }
            @Override public void afterTextChanged(Editable s) { }
        };
        editTextQuantity.addTextChangedListener(proceedsWatcher);
        editTextLimitPrice.addTextChangedListener(proceedsWatcher);

        buttonConfirmSell.setOnClickListener(v -> placeSellOrder());

        updateEstimatedProceeds();
    }


    private void updateEstimatedProceeds() {
        double quantity = 0.0;
        double priceToUse = 0.0;

        try {
            quantity = Double.parseDouble(editTextQuantity.getText().toString());
        } catch (NumberFormatException e) {
        }

        if ("Limit".equalsIgnoreCase(selectedOrderType)) {
            try {
                priceToUse = Double.parseDouble(editTextLimitPrice.getText().toString());
            } catch (NumberFormatException e) {
            }
        } else {
            priceToUse = (currentPrice > 0 ? currentPrice : 98.76); // Use actual or placeholder current price
        }

        double estimatedProceeds = quantity * priceToUse;
        textViewEstimatedProceedsValue.setText(Utils.formatCurrency(estimatedProceeds));
    }

    private void placeSellOrder() {
        // --- Input Validation (Simplified) ---
        String quantityStr = editTextQuantity.getText().toString();
        String limitPriceStr = editTextLimitPrice.getText().toString();
        double quantity;
        Double limitPrice = null;

        if (TextUtils.isEmpty(quantityStr)) {
            editTextQuantity.setError("Quantity is required.");
            editTextQuantity.requestFocus();
            return;
        }
        try {
            quantity = Double.parseDouble(quantityStr);
            if (quantity <= 0) {
                 editTextQuantity.setError("Quantity must be positive.");
                 editTextQuantity.requestFocus();
                 return;
            }
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Invalid quantity.");
            editTextQuantity.requestFocus();
            return;
        }

        if ("Limit".equalsIgnoreCase(selectedOrderType)) {
            if (TextUtils.isEmpty(limitPriceStr)) {
                editTextLimitPrice.setError("Limit price is required for Limit orders.");
                editTextLimitPrice.requestFocus();
                return;
            }
            try {
                limitPrice = Double.parseDouble(limitPriceStr);
                if (limitPrice <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                editTextLimitPrice.setError("Invalid limit price.");
                editTextLimitPrice.requestFocus();
                return;
            }
        }

        String orderDetails = selectedOrderType + " sell order for " + quantity + " of " + (assetSymbol != null ? assetSymbol : "DUMMY_ASSET");
        if (limitPrice != null) {
            orderDetails += " at limit price " + Utils.formatCurrency(limitPrice);
        }
        Toast.makeText(SellCoinActivity.this, orderDetails + " simulated.", Toast.LENGTH_LONG).show();

    }

    private void showError(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

