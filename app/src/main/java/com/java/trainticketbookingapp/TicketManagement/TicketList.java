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

    TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

<<<<<<< Updated upstream:app/src/main/java/com/java/trainticketbookingapp/TicketManagement/TicketList.java
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

=======
        tv_bookingFromID = findViewById(R.id.tv_bookingFromID);
        tv_bookingToID = findViewById(R.id.tv_bookingToID);
        tv_bookingDateID = findViewById(R.id.tv_bookingDateID);
        tv_bookingPassID = findViewById(R.id.tv_bookingPassID);

        String savedDepartureName = getIntent().getStringExtra("bookingFromID");
        String savedDestination = getIntent().getStringExtra("bookingToID");
        String savedPassenger = getIntent().getStringExtra("passenger");
        String savedDate = getIntent().getStringExtra("date");

        tv_bookingFromID.setText(String.valueOf(savedDepartureName));
        tv_bookingToID.setText(String.valueOf(savedDestination));
        tv_bookingPassID.setText(savedPassenger);
        tv_bookingDateID.setText(savedDate);

        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> onBackPressed());
>>>>>>> Stashed changes:app/src/main/java/com/java/trainticketbookingapp/TicketList.java

    }


}
