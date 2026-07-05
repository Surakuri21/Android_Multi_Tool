package com.example.caerlangs_calculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo = findViewById(R.id.ivSplashLogo);
        TextView tvBrand = findViewById(R.id.tvBrandName);
        TextView tvTitle = findViewById(R.id.tvAppTitle);

        // 1. Setup Starting State (Invisible and slightly smaller)
        ivLogo.setAlpha(0f);
        ivLogo.setScaleX(0.5f);
        ivLogo.setScaleY(0.5f);

        tvBrand.setAlpha(0f);
        tvTitle.setAlpha(0f);

        // 2. Animate Logo (Fade in and scale up to full size over 1 second)
        ivLogo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000).start();

        // 3. Animate Text (Fade in slightly after the logo starts)
        tvBrand.animate().alpha(1f).setDuration(1000).setStartDelay(400).start();
        tvTitle.animate().alpha(1f).setDuration(1000).setStartDelay(600).start();

        // 4. Wait 2.5 seconds, then transition to Dashboard
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);

            // Apply a smooth fade transition between screens
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Destroy the Splash Screen so the user can't click "Back" into it
            finish();
        }, 2500);
    }
}