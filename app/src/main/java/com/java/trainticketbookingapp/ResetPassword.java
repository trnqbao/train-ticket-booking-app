package com.java.trainticketbookingapp;

import static com.java.trainticketbookingapp.ConfirmEmail.CODE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnBack, btnReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.et_new_password);
        btnBack = findViewById(R.id.btnBack_cf);
        btnReset = findViewById(R.id.btn_reset);

        etEmail.setText(intent.getStringExtra("email"));
        btnBack.setOnClickListener(v -> {
          Intent intent1 = new Intent(ResetPassword.this, ConfirmEmail.class);
          intent1.putExtra("email", etEmail.getText().toString());
          setResult(RESULT_OK, intent1);
          finish();
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkReset(etEmail.getText().toString(), etPassword.getText().toString())) {
                    Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkReset(String email, String password) {
        //TODO
        return true;
    }
}
