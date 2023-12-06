package com.java.trainticketbookingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class HomeFragment extends Fragment{

    int passenger = 1;
    ImageButton btnPlus, btnMinus;
    TextView tvPassenger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_search, container, false);


        Button btnFindTrain = view.findViewById(R.id.btn_find_train);
        btnPlus = view.findViewById(R.id.btn_plus);
        btnMinus = view.findViewById(R.id.btn_minus);
        tvPassenger = view.findViewById(R.id.tv_count);

        tvPassenger.setText(String.valueOf(1));

        btnFindTrain.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TicketList.class);
                startActivity(intent);
        });

        btnPlus.setOnClickListener(v -> {
            passenger++;
            tvPassenger.setText(String.valueOf(passenger));
        });

        btnMinus.setOnClickListener(v -> {

            passenger--;
            if (passenger <= 1) {
                passenger = 1;
            }
            tvPassenger.setText(String.valueOf(passenger));
        });
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




}