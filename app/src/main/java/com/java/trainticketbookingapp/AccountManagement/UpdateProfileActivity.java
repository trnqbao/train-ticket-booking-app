package com.java.trainticketbookingapp.AccountManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.ActivityforFragment.ActForProfileFragment;
import com.java.trainticketbookingapp.Fragment.ProfileFragment;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;


public class UpdateProfileActivity extends AppCompatActivity {
    private EditText et_name, et_phone;
    private Button btnSave, btnBack;
    private FirebaseAuth auth;
    private String user_name, user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        et_name = findViewById(R.id.et_user_name);
        et_phone = findViewById(R.id.et_user_phone);
        btnSave = findViewById(R.id.save);
        btnBack = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        showUserProfile(user);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(user);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, ProfileFragment.class);
                startActivity(intent);
            }
        });
    }

    private void showUserProfile(FirebaseUser user) {
        String userID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    user_name = userAccount.getUserName();
                    user_phone = userAccount.getUserPhone();

                    et_phone.setText(user_phone);
                    et_name.setText(user_name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateProfile(FirebaseUser user) {


        if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_phone)) {
            Toast.makeText(UpdateProfileActivity.this, "Please fill the information", Toast.LENGTH_SHORT).show();

        } else {

            user_name = et_name.getText().toString();
            user_phone = et_phone.getText().toString();

            UserAccount userAccount = new UserAccount(user_name, user_phone);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
            String userID = user.getUid();

            reference.child(userID).setValue(userAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(user_name).build();

                        user.updateProfile(profileChangeRequest);
                        replaceFragment(new ProfileFragment());

                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.update_profile, fragment);
        fragmentTransaction.commit();
        finish();
    }
    //                        Intent intent = new Intent(UpdateProfileActivity.this, ProfileFragment.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();


}