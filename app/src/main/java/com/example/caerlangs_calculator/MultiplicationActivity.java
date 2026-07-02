package com.example.caerlangs_calculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class MultiplicationActivity extends AppCompatActivity {

    private EditText etBaseNumber;
    private TextView tvTableResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication);

        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        etBaseNumber = findViewById(R.id.etBaseNumber);
        tvTableResult = findViewById(R.id.tvTableResult);
        MaterialButton btnGenerate = findViewById(R.id.btnGenerateTable);

        // Handle Back Navigation
        btnBack.setOnClickListener(v -> finish());

        // Handle Generation
        btnGenerate.setOnClickListener(v -> generateTable());
    }

    private void generateTable() {
        String numberText = etBaseNumber.getText().toString().trim();

        // Guard Statement: Validate Input
        if (numberText.isEmpty() || numberText.equals("-") || numberText.equals(".")) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        double baseNumber = Double.parseDouble(numberText);

        // Use a StringBuilder because appending text in a loop is much faster and cleaner
        StringBuilder tableBuilder = new StringBuilder();

        // Generate the table from 1 to 12
        for (int i = 1; i <= 12; i++) {
            double result = baseNumber * i;

            // Format cleanly to remove ".0" for whole numbers
            String formattedBase = formatNumber(baseNumber);
            String formattedResult = formatNumber(result);

            // Append the math equation string with a line break
            tableBuilder.append(formattedBase)
                    .append(" x ")
                    .append(i)
                    .append(" = ")
                    .append(formattedResult)
                    .append("\n");
        }

        // Set the final massive string block to the TextView
        tvTableResult.setText(tableBuilder.toString());
    }

    // Helper method to keep decimals looking clean
    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        } else {
            return String.format(Locale.US, "%.2f", value);
        }
    }
}