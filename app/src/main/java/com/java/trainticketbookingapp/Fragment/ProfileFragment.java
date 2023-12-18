package com.java.trainticketbookingapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.AccountManagement.LoginActivity;
import com.java.trainticketbookingapp.AccountManagement.UpdateProfileActivity;
import com.java.trainticketbookingapp.AccountManagement.UploadImageActivity;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button logout, btnChangeLanguage, update_profile;
    private ImageView avatar;
    private TextView user_name, user_phone, user_point, user_email, user_name_avatar;
    private String name, phone, email;
    private int point;

    boolean updateSuccessful;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("app_language", MODE_PRIVATE);

        final String[] en_availableLanguages = {"English", "Vietnamese"};
        final String[] vi_availableLanguages = {"Tiếng Anh", "Tiếng Việt"};
        final String[] languageCodes = {"en", "vi"};

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnChangeLanguage = view.findViewById(R.id.change_language);
        logout = view.findViewById(R.id.logout);
        update_profile = view.findViewById(R.id.update_profile);
        user_name = view.findViewById(R.id.user_name);
        user_phone = view.findViewById(R.id.user_phone);
        user_point = view.findViewById(R.id.user_point);
        user_email = view.findViewById(R.id.user_email);
        avatar = view.findViewById(R.id.user_avatar);



        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        UserProfile(user);

        //Set user avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadImageActivity.class);
                startActivity(intent);
            }
        });
        //Log out button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        //Change language app
        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleOfButton = (String) btnChangeLanguage.getText();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                Log.e("TAG", "onClick: " + titleOfButton);
                if (titleOfButton.equals("Change Language")) {
                    builder.setTitle("Select Language");

                    // Set the list of options
                    builder.setItems(en_availableLanguages, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update SharedPreferences with selected language
                            String selectedLocale = languageCodes[which];
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userLanguage", selectedLocale);
                            editor.apply();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
                            reference.child(user.getUid()).child("userLanguage").setValue(selectedLocale);

                            getActivity().recreate();
                        }
                    });
                } else {
                    builder.setTitle("Chọn ngôn ngữ");

                    // Set the list of options
                    builder.setItems(vi_availableLanguages, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update SharedPreferences with selected language
                            String selectedLocale = languageCodes[which];
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userLanguage", selectedLocale);
                            editor.apply();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
                            reference.child(user.getUid()).child("userLanguage").setValue(selectedLocale);

                            getActivity().recreate();
                        }
                    });
                }


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void UserProfile(FirebaseUser user) {
            String userID = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        name = userAccount.getUserName();
                        email = user.getEmail();
                        phone = userAccount.getUserPhone();
                        point = userAccount.getUserPoint();

                        user_name.setText(name);
                        user_email.setText(email);
                        user_phone.setText(phone);
                        user_point.setText(String.valueOf(point));

                        Uri uri = user.getPhotoUrl();
                        Picasso.with(getActivity()).load(uri).into(avatar);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
}

