package com.java.trainticketbookingapp.TicketManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.trainticketbookingapp.Fragment.HomeFragment;
import com.java.trainticketbookingapp.R;

public class TicketList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent data = getIntent();
        int passenger = data.getIntExtra("passenger", 1);
        Log.e("TAG", passenger+"" );

        TextView tvPassenger = (TextView) findViewById(R.id.tv_bookingPass);
        tvPassenger.setText(passenger + " Passenger");

        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TicketList.this, HomeFragment.class);
            startActivity(intent);
            
        });


    }


}
