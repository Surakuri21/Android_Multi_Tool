package com.example.caerlangs_calculator;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {

    private TextView tvEquation, tvResult;
    private boolean isCalculated = false; // Tracks if we just hit the "=" button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                if (isCalculated) {
                    // If we just finished a calculation, start a brand new one
                    tvResult.setText(btn.getText());
                    tvEquation.setText("");
                    isCalculated = false;
                } else {
                    String currentText = tvResult.getText().toString();
                    if (currentText.equals("0") || currentText.equals("Error")) {
                        tvResult.setText(btn.getText()); // Replace the starting "0"
                    } else {
                        tvResult.append(btn.getText()); // Add the number to our string
                    }
                }
                updateLiveResult(); // Instantly calculate the auto-answer!
            });
        }

        // 4. Setup Decimal (.)
        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (isCalculated) {
                tvResult.setText("0.");
                tvEquation.setText("");
                isCalculated = false;
            } else {
                String currentText = tvResult.getText().toString();
                // Check if the current block of numbers already has a decimal
                String[] parts = currentText.split(" ");
                String lastPart = parts[parts.length - 1];

                if (!lastPart.contains(".")) {
                    tvResult.append(".");
                }
            }
        });

        // 5. Setup Clear (C)
        findViewById(R.id.btnC).setOnClickListener(v -> {
            tvResult.setText("0");
            tvEquation.setText("");
            isCalculated = false;
        });

        // 6. Setup Smart Backspace
        findViewById(R.id.btnBackspace).setOnClickListener(v -> {
            if (isCalculated) {
                tvEquation.setText("");
                isCalculated = false;
            }

            String currentText = tvResult.getText().toString();

            if (currentText.equals("Error") || currentText.length() <= 1) {
                tvResult.setText("0");
                tvEquation.setText("");
            } else {
                // If it ends with a space (meaning it's an operator like " + "), delete all 3 characters
                if (currentText.endsWith(" ")) {
                    tvResult.setText(currentText.substring(0, currentText.length() - 3));
                } else {
                    // Otherwise just delete the last number
                    tvResult.setText(currentText.substring(0, currentText.length() - 1));
                }
                updateLiveResult(); // Update the auto-answer after deleting
            }
        });

        // 7. Setup Plus/Minus Toggle (+/-)
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> {
            try {
                // Instantly calculate whatever is on screen and make it negative
                double result = evaluateMath(tvResult.getText().toString());
                result = result * -1;
                tvResult.setText(formatNumber(result));
                updateLiveResult();
            } catch (Exception ignored) {}
        });

        // 8. Setup Percent (%)
        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            try {
                double result = evaluateMath(tvResult.getText().toString());
                result = result / 100.0;
                tvResult.setText(formatNumber(result));
                updateLiveResult();
            } catch (Exception ignored) {}
        });

        // 9. Setup Operators (+, -, ×, ÷)
        findViewById(R.id.btnPlus).setOnClickListener(v -> appendOperator("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> appendOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> appendOperator("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> appendOperator("÷"));

        // 10. Setup Equals (=)
        findViewById(R.id.btnEquals).setOnClickListener(v -> {
            String expression = tvResult.getText().toString();
            try {
                double result = evaluateMath(expression);
                // Move the math string to the top, and put the final answer on the main screen
                tvEquation.setText(expression);
                tvResult.setText(formatNumber(result));
                isCalculated = true;
            } catch (Exception e) {
                tvResult.setText("Error");
                isCalculated = true;
            }
        });
    }

    // --- HELPER METHODS ---

    private void appendOperator(String op) {
        if (isCalculated) {
            isCalculated = false;
            tvEquation.setText(""); // Keep the result, but clear the old top equation
        }

        String currentText = tvResult.getText().toString();
        if (currentText.equals("Error")) currentText = "0";

        // If the user tries to type two operators in a row, replace the old one
        if (currentText.endsWith(" + ") || currentText.endsWith(" - ") ||
                currentText.endsWith(" × ") || currentText.endsWith(" ÷ ")) {
            tvResult.setText(currentText.substring(0, currentText.length() - 3) + " " + op + " ");
        } else {
            // Add spaces around the operator so it looks clean (e.g. "34 + ")
            tvResult.append(" " + op + " ");
        }
    }

    private void updateLiveResult() {
        String expression = tvResult.getText().toString();

        // Only show live preview if there is an operator involved
        if (expression.contains(" + ") || expression.contains(" - ") ||
                expression.contains(" × ") || expression.contains(" ÷ ")) {
            try {
                double result = evaluateMath(expression);
                tvEquation.setText(formatNumber(result));
            } catch (Exception e) {
                // If they are halfway through typing (e.g., "34 + "), hide the preview
                tvEquation.setText("");
            }
        } else {
            tvEquation.setText(""); // Hide preview if it's just a single number
        }
    }

    private String formatNumber(double value) {
        // DecimalFormat automatically adds commas for thousands and drops unnecessary trailing zeros!
        DecimalFormat df = new DecimalFormat("#,###.##########");
        return df.format(value);
    }

    // =========================================================================
    // ADVANCED MATH PARSER
    // This reads a string like "34 + 45" and perfectly evaluates it mathematically!
    // =========================================================================
    private double evaluateMath(String str) throws Exception {
        // Swap out visual symbols for real math symbols, remove spaces AND COMMAS!
        String tempStr = str.replace("×", "*").replace("÷", "/").replace(" ", "").replace(",", "");

        // If the string ends with a dangling operator (like "34+"), ignore the operator for preview
        if (tempStr.endsWith("+") || tempStr.endsWith("-") || tempStr.endsWith("*") || tempStr.endsWith("/")) {
            tempStr = tempStr.substring(0, tempStr.length() - 1);
        }

        // Lock it in! Now cleanStr is perfectly final so the inner class is happy.
        final String cleanStr = tempStr;

        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < cleanStr.length()) ? cleanStr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() throws Exception {
                nextChar();
                double x = parseExpression();
                if (pos < cleanStr.length()) throw new Exception("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() throws Exception {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() throws Exception {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) throw new Exception("Division by Zero");
                        x /= divisor; // division
                    }
                    else return x;
                }
            }

            double parseFactor() throws Exception {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(cleanStr.substring(startPos, this.pos));
                } else {
                    throw new Exception("Unexpected: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }
}