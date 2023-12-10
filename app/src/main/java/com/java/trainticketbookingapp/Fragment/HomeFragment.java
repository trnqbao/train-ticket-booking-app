package com.java.trainticketbookingapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import static android.content.Context.MODE_PRIVATE;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.TicketList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//public class HomeFragment extends Fragment{
//
//    String[] locations = new String[]{
//            "An Giang",
//            "Ba Ria - Vung Tau",
//            "Bac Lieu",
//            "Ben Tre",
//            "Binh Phuoc",
//            "Binh Duong",
//            "Ca Mau",
//            "Dong Nai",
//            "Dong Thap",
//            "Hau Giang",
//            "Ho Chi Minh City",
//            "Kien Giang",
//            "Long An",
//            "Soc Trang",
//            "Tay Ninh",
//            "Tien Giang",
//            "Tra Vinh",
//            "Vinh Long",
//    };
//
//    int passenger = 1;
//    ImageButton btnPlus, btnMinus;
//    TextView tvPassenger, tvDate;
//    Spinner spinnerFromID, spinnerToID;
//    ImageView swap, date_picker;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_search, container, false);
//
//        Button btnFindTrain = view.findViewById(R.id.btn_find_train);
//        btnPlus = view.findViewById(R.id.btn_plus);
//        btnMinus = view.findViewById(R.id.btn_minus);
//        tvPassenger = view.findViewById(R.id.tv_count);
//        tvDate = view.findViewById(R.id.tv_date);
//        spinnerFromID = view.findViewById(R.id.spinnerFromID);
//        spinnerToID = view.findViewById(R.id.spinnerToID);
//        swap = view.findViewById(R.id.swap);
//        date_picker = view.findViewById(R.id.date_picker);
//
//        tvPassenger.setText(String.valueOf(1));
//
//        //Handle click "Swap" image
//        swap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Swap locations
//                int fromPosition = spinnerFromID.getSelectedItemPosition();
//                int toPosition = spinnerToID.getSelectedItemPosition();
//
//                int temp = fromPosition;
//                fromPosition = toPosition;
//                toPosition = temp;
//
//                spinnerFromID.setSelection(fromPosition);
//                spinnerToID.setSelection(toPosition);
//            }
//        });
//
//        //Departure
//        showSpinnerOptions(spinnerFromID, locations);
//        spinnerFromID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                // Get the selected location
//                String selectedLocation = locations[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                // Do something when nothing is selected
//            }
//        });
//        //Destination
//        showSpinnerOptions(spinnerToID, locations);
//        spinnerToID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                String selectedLocation = locations[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                // Do something when nothing is selected
//            }
//        });
//
//        //Date_picker handle
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                // Update date text view
//                String chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year;
//                tvDate.setText(chosenDate);
//            }
//        };
//        date_picker.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
//            datePickerDialog.show();
//        });
//
//        //Remember selections
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_PREFS", MODE_PRIVATE);
//        int save_departure = sharedPreferences.getInt("SELECTED_POSITION", 0);
//        int save_destination = sharedPreferences.getInt("SELECTED_POSITION_TO", 0);
//        String savedPassengerText = sharedPreferences.getString("PASSENGER_TEXT", "");
//        String savedDateText = sharedPreferences.getString("DATE_TEXT", "");
//
//        spinnerFromID.setSelection(save_departure);
//        spinnerToID.setSelection(save_destination);
//        tvPassenger.setText(savedPassengerText);
//        tvDate.setText(savedDateText);
//
//        btnPlus.setOnClickListener(v -> {
//            passenger++;
//            tvPassenger.setText(String.valueOf(passenger));
//        });
//        btnMinus.setOnClickListener(v -> {
//            passenger--;
//            if (passenger <= 1) {
//                passenger = 1;
//            }
//            tvPassenger.setText(String.valueOf(passenger));
//        });
//
//        btnFindTrain.setOnClickListener(v -> {
//            String departure = locations[save_departure];
//            String destination = locations[save_destination];
//
//            Intent intent = new Intent(getActivity(), TicketList.class);
//            // Put the saved values as extras in the intent
//            intent.putExtra("departure", departure);
//            intent.putExtra("destination", destination);
//            intent.putExtra("passenger", savedPassengerText);
//            intent.putExtra("date", savedDateText);
//
//            startActivity(intent);
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // Save the selected item position
//        int selectedDeparture = spinnerFromID.getSelectedItemPosition();
//        int selectedDestination = spinnerToID.getSelectedItemPosition();
//        String passengerText = tvPassenger.getText().toString();
//        String dateText = tvDate.getText().toString();
//
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_PREFS", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("SELECTED_POSITION", selectedDeparture);
//        editor.putInt("SELECTED_POSITION_TO", selectedDestination);
//        editor.putString("PASSENGER_TEXT", passengerText);
//        editor.putString("DATE_TEXT", dateText);
//        editor.apply();
//    }
//
//    public void showSpinnerOptions(Spinner spinner, String[] options) {
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, options);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}

public class HomeFragment extends Fragment {

    String[] locations = new String[]{
            "An Giang",
            "Ba Ria - Vung Tau",
            "Bac Lieu",
            "Ben Tre",
            "Binh Phuoc",
            "Binh Duong",
            "Ca Mau",
            "Dong Nai",
            "Dong Thap",
            "Hau Giang",
            "Ho Chi Minh City",
            "Kien Giang",
            "Long An",
            "Soc Trang",
            "Tay Ninh",
            "Tien Giang",
            "Tra Vinh",
            "Vinh Long",
    };

    String[] typeOfUsers = new String[]{
            "Adult", "Children"
    };

    private Spinner spinnerType, spinnerFromID, spinnerToID;
    private TextView tvType, tvPassenger, tvDate;
    private Button btnFindTrain;
    private ImageButton btnPlus, btnMinus;
    private DatePicker datePicker;
    private ImageView swap;
    private SharedPreferences sharedPreferences;
    private int passenger;
    private String savedDepartureName, savedDestination, savedPassengerText, savedDateText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize variables
        sharedPreferences = getActivity().getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        passenger = 1;

        // Find UI elements
        btnFindTrain = view.findViewById(R.id.btn_find_train);
        btnPlus = view.findViewById(R.id.btn_plus);
        btnMinus = view.findViewById(R.id.btn_minus);
        tvPassenger = view.findViewById(R.id.tv_count);
        tvType = view.findViewById(R.id.tv_type);
        tvDate = view.findViewById(R.id.tv_date);
        spinnerType = view.findViewById(R.id.spinner_type);
        spinnerFromID = view.findViewById(R.id.spinnerFromID);
        spinnerToID = view.findViewById(R.id.spinnerToID);
        swap = view.findViewById(R.id.swap);
        ImageView datePicker = view.findViewById(R.id.date_picker);

        // Set initial values
        savedDepartureName = locations[sharedPreferences.getInt("SELECTED_POSITION", 0)];
        savedDestination = locations[sharedPreferences.getInt("SELECTED_POSITION_TO", 0)];
        savedPassengerText = sharedPreferences.getString("PASSENGER_TEXT", "");
        savedDateText = sharedPreferences.getString("DATE_TEXT", "");

        // Display initial values
        tvPassenger.setText(String.valueOf(passenger));
        tvDate.setText(savedDateText);
        spinnerFromID.setSelection(sharedPreferences.getInt("SELECTED_POSITION", 0));
        spinnerToID.setSelection(sharedPreferences.getInt("SELECTED_POSITION_TO", 0));

        // Set up spinner adapters
        showSpinnerOptions(spinnerFromID, locations);
        showSpinnerOptions(spinnerToID, locations);
        showSpinnerOptions(spinnerType, typeOfUsers);
        showTypeOfUser();

        // On date change update date text and shared preferences
//        datePicker.setOnClickListener(v -> {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
//                    (vi, year, month, dayOfMonth) -> {
//                        // Get the current date
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(year, month, dayOfMonth);
//                        Date date = calendar.getTime();
//                        // Format the date string
//                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
//                        savedDateText = format.format(date); // Day + DayNumber + Month + Year
//
//                        tvDate.setText(savedDateText);
//                        sharedPreferences.edit().putString("DATE_TEXT", savedDateText).apply();
//                    },
//                    Calendar.getInstance().get(Calendar.YEAR),
//                    Calendar.getInstance().get(Calendar.MONTH),
//                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
//            );
//            datePickerDialog.show();
//        });

        datePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (vi, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        Calendar currentDate = Calendar.getInstance();

                        if (selectedDate.before(currentDate)) {
                            Toast.makeText(getActivity(), "Please select a date on or after today.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Format the date string
                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                        savedDateText = format.format(selectedDate.getTime());

                        tvDate.setText(savedDateText);
                        sharedPreferences.edit().putString("DATE_TEXT", savedDateText).apply();
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });


        // On plus button click increase passenger count and update shared preferences
        btnPlus.setOnClickListener(v -> {
            passenger++;
            tvPassenger.setText(String.valueOf(passenger));
            savedPassengerText = String.valueOf(passenger);
            sharedPreferences.edit().putString("PASSENGER_TEXT", savedPassengerText).apply();

        });

        // On minus button click decrease passenger count (with minimum value of 1) and update shared preferences
        btnMinus.setOnClickListener(v -> {
            passenger--;
            if (passenger <= 1) {
                passenger = 1;
            }
            tvPassenger.setText(String.valueOf(passenger));
            savedPassengerText = String.valueOf(passenger);
            sharedPreferences.edit().putString("PASSENGER_TEXT", savedPassengerText).apply();
        });

        // On swap button click swap location selections and update shared preferences
        swap.setOnClickListener(v -> {
            int tempFromID = spinnerFromID.getSelectedItemPosition();
            int tempToID = spinnerToID.getSelectedItemPosition();

            spinnerFromID.setSelection(spinnerToID.getSelectedItemPosition());
            spinnerToID.setSelection(tempFromID);
            sharedPreferences.edit().putInt("SELECTED_POSITION", tempFromID).apply();
            sharedPreferences.edit().putInt("SELECTED_POSITION_TO", tempToID).apply();
        });


        // On find train click create intent with updated values and start TicketList activity
        btnFindTrain.setOnClickListener(v -> {
            savedDepartureName = locations[spinnerFromID.getSelectedItemPosition()];
            savedDestination = locations[spinnerToID.getSelectedItemPosition()];
            savedPassengerText = sharedPreferences.getString("PASSENGER_TEXT", "");
            savedDateText = sharedPreferences.getString("DATE_TEXT", "");

            Intent intent = new Intent(getActivity(), TicketList.class);
            intent.putExtra("bookingFromID", savedDepartureName);
            intent.putExtra("bookingToID", savedDestination);
            intent.putExtra("passenger", savedPassengerText);
            intent.putExtra("date", savedDateText);

            startActivity(intent);
        });
        return view;
    }

    private void showTypeOfUser() {
//        String[] items = new String[]{
//                "Adult", "Children"
//        };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_type, items);
//        spinnerType.setAdapter(adapter);
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

    private void showSpinnerOptions(Spinner spinner, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, options);
        spinner.setAdapter(adapter);
    }
}


