package com.java.trainticketbookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Adapter.BookedTicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.ArrayList;
import java.util.List;


public class BookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookedTicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user").child(userID).child("tickets");

        recyclerView = view.findViewById(R.id.booked_tripID);
        ticketList = new ArrayList<>();

        ticketAdapter = new BookedTicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketList.clear(); // Clear existing tickets

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    Ticket ticket = ticketSnapshot.getValue(Ticket.class);
                    if (ticket != null) {
                        ticketList.add(ticket); // Add retrieved ticket to list
                    }
                }
                ticketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BookingFragment", "Error fetching tickets: ", error.toException());
            }
        });

        return view;
    }
}