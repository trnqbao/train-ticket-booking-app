package com.java.trainticketbookingapp.AccountManagement;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.Fragment.HomeFragment;
import com.java.trainticketbookingapp.Fragment.InboxFragment;
import com.java.trainticketbookingapp.Fragment.ProfileFragment;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.databinding.ActivityHomeBinding;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
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
