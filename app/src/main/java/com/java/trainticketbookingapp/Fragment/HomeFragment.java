package com.java.trainticketbookingapp.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.TicketListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    String[] locations = new String[]{
            "Ha Noi",
            "Sai Gon",
            "Da Nang",
            "Hue",
            "Nha Trang"
    };

//    String[] typeOfUsers = new String[]{
//            "Adult", "Children"
//    };

    private Spinner spinnerType, spinnerFromID, spinnerToID;
    private TextView tvType, tvPassenger, tvDate, tvUser;
    private Button btnFindTrain;
    private ImageButton btnPlus, btnMinus;
    private ImageView swap;
    private SharedPreferences sharedPreferences;
    private int passenger;
    private String savedDepartureName, savedDestination, savedPassengerText, savedDateText;
    FirebaseAuth auth;
    FirebaseUser user;
    String name;

    private ProgressDialog loadingDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize variables
        sharedPreferences = getActivity().getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        passenger = 1;

        // Find UI elements
        btnFindTrain = view.findViewById(R.id.btn_find_train);
//        btnPlus = view.findViewById(R.id.btn_plus);
//        btnMinus = view.findViewById(R.id.btn_minus);
//        tvPassenger = view.findViewById(R.id.tv_count);
//        tvType = view.findViewById(R.id.tv_type);
        tvDate = view.findViewById(R.id.tv_date);
        tvUser = view.findViewById(R.id.tv_user);
//        spinnerType = view.findViewById(R.id.spinner_type);
        spinnerFromID = view.findViewById(R.id.spinnerFromID);
        spinnerToID = view.findViewById(R.id.spinnerToID);
        swap = view.findViewById(R.id.swap);
        ImageView datePicker = view.findViewById(R.id.date_picker);

        rotateImg(swap);

        // Set initial values
        savedDepartureName = locations[sharedPreferences.getInt("SELECTED_POSITION", 0)];
        savedDestination = locations[sharedPreferences.getInt("SELECTED_POSITION_TO", 0)];
        savedPassengerText = sharedPreferences.getString("PASSENGER_TEXT", "");
        savedDateText = sharedPreferences.getString("DATE_TEXT", "");

        // Display initial values
//        tvPassenger.setText(String.valueOf(passenger));

        tvDate.setText(savedDateText);
        spinnerFromID.setSelection(sharedPreferences.getInt("SELECTED_POSITION", 0));
        spinnerToID.setSelection(sharedPreferences.getInt("SELECTED_POSITION_TO", 0));

        // Set up spinner adapters
        showSpinnerOptions(spinnerFromID, locations);
        showSpinnerOptions(spinnerToID, locations);
//        showSpinnerOptions(spinnerType, typeOfUsers);
//        showTypeOfUser();

        userName(user);

        datePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (vi, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        Calendar currentDate = Calendar.getInstance();

                        if (selectedDate.before(currentDate)) {
                            showNotification(getString(R.string.error_place));
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

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // On plus button click increase passenger count and update shared preferences
//        btnPlus.setOnClickListener(v -> {
//            passenger++;
//            tvPassenger.setText(String.valueOf(passenger));
//            savedPassengerText = String.valueOf(passenger);
//            sharedPreferences.edit().putString("PASSENGER_TEXT", savedPassengerText).apply();
//
//        });

        // On minus button click decrease passenger count (with minimum value of 1) and update shared preferences
//        btnMinus.setOnClickListener(v -> {
//            passenger--;
//            if (passenger <= 1) {
//                passenger = 1;
//            }
//            tvPassenger.setText(String.valueOf(passenger));
//            savedPassengerText = String.valueOf(passenger);
//            sharedPreferences.edit().putString("PASSENGER_TEXT", savedPassengerText).apply();
//        });

        // On swap button click swap location selections and update shared preferences
        swap.setOnClickListener(v -> {
            int tempFromID = spinnerFromID.getSelectedItemPosition();
            int tempToID = spinnerToID.getSelectedItemPosition();

            spinnerFromID.setSelection(spinnerToID.getSelectedItemPosition());
            spinnerToID.setSelection(tempFromID);
            sharedPreferences.edit().putInt("SELECTED_POSITION", tempFromID).apply();
            sharedPreferences.edit().putInt("SELECTED_POSITION_TO", tempToID).apply();
        });


        btnFindTrain.setOnClickListener(v -> {
            savedDepartureName = locations[spinnerFromID.getSelectedItemPosition()];
            savedDestination = locations[spinnerToID.getSelectedItemPosition()];
//            savedPassengerText = sharedPreferences.getString("PASSENGER_TEXT", "");
            savedDateText = sharedPreferences.getString("DATE_TEXT", "");

            if (savedDepartureName.equals(savedDestination)) {
                showNotification(getString(R.string.error_place));
                return;
            }

            showLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                dismissLoadingDialog();
                Intent intent = new Intent(getActivity(), TicketListActivity.class);
                intent.putExtra("bookingFromID", savedDepartureName);
                intent.putExtra("bookingToID", savedDestination);
//                    intent.putExtra("passenger", savedPassengerText);
                intent.putExtra("date", savedDateText);
                startActivity(intent);
            }, 2000);

        });
        return view;
    }

    private void showNotification(String notification) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Snackbar snackbar = Snackbar.make(requireView(), notification, Snackbar.LENGTH_SHORT);
            snackbar.setAnchorView(null);
            snackbar.show();
            new Handler(Looper.getMainLooper()).postDelayed(snackbar::dismiss, 2000);
        });
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getContext());
            loadingDialog.setMessage(getString(R.string.loading));
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();

        new Handler().postDelayed(() -> {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }, 2000);
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private boolean checkVi() {
        String check = (String) btnFindTrain.getText();
        if (check.equals("Tìm kiếm")) {
            return true;
        }
        return false;
    }

    private void rotateImg(ImageView imageView) {
        Drawable originalDrawable = imageView.getDrawable();
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

        Drawable rotatedDrawable = new BitmapDrawable(getResources(), rotatedBitmap);
        imageView.setImageDrawable(rotatedDrawable);
    }


    private void showTypeOfUser() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_type, items);
//        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    private void userName(FirebaseUser user){
        String userID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    name = userAccount.getUserName();
                    tvUser.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}


