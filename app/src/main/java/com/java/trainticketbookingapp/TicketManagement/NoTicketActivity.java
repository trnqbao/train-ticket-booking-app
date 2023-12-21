package com.java.trainticketbookingapp.TicketManagement;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.trainticketbookingapp.R;

public class NoTicketActivity extends AppCompatActivity {
    private ImageButton imgBtnBack;
    private TextView tvStart, tvDes, tvDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_ticket_result);

        imgBtnBack = findViewById(R.id.btn_back);
        tvStart = findViewById(R.id.tv_bookingFromID);
        tvDes = findViewById(R.id.tv_bookingToID);
        tvDate = findViewById(R.id.tv_bookingDateID);

        String start = getIntent().getStringExtra("start");
        String des = getIntent().getStringExtra("des");
        String date = getIntent().getStringExtra("date");

        tvStart.setText(start);
        tvDes.setText(des);
        tvDate.setText(date);

        imgBtnBack.setOnClickListener(v -> onBackPressed());

    }

}
