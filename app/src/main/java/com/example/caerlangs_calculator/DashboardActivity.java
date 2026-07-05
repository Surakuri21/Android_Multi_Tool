package com.example.caerlangs_calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize custom MaterialCard components from the XML layout
        MaterialCardView cardCalculator = findViewById(R.id.btnNavCalculator);
        MaterialCardView cardCurrency = findViewById(R.id.btnNavCurrency);
        MaterialCardView cardPower = findViewById(R.id.btnNavPower);
        MaterialCardView cardTemperature = findViewById(R.id.btnNavTemperature);
        MaterialCardView cardNumber = findViewById(R.id.btnNavNumber);
        MaterialCardView cardMultiplication = findViewById(R.id.btnNavMultiplication);


        // --- Set Click Listeners for all 6 tools! ---

        cardCalculator.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        cardCurrency.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, CurrencyActivity.class);
            startActivity(intent);
        });

        cardPower.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, PowerActivity.class);
            startActivity(intent);
        });

        cardTemperature.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, TemperatureActivity.class);
            startActivity(intent);
        });

        cardNumber.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, NumberActivity.class);
            startActivity(intent);
        });

        cardMultiplication.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, MultiplicationActivity.class);
            startActivity(intent);
        });


    }
}