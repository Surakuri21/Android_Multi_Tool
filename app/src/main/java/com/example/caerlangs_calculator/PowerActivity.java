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

public class PowerActivity extends AppCompatActivity {

    private EditText etAmount;
    private AutoCompleteTextView spinnerFrom, spinnerTo;
    private TextView tvResult;

    // Anchor everything to 1 Watt (W)
    private final Map<String, Double> powerRates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        etAmount = findViewById(R.id.etPowerAmount);
        spinnerFrom = findViewById(R.id.spinnerFromPower);
        spinnerTo = findViewById(R.id.spinnerToPower);
        tvResult = findViewById(R.id.tvPowerResult);
        MaterialButton btnConvert = findViewById(R.id.btnConvertPower);

        // Map values relative to 1 Watt
        powerRates.put("Watts (W)", 1.0);
        powerRates.put("Kilowatts (kW)", 1000.0);
        powerRates.put("Megawatts (MW)", 1000000.0);
        powerRates.put("Milliwatts (mW)", 0.001);
        powerRates.put("Horsepower (hp)", 745.7);

        // Populate the dropdowns
        String[] units = {"Watts (W)", "Kilowatts (kW)", "Megawatts (MW)", "Milliwatts (mW)", "Horsepower (hp)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Back button action
        btnBack.setOnClickListener(v -> finish());

        // Convert button action
        btnConvert.setOnClickListener(v -> executeConversion());
    }

    private void executeConversion() {
        String amountText = etAmount.getText().toString().trim();

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputAmount = Double.parseDouble(amountText);
        String fromUnit = spinnerFrom.getText().toString();
        String toUnit = spinnerTo.getText().toString();

        if (!powerRates.containsKey(fromUnit) || !powerRates.containsKey(toUnit)) {
            Toast.makeText(this, "Invalid unit selection", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Convert input to base Watts
        double amountInWatts = inputAmount * powerRates.get(fromUnit);

        // 2. Convert base Watts to Target Unit
        double finalConvertedValue = amountInWatts / powerRates.get(toUnit);

        // Format dynamically to handle very small or very large numbers beautifully
        tvResult.setText(formatResult(finalConvertedValue));
    }

    // Helper method to keep the display clean
    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        } else {
            return String.format(Locale.US, "%.4f", value);
        }
    }
}