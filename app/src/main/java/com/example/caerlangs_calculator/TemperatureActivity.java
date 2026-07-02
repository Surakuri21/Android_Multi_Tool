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

import java.util.Locale;

public class TemperatureActivity extends AppCompatActivity {

    private EditText etAmount;
    private AutoCompleteTextView spinnerFrom, spinnerTo;
    private TextView tvResult;

    // Constants for our dropdown strings to prevent typos
    private final String CELSIUS = "Celsius (°C)";
    private final String FAHRENHEIT = "Fahrenheit (°F)";
    private final String KELVIN = "Kelvin (K)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        etAmount = findViewById(R.id.etTempAmount);
        spinnerFrom = findViewById(R.id.spinnerFromTemp);
        spinnerTo = findViewById(R.id.spinnerToTemp);
        tvResult = findViewById(R.id.tvTempResult);
        MaterialButton btnConvert = findViewById(R.id.btnConvertTemp);

        // Populate the dropdowns
        String[] scales = {CELSIUS, FAHRENHEIT, KELVIN};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                scales
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
        btnConvert.setOnClickListener(v -> executeConversion());
    }

    private void executeConversion() {
        String amountText = etAmount.getText().toString().trim();

        if (amountText.isEmpty() || amountText.equals("-") || amountText.equals(".")) {
            Toast.makeText(this, "Please enter a valid temperature", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputTemp = Double.parseDouble(amountText);
        String fromScale = spinnerFrom.getText().toString();
        String toScale = spinnerTo.getText().toString();

        // 1. Convert everything to our Base scale (Celsius)
        double tempInCelsius = 0.0;

        switch (fromScale) {
            case CELSIUS:
                tempInCelsius = inputTemp;
                break;
            case FAHRENHEIT:
                tempInCelsius = (inputTemp - 32) * 5 / 9;
                break;
            case KELVIN:
                tempInCelsius = inputTemp - 273.15;
                break;
            default:
                Toast.makeText(this, "Invalid scale selection", Toast.LENGTH_SHORT).show();
                return;
        }

        // 2. Convert from our Base (Celsius) to the Target scale
        double finalTemp = 0.0;

        switch (toScale) {
            case CELSIUS:
                finalTemp = tempInCelsius;
                break;
            case FAHRENHEIT:
                finalTemp = (tempInCelsius * 9 / 5) + 32;
                break;
            case KELVIN:
                finalTemp = tempInCelsius + 273.15;
                break;
        }

        // Display the result
        tvResult.setText(formatResult(finalTemp));
    }

    // Helper method for clean decimal formatting
    private String formatResult(double value) {
        // Check if it's a whole number, otherwise show 2 decimals
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        } else {
            return String.format(Locale.US, "%.2f", value);
        }
    }
}