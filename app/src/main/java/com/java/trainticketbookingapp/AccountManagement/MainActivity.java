package com.java.trainticketbookingapp.AccountManagement;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Broadcast.NotificationReceiver;
import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.Fragment.HomeFragment;
import com.java.trainticketbookingapp.Fragment.InboxFragment;
import com.java.trainticketbookingapp.Fragment.ProfileFragment;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.databinding.ActivityHomeBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            String userID = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);

                    if (userAccount != null) {
                        String storedLanguage = userAccount.getUserLanguage() != null ? userAccount.getUserLanguage() : "en";
                        SharedPreferences prefs = getSharedPreferences("app_language", MODE_PRIVATE);
                        String currentLanguage = prefs.getString("userLanguage", "en");

                        if (!storedLanguage.equals(currentLanguage)) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userLanguage", storedLanguage);
                            editor.apply();

                            Locale locale = new Locale(storedLanguage);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                            recreate();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {

        }

        SharedPreferences prefs = getSharedPreferences("app_language", MODE_PRIVATE);
        String language = prefs.getString("userLanguage", "en");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNav.setBackground(null);

        binding.bottomNav.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.booking) {
                replaceFragment(new BookingFragment());
            } else if (item.getItemId() == R.id.inbox) {
                replaceFragment(new InboxFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        checkDepartureTime();
    }


    private boolean isDepartureTimeClose(String departureTime)
    {
        // Get current time
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat sdfCurrent = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedCurrentTime = sdfCurrent.format(currentTime.getTime());

        // Convert departure time to "HH:mm" format
        SimpleDateFormat sdfDeparture = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
        try {
            Date departure = sdfDeparture.parse(departureTime);
            String formattedDepartureTime = sdfCurrent.format(departure);

            // Set a threshold (e.g., 30 minutes)
            long thresholdInMillis = 30 * 60 * 1000; // 30 minutes in milliseconds

            // Check if the departure time is within the threshold
            Date current = sdfCurrent.parse(formattedCurrentTime);
            Date formattedDeparture = sdfCurrent.parse(formattedDepartureTime);

            System.out.println(formattedDeparture);
            System.out.println(formattedCurrentTime);

            return Math.abs(formattedDeparture.getTime() - current.getTime()) <= thresholdInMillis;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void scheduleNotification(String departureTime, String title, String message)
    {
        // Convert departure time to "HH:mm" format
        SimpleDateFormat sdfDeparture = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
        try {
            Date departure = sdfDeparture.parse(departureTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(departure);

            // Schedule the notification
            Intent notificationIntent = new Intent(this, NotificationReceiver.class);
            notificationIntent.putExtra("title", title);
            notificationIntent.putExtra("message", message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void checkDepartureTime()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user")
                .child(user.getUid())
                .child("tickets");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    Ticket ticket = ticketSnapshot.getValue(Ticket.class);
                    if (ticket != null) {
                        scheduleNotification(ticket.getDepartureTime(), "Your departure time is approaching!", "Departure Time: " + ticket.getDepartureTime());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

