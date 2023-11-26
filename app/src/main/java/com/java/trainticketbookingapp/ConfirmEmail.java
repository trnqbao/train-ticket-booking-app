package com.java.trainticketbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmEmail extends AppCompatActivity {
    static final int CODE = 100;
    private EditText etEmail;
    private Button btnNext, btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);

        etEmail = findViewById(R.id.et_email_cf);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);



        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmEmail.this, LoginActivity.class);
            startActivity(intent);
        });

        btnNext.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            if (checkEmail(email)) {
                Intent intent = new Intent(ConfirmEmail.this, ResetPassword.class);
                intent.putExtra("email", email);
                startActivityForResult(intent, CODE);
            }
        });


    }

    private boolean checkEmail(String email) {
        //TODO
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE) {
            etEmail.setText(data.getStringExtra("email"));
        }
    }
}
