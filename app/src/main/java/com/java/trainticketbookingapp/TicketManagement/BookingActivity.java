package com.java.trainticketbookingapp.TicketManagement;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.trainticketbookingapp.R;

public class BookingActivity extends AppCompatActivity {

    private TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_seat);

        tv_bookingFromID = findViewById(R.id.tv_bookingFromID);
        tv_bookingToID = findViewById(R.id.tv_bookingToID);
        tv_bookingDateID = findViewById(R.id.tv_bookingDateID);

        int savedDeparture = getIntent().getIntExtra("departure", 0);
        int savedDestination = getIntent().getIntExtra("destination", 0);
        String savedDate = getIntent().getStringExtra("date");

        // Set the saved values to the text views
        tv_bookingFromID.setText(savedDeparture);
        tv_bookingToID.setText(savedDestination);
        tv_bookingDateID.setText(savedDate);



    }
}
