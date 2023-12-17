package com.java.trainticketbookingapp.TicketManagement;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.java.trainticketbookingapp.Adapter.TicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.List;

public class Booking extends AppCompatActivity {

    private TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_seat);


        tv_bookingFromID = findViewById(R.id.tv_bookingFromID);
        tv_bookingToID = findViewById(R.id.tv_bookingToID);
        tv_bookingDateID = findViewById(R.id.tv_bookingDateID);
//        tv_bookingPassID = findViewById(R.id.tv_bookingPassID);

        int savedDeparture = getIntent().getIntExtra("departure", 0);
        int savedDestination = getIntent().getIntExtra("destination", 0);
//        String savedPassenger = getIntent().getStringExtra("passenger");
        String savedDate = getIntent().getStringExtra("date");

        // Set the saved values to the text views
        tv_bookingFromID.setText(savedDeparture); // replace with actual data
        tv_bookingToID.setText(savedDestination); // replace with actual data
//        tv_bookingPassID.setText(savedPassenger);
        tv_bookingDateID.setText(savedDate);



    }
}
