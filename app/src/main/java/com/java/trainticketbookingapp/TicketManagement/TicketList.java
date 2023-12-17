package com.java.trainticketbookingapp.TicketManagement;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.trainticketbookingapp.Adapter.TicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.ArrayList;
import java.util.List;

public class TicketList extends AppCompatActivity {

    private TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
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

        recyclerView = findViewById(R.id.recy_tripID);
        ticketList = new ArrayList<>();

        int i = 1;
        for (i = 1; i < 10; i++) {
            ticketList.add(new Ticket("null", "Location A", "Location B", 1500000, "2 hours", "10:00 AM"));
        }


        ticketAdapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}