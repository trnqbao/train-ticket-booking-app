package com.java.trainticketbookingapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.java.trainticketbookingapp.AccountManagement.LoginActivity;
import com.java.trainticketbookingapp.R;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    Button logout, change_language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("app_language", MODE_PRIVATE);

        final String[] availableLanguages = {"English", "Vietnamese"};
        final String[] languageCodes = {"en", "vi"};

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        change_language = view.findViewById(R.id.change_language);
        logout = view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //Change language app
        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Language");

                // Set the list of options
                builder.setItems(availableLanguages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update SharedPreferences with selected language
                        String selectedLocale = languageCodes[which];
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("locale", selectedLocale);
                        editor.apply();

                        getActivity().recreate();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
}