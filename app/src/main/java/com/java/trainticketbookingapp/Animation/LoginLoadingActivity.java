package com.java.trainticketbookingapp.Animation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.java.trainticketbookingapp.AccountManagement.MainActivity;
import com.java.trainticketbookingapp.R;

@SuppressLint("CustomSplashScreen")
public class LoginLoadingActivity extends AppCompatActivity {
    private static final long SPLASH_SREEN_TIMOUT = 1200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_loading);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoginLoadingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SREEN_TIMOUT);
    }
}
