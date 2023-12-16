package com.java.trainticketbookingapp.TicketManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.trainticketbookingapp.R;

public class TicketList extends AppCompatActivity {

    TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        tv_bookingFromID = findViewById(R.id.tv_bookingFromID);
        tv_bookingToID = findViewById(R.id.tv_bookingToID);
        tv_bookingDateID = findViewById(R.id.tv_bookingDateID);
//        tv_bookingPassID = findViewById(R.id.tv_bookingPassID);

        String savedDepartureName = getIntent().getStringExtra("bookingFromID");
        String savedDestination = getIntent().getStringExtra("bookingToID");
//        String savedPassenger = getIntent().getStringExtra("passenger");
        String savedDate = getIntent().getStringExtra("date");

        tv_bookingFromID.setText(String.valueOf(savedDepartureName));
        tv_bookingToID.setText(String.valueOf(savedDestination));
//        tv_bookingPassID.setText(savedPassenger);
        tv_bookingDateID.setText(savedDate);

        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> onBackPressed());

    }
}