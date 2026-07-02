package com.example.caerlangs_calculator;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CurrencyActivity extends AppCompatActivity {

    private EditText etAmount;
    private AutoCompleteTextView spinnerFrom, spinnerTo;
    private TextView tvConvertedResult;

    // Static Exchange Rate Map anchoring everything relative to 1 USD
    private final Map<String, Double> exchangeRates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        // 1. Initialize UI Elements
        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        etAmount = findViewById(R.id.etAmount);
        spinnerFrom = findViewById(R.id.spinnerFromCurrency);
        spinnerTo = findViewById(R.id.spinnerToCurrency);
        tvConvertedResult = findViewById(R.id.tvConvertedResult);
        MaterialButton btnConvert = findViewById(R.id.btnConvert);

        // 2. Initialize Seed Exchange Rates (Base: 1 USD)
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.92);  // 1 USD = 0.92 EUR
        exchangeRates.put("PHP", 58.50); // 1 USD = 58.50 PHP
        exchangeRates.put("JPY", 155.20); // 1 USD = 155.20 JPY

        // 3. Populate Material Dropdown Menu Options
        String[] currencies = {"USD", "EUR", "PHP", "JPY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                currencies
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // 4. Handle Back Arrow Navigation
        btnBack.setOnClickListener(v -> finish());

        // 5. Handle Calculation Action Sequence
        btnConvert.setOnClickListener(v -> executeConversion());
    }

    private void executeConversion() {
        String amountText = etAmount.getText().toString().trim();

        // Validation Guard Statement: Verify input presence
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter an amount to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputAmount = Double.parseDouble(amountText);
        String fromCurrency = spinnerFrom.getText().toString();
        String toCurrency = spinnerTo.getText().toString();

        // Validation Guard Statement: Verify items exist in index mapping
        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            Toast.makeText(this, "Invalid currency selection", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Core Conversion Equation Engine ---
        // 1. Convert source amount back to standard anchor value (USD)
        double amountInUSD = inputAmount / exchangeRates.get(fromCurrency);
        // 2. Convert anchor value to destination target value
        double finalConvertedValue = amountInUSD * exchangeRates.get(toCurrency);

        // Render result cleanly formatted to 2 decimal places
        tvConvertedResult.setText(String.format(Locale.US, "%.2f", finalConvertedValue));
    }
}