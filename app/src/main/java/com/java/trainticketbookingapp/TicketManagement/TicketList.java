package com.java.trainticketbookingapp.TicketManagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.AccountManagement.PaymentActivity;
import com.java.trainticketbookingapp.Adapter.TicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketList extends AppCompatActivity {

    private TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID, tv_bookingPassID;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(currentDate);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tickets");

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Create a new Ticket object
                    Ticket ticket = new Ticket();

                    String departureTime = childSnapshot.child("departureTime").getValue(String.class);
                    String ticketStart = childSnapshot.child("start").getValue(String.class);
                    String ticketDestination = childSnapshot.child("destination").getValue(String.class);

                    String formattedDepartureTime = convertDepartureTime(departureTime);

                    if (!isDeparted(formattedDepartureTime, "1h00") && savedDepartureName.equals(ticketStart) && savedDestination.equals(ticketDestination)) {
//
                        ticket.setId(Integer.parseInt(childSnapshot.getKey().substring("ticket".length())));
                        ticket.setStart(childSnapshot.child("start").getValue(String.class));
                        ticket.setDestination(childSnapshot.child("destination").getValue(String.class));
                        if (childSnapshot.child("prices").getValue() instanceof Long) {
                            long priceLong = (long) childSnapshot.child("prices").getValue();
                            ticket.setPrice(String.valueOf(priceLong)); // Store as String
                        } else {
                            ticket.setPrice(childSnapshot.child("prices").getValue(String.class));
                        }
                        ticket.setTotalTime(childSnapshot.child("totalTime").getValue(String.class));
                        ticket.setDepartureTime(childSnapshot.child("departureTime").getValue(String.class));
                        ticket.setArrivalTime(childSnapshot.child("arrivalTime").getValue(String.class));
                        ticket.setTrainID(childSnapshot.child("trainID").getValue(String.class));
                        ticket.setDate(childSnapshot.child("date").getValue(String.class));

                        ticketList.add(ticket);
                    }
                }
                ticketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error reading data", error.toException());
            }
        });

        ticketAdapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ticketAdapter.setOnTicketClickListener(new TicketAdapter.OnTicketClickListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                startPaymentActivity(ticket);
            }
        });

    }


    // Convert time format
    private String convertDepartureTime(String originalDepartureTime)
    {
        try {
            // Define the format for parsing the original string
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH'h'MM");

            // Parse the original string into a Date object
            Date parsedDate = originalFormat.parse(originalDepartureTime);

            // Define the format for formatting the Date object
            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm");

            // Format the Date object into the desired output format
            return targetFormat.format(parsedDate);

        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return originalDepartureTime; // Return the original string in case of error
        }
    }

    // Compare departure time with current time
    private boolean isDeparted(String departureTime) {
        try {
            // Define the format for parsing the original string
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm");

            // Parse the original string into a Date object
            Date parsedDepartureTime = originalFormat.parse(departureTime);

            // Get the current time
            Date currentDate = new Date();
            String currentTime = originalFormat.format(currentDate);

            // Parse the current time into a Date object
            Date parsedCurrentTime = originalFormat.parse(currentTime);

            // Check if the current time is after the departure time
            return parsedCurrentTime.after(parsedDepartureTime);

        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return false; // Return false in case of error
        }
    }

    // Test with specific time
    private boolean isDeparted(String departureTime, String fixedTime) {
        try {
            // Define the format for parsing the original string
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm");

            // Parse the original and fixed strings into Date objects
            Date parsedDepartureTime = originalFormat.parse(departureTime);
            Date parsedFixedTime = originalFormat.parse(fixedTime);

            // Check if the fixed time is after the departure time
            return parsedFixedTime.after(parsedDepartureTime);

        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return false; // Return false in case of error
        }
    }

    private void startPaymentActivity(Ticket selectedTicket) {
        Intent paymentIntent = new Intent(TicketList.this, PaymentActivity.class);
        paymentIntent.putExtra("ticket_id", selectedTicket.getId());
        paymentIntent.putExtra("ticket_destination", selectedTicket.getDestination());
        paymentIntent.putExtra("ticket_price", selectedTicket.getPrice());
        paymentIntent.putExtra("ticket_start", selectedTicket.getStart());
        paymentIntent.putExtra("ticket_arrivalTime", selectedTicket.getArrivalTime());
        paymentIntent.putExtra("ticket_totalTime", selectedTicket.getTotalTime());
        paymentIntent.putExtra("ticket_departureTime", selectedTicket.getDepartureTime());
        paymentIntent.putExtra("ticket_trainID", selectedTicket.getTrainID());
        paymentIntent.putExtra("ticket_date", selectedTicket.getDate());

        startActivity(paymentIntent);
        // If you need a result from PaymentActivity, consider using startActivityForResult instead.
    }

}