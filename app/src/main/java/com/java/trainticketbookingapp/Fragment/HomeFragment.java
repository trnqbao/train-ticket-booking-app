package com.java.trainticketbookingapp.Fragment;

import android.annotation.SuppressLint;
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
import com.java.trainticketbookingapp.Animation.LoadingActivity;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.TicketListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private Spinner spinnerFromID, spinnerToID;
    private TextView tvDate, tvUser;
    private Button btnFindTrain;
    private ImageView swap;
    private SharedPreferences sharedPreferences;
    private String savedDepartureName, savedDestination, savedDateText;

    public static String check;
    FirebaseAuth auth;
    FirebaseUser user;
    String name;
    String[] locations = new String[] {"Ha Noi", "Sai Gon", "Da Nang", "Nha Trang", "Hue"};

    private ProgressDialog loadingDialog;
    private LoadingActivity loadingActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        sharedPreferences = getActivity().getSharedPreferences("MY_PREFS", MODE_PRIVATE);

        // Find UI elements
        btnFindTrain = view.findViewById(R.id.btn_find_train);
        tvDate = view.findViewById(R.id.tv_date);
//        tvUser = view.findViewById(R.id.tv_user);
        spinnerFromID = view.findViewById(R.id.spinnerFromID);
        spinnerToID = view.findViewById(R.id.spinnerToID);
        swap = view.findViewById(R.id.swap);
        ImageView datePicker = view.findViewById(R.id.date_picker);
        loadingActivity = new LoadingActivity(loadingDialog);

        rotateImg(swap);

        check = btnFindTrain.getText().toString();

//        locations = getLocations();

        savedDepartureName = locations[sharedPreferences.getInt("SELECTED_POSITION", 0)];
        savedDestination = locations[sharedPreferences.getInt("SELECTED_POSITION_TO", 0)];
        savedDateText = sharedPreferences.getString("DATE_TEXT", "");

        tvDate.setText(savedDateText);
        spinnerFromID.setSelection(sharedPreferences.getInt("SELECTED_POSITION", 0));
        spinnerToID.setSelection(sharedPreferences.getInt("SELECTED_POSITION_TO", 0));

        showSpinnerOptions(spinnerFromID, locations);
        showSpinnerOptions(spinnerToID, locations);

//        userName(user);

        datePicker.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            int currentYear = currentDate.get(Calendar.YEAR);
            int currentMonth = currentDate.get(Calendar.MONTH);
            int currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (vi, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        if (selectedDate.before(currentDate)) {
                            showNotification(getString(R.string.error_place));
                            return;
                        }

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        savedDateText = format.format(selectedDate.getTime());

                        tvDate.setText(savedDateText);
                        sharedPreferences.edit().putString("DATE_TEXT", savedDateText).apply();
                    },
                    currentYear,
                    currentMonth,
                    currentDayOfMonth
            );

            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date savedDate = format.parse(savedDateText);
                if (savedDate != null) {
                    Calendar savedCalendar = Calendar.getInstance();
                    savedCalendar.setTime(savedDate);
                    int savedYear = savedCalendar.get(Calendar.YEAR);
                    int savedMonth = savedCalendar.get(Calendar.MONTH);
                    int savedDayOfMonth = savedCalendar.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog.getDatePicker().updateDate(savedYear, savedMonth, savedDayOfMonth);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

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
            savedDateText = sharedPreferences.getString("DATE_TEXT", "");

            if (savedDepartureName.equals(savedDestination)) {
                showNotification(getString(R.string.error_place));
                return;
            }

//            showLoadingDialog();
            loadingActivity.showLoadingDialog(getContext(), getString(R.string.loading));
            Handler handler = new Handler();
            handler.postDelayed(() -> {
//                dismissLoadingDialog();
                loadingActivity.dismissLoadingDialog();
                Intent intent = new Intent(getActivity(), TicketListActivity.class);
                intent.putExtra("bookingFromID", savedDepartureName);
                intent.putExtra("bookingToID", savedDestination);
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

    private String[] getLocations() {
        final String hanoi = getString(R.string.hanoi);
        final String saigon = getString(R.string.saigon);
        final String danang = getString(R.string.danang);
        final String nhatrang = getString(R.string.nhatrang);
        final String hue = getString(R.string.hue);

        return locations = new String[]{hanoi, saigon, danang, nhatrang, hue};
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


