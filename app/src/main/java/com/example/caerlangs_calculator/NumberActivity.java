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

import java.math.BigInteger;

public class NumberActivity extends AppCompatActivity {

    private EditText etAmount;
    private AutoCompleteTextView spinnerFrom, spinnerTo;
    private TextView tvResult;

    // Constants for Bases
    private final String DECIMAL = "Decimal (Base 10)";
    private final String BINARY = "Binary (Base 2)";
    private final String OCTAL = "Octal (Base 8)";
    private final String HEXADECIMAL = "Hexadecimal (Base 16)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        etAmount = findViewById(R.id.etNumberAmount);
        spinnerFrom = findViewById(R.id.spinnerFromNumber);
        spinnerTo = findViewById(R.id.spinnerToNumber);
        tvResult = findViewById(R.id.tvNumberResult);
        MaterialButton btnConvert = findViewById(R.id.btnConvertNumber);

        // Populate the dropdowns
        String[] bases = {DECIMAL, BINARY, OCTAL, HEXADECIMAL};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                bases
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
        btnConvert.setOnClickListener(v -> executeConversion());
    }

    private void executeConversion() {
        String amountText = etAmount.getText().toString().trim().toUpperCase();

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        int fromRadix = getRadixForBase(spinnerFrom.getText().toString());
        int toRadix = getRadixForBase(spinnerTo.getText().toString());

        try {
            // BigInteger parses the string based on its original radix (Base)
            BigInteger baseValue = new BigInteger(amountText, fromRadix);

            // Then it perfectly converts it to the target radix string
            String finalResult = baseValue.toString(toRadix).toUpperCase();

            tvResult.setText(finalResult);

        } catch (NumberFormatException e) {
            // If the user types "9" but sets the dropdown to "Binary", it will crash.
            // This try/catch block intercepts the crash and shows a friendly error!
            Toast.makeText(this, "Invalid characters for selected base!", Toast.LENGTH_LONG).show();
        }
    }

    // Helper method to convert the dropdown string into actual mathematical bases
    private int getRadixForBase(String baseString) {
        switch (baseString) {
            case BINARY: return 2;
            case OCTAL: return 8;
            case HEXADECIMAL: return 16;
            case DECIMAL:
            default: return 10;
        }
    }
}