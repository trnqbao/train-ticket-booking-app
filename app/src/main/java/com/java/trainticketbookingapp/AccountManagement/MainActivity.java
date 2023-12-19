package com.java.trainticketbookingapp.AccountManagement;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.Fragment.HomeFragment;
import com.java.trainticketbookingapp.Fragment.InboxFragment;
import com.java.trainticketbookingapp.Fragment.ProfileFragment;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.databinding.ActivityHomeBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Language Setting
        if (user != null) {
            // User is signed in
            String userID = user.getUid();
            // Get reference to user's data in Firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);

                    if (userAccount != null) {
                        // Get stored language or default to "en"
                        String storedLanguage = userAccount.getUserLanguage() != null ? userAccount.getUserLanguage() : "en";
                        SharedPreferences prefs = getSharedPreferences("app_language", MODE_PRIVATE);
                        String currentLanguage = prefs.getString("userLanguage", "en");

                        if (!storedLanguage.equals(currentLanguage)) {
                            // Change language
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
                    // Handle error
                }
            });
        } else {
            // No user is signed in
        }

        // Apply the language settings
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

        binding.bottomNav.setOnItemSelectedListener(item -> {
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
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

