package com.java.trainticketbookingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TicketList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TicketList.this, HomeFragment.class);
            startActivity(intent);
            
        });
    }
}
