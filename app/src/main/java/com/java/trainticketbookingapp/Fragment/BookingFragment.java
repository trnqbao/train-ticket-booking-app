package com.java.trainticketbookingapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.trainticketbookingapp.Adapter.BookedTicketAdapter;
import com.java.trainticketbookingapp.Adapter.TicketAdapter;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.ArrayList;
import java.util.List;


public class BookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookedTicketAdapter ticketAdapter;
    private List<Ticket> ticketList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerView = view.findViewById(R.id.booked_tripID);
        ticketList = new ArrayList<>();

        int i = 1;
        for (i = 1; i < 10; i++) {
            ticketList.add(new Ticket("null", "null", "null", 600, "3 hours", "09:00", "12:00", "Train ID", "null"));
        }


        ticketAdapter = new BookedTicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}