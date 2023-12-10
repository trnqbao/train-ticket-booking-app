package com.java.trainticketbookingapp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.TicketList;


public class HomeFragment extends Fragment{

    int passenger = 1;
    ImageButton btnPlus, btnMinus;
    TextView tvPassenger, tvType;
    Spinner spinnerType, spinnerFrom, spinnerTo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button btnFindTrain = view.findViewById(R.id.btn_find_train);
        btnPlus = view.findViewById(R.id.btn_plus);
        btnMinus = view.findViewById(R.id.btn_minus);
        tvPassenger = view.findViewById(R.id.tv_count);
        tvType = view.findViewById(R.id.tv_type);
        spinnerType = view.findViewById(R.id.spinner_type);
        initSpinnerFooter();

        tvPassenger.setText(String.valueOf(1));

        btnFindTrain.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TicketList.class);
                intent.putExtra("passenger", passenger);
                startActivityForResult(intent,1);
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

    private void initSpinnerFooter() {
        String[] items = new String[]{
                "Adult", "Children"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_type, items);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                tvType.setText((String) parent.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




}