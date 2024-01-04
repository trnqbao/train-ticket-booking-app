package com.java.trainticketbookingapp.TicketManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Adapter.TicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.java.trainticketbookingapp.Fragment.HomeFragment.check;

public class TicketListActivity extends AppCompatActivity {

    public static String formattedDate;
    static final String TAG = "TicketList";
    private TextView tv_bookingFromID, tv_bookingToID, tv_bookingDateID;
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LinearLayout recyLayout;
    FirebaseUser user;
    FirebaseAuth auth;
    private int point = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tickets");

        tv_bookingFromID = findViewById(R.id.tv_bookingFromID);
        tv_bookingToID = findViewById(R.id.tv_bookingToID);
        tv_bookingDateID = findViewById(R.id.tv_bookingDateID);

        recyLayout = findViewById(R.id.recy_layout);

        String savedDepartureName = getIntent().getStringExtra("bookingFromID");
        String savedDestination = getIntent().getStringExtra("bookingToID");
        String savedDate = getIntent().getStringExtra("date");

        String formattedDate = formatDate(savedDate);


        tv_bookingFromID.setText(String.valueOf(savedDepartureName));
        tv_bookingToID.setText(String.valueOf(savedDestination));
        tv_bookingDateID.setText(formattedDate);

        LayoutInflater inflater = LayoutInflater.from(this);
        View noTicketView = inflater.inflate(R.layout.activity_no_ticket_result, recyLayout, false);

        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recy_tripID);
        ticketList = new ArrayList<>();
        point = getUserPoint(user);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ticketList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Ticket ticket = new Ticket();

                    String departureTime = childSnapshot.child("departureTime").getValue(String.class);
                    String ticketStart = childSnapshot.child("start").getValue(String.class);
                    String ticketDestination = childSnapshot.child("destination").getValue(String.class);

                    String formattedDepartureTime = convertDepartureTime(departureTime);

                    if (!isDeparted(formattedDepartureTime, "1h00") && savedDepartureName.equals(ticketStart) && savedDestination.equals(ticketDestination)) {

                        ticket.setId(Integer.parseInt(childSnapshot.getKey().substring("ticket".length())));
                        ticket.setStart(childSnapshot.child("start").getValue(String.class));
                        ticket.setDestination(childSnapshot.child("destination").getValue(String.class));
                        if (point == 0) {
                            if (childSnapshot.child("prices").getValue() instanceof Long) {
                                long priceLong = (long) childSnapshot.child("prices").getValue();
                                ticket.setPrice(String.valueOf(priceLong));
                            } else {
                                ticket.setPrice(childSnapshot.child("prices").getValue(String.class));
                            }
                        } else {
                            point = point * 100000;
                            if (childSnapshot.child("prices").getValue() instanceof Long) {
                                long priceLong = (long) childSnapshot.child("prices").getValue();
                                priceLong = priceLong - point;
                                ticket.setPrice(String.valueOf(priceLong));
                            } else {
                                ticket.setPrice(childSnapshot.child("prices").getValue(String.class));
                            }
                        }
                        ticket.setTotalTime(childSnapshot.child("totalTime").getValue(String.class));
                        ticket.setDepartureTime(childSnapshot.child("departureTime").getValue(String.class));
                        ticket.setArrivalTime(childSnapshot.child("arrivalTime").getValue(String.class));
                        ticket.setTrainID(childSnapshot.child("trainID").getValue(String.class));
                        ticket.setDepartureDate(savedDate);

                        ticketList.add(ticket);
                    }
                }
                ticketAdapter.notifyDataSetChanged();

                if (ticketAdapter.getItemCount() == 0) {
                    showNoTicketResult(noTicketView);
                } else {
                    hideNoTicketResutl(noTicketView);
                }

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

    private void showNoTicketResult(View noTicketView) {
        recyclerView.setVisibility(View.GONE);
        recyLayout.addView(noTicketView);
    }

    private void hideNoTicketResutl(View noTicket) {
        recyLayout.removeView(noTicket);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // Convert time format
    private String convertDepartureTime(String originalDepartureTime)
    {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH'h'MM");

            Date parsedDate = originalFormat.parse(originalDepartureTime);

            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm");

            return targetFormat.format(parsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return originalDepartureTime;
        }
    }

    // Compare departure time with current time
    private boolean isDeparted(String departureTime)
    {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm");

            Date parsedDepartureTime = originalFormat.parse(departureTime);
            Date currentDate = new Date();
            String currentTime = originalFormat.format(currentDate);

            Date parsedCurrentTime = originalFormat.parse(currentTime);

            return parsedCurrentTime.after(parsedDepartureTime);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Test with specific time
    private boolean isDeparted(String departureTime, String fixedTime)
    {
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

    private void startPaymentActivity(Ticket selectedTicket)
    {
        Intent paymentIntent = new Intent(TicketListActivity.this, PaymentActivity.class);
        paymentIntent.putExtra("ticket_id", selectedTicket.getId());
        paymentIntent.putExtra("ticket_destination", selectedTicket.getDestination());
        paymentIntent.putExtra("ticket_price", selectedTicket.getPrice());
        paymentIntent.putExtra("ticket_start", selectedTicket.getStart());
        paymentIntent.putExtra("ticket_arrivalTime", selectedTicket.getArrivalTime());
        paymentIntent.putExtra("ticket_totalTime", selectedTicket.getTotalTime());
        paymentIntent.putExtra("ticket_departureTime", selectedTicket.getDepartureTime());
        paymentIntent.putExtra("ticket_trainID", selectedTicket.getTrainID());
        paymentIntent.putExtra("ticket_date", selectedTicket.getDepartureDate());

        startActivity(paymentIntent);
    }

    private int getUserPoint(FirebaseUser user)
    {
        String userID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    point = userAccount.getUserPoint();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return point;
    }

    private boolean checkVi(String check) {
        return !check.equals("Find Train");
    }

    private String formatDate(String date) {
        if (checkVi(check)) {
            Log.e(TAG, "formatDate: " + check);
            Locale vietnameseLocale = new Locale("vi", "VN");
            return formatDateHelper(date, vietnameseLocale);
        }
        return formatDateHelper(date, Locale.ENGLISH);
    }

    private String formatDateHelper(String date, Locale locale) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
        LocalDate parsedDate = LocalDate.parse(date, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EE, dd/MM/yyyy", locale);
        return parsedDate.format(outputFormatter);
    }

}