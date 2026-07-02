package com.example.caerlangs_calculator;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {

    private TextView tvEquation, tvResult;
    private double valueOne = Double.NaN;
    private double valueTwo;
    private char currentAction = '0';
    private boolean isTypingNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Updated to use your newly renamed XML layout file
        setContentView(R.layout.activity_calculator);

        // 1. Header Navigation
        ImageButton btnBack = findViewById(R.id.btnBackToDashboard);
        btnBack.setOnClickListener(v -> finish());

        // 2. Setup Displays
        tvEquation = findViewById(R.id.tvEquation);
        tvResult = findViewById(R.id.tvResult);

        // 3. Setup Number Buttons (0-9)
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(v -> {
                MaterialButton btn = (MaterialButton) v;
                // If we just clicked an operator, or the screen only says "0" (or "Error"), replace the text
                if (!isTypingNumber || tvResult.getText().toString().equals("0") || tvResult.getText().toString().equals("Error")) {
                    tvResult.setText(btn.getText());
                } else {
                    // Otherwise, append the number
                    tvResult.append(btn.getText());
                }
                isTypingNumber = true;
            });
        }

        // 4. Setup Decimal (.)
        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!isTypingNumber) {
                tvResult.setText("0.");
                isTypingNumber = true;
            } else if (!tvResult.getText().toString().contains(".")) {
                tvResult.append(".");
            }
        });

        // 5. Setup Clear (C)
        findViewById(R.id.btnC).setOnClickListener(v -> clearAll());

        // 6. Setup Backspace
        findViewById(R.id.btnBackspace).setOnClickListener(v -> {
            String currentText = tvResult.getText().toString();

            // Only backspace if it's not an Error or already at "0"
            if (!currentText.equals("Error") && !currentText.equals("0")) {
                if (currentText.length() > 1 && !(currentText.length() == 2 && currentText.startsWith("-"))) {
                    // Chop off the last character
                    tvResult.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    // If only 1 number left (or just a minus sign), reset to 0
                    tvResult.setText("0");
                    isTypingNumber = false;
                }
            }
        });

        // 7. Setup Plus/Minus Toggle (+/-)
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> {
            String current = tvResult.getText().toString();
            if (!current.equals("0") && !current.equals("Error") && !current.isEmpty()) {
                if (current.startsWith("-")) {
                    tvResult.setText(current.substring(1)); // Remove negative
                } else {
                    tvResult.setText("-" + current); // Add negative
                }
            }
        });

        // 8. Setup Percent (%)
        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            try {
                double current = Double.parseDouble(tvResult.getText().toString());
                tvResult.setText(formatNumber(current / 100));
                isTypingNumber = false;
            } catch (Exception e) {
                tvResult.setText("Error");
            }
        });

        // 9. Setup Operators
        findViewById(R.id.btnPlus).setOnClickListener(v -> handleOperator('+'));
        findViewById(R.id.btnMinus).setOnClickListener(v -> handleOperator('-'));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> handleOperator('×'));
        findViewById(R.id.btnDivide).setOnClickListener(v -> handleOperator('÷'));

        // 10. Setup Equals (=)
        findViewById(R.id.btnEquals).setOnClickListener(v -> {
            calculate();
            currentAction = '0';
            tvEquation.setText(""); // Clear the top equation display
            isTypingNumber = false; // Ready for a fresh number
        });
    }

    private void handleOperator(char action) {
        String currentText = tvResult.getText().toString();

        if (currentText.equals("Error")) return;

        // If we are currently typing a number and already have a value stored, calculate it first (e.g. 5 + 5 +)
        if (!Double.isNaN(valueOne) && isTypingNumber) {
            calculate();
        } else {
            // Otherwise, just store the current number on the screen
            try {
                valueOne = Double.parseDouble(currentText);
            } catch (Exception e) {
                return;
            }
        }

        currentAction = action;
        tvEquation.setText(formatNumber(valueOne) + " " + currentAction);
        isTypingNumber = false; // Next tap will start a new number
    }

    private void calculate() {
        if (!Double.isNaN(valueOne) && currentAction != '0') {
            try {
                valueTwo = Double.parseDouble(tvResult.getText().toString());
                switch (currentAction) {
                    case '+': valueOne += valueTwo; break;
                    case '-': valueOne -= valueTwo; break;
                    case '×': valueOne *= valueTwo; break;
                    case '÷':
                        if (valueTwo != 0) {
                            valueOne /= valueTwo;
                        } else {
                            valueOne = Double.NaN; // Handle divide by zero
                        }
                        break;
                }

                if (Double.isNaN(valueOne) || Double.isInfinite(valueOne)) {
                    tvResult.setText("Error");
                    valueOne = Double.NaN;
                } else {
                    tvResult.setText(formatNumber(valueOne));
                }
            } catch (Exception e) {
                tvResult.setText("Error");
            }
        }
    }

    private void clearAll() {
        valueOne = Double.NaN;
        valueTwo = Double.NaN;
        currentAction = '0';
        tvResult.setText("0");
        tvEquation.setText("");
        isTypingNumber = false;
    }

    // Helper to keep numbers clean (e.g., 5.0 becomes 5)
    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        } else {
            return String.format(Locale.US, "%s", value);
        }
    }
}