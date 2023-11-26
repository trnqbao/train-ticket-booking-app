package com.java.trainticketbookingapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etPasswordCf;
    private Button btnSignUp;
    private TextView tvLogin;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        tvLogin = findViewById(R.id.tvLogin);
        btnSignUp = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPasswordCf = findViewById(R.id.et_password_cf);

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(view -> {
            String email, password, passwordCf;
            email = String.valueOf(etEmail.getText());
            password = String.valueOf(etPassword.getText());
            passwordCf = String.valueOf(etPasswordCf.getText());

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            } else if (!email.contains("@gmail")) {
                etEmail.setError("Email should contains '@gmail'");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Please enter your password");
                return;
            }

            if (!password.equals(passwordCf)) {
                etPasswordCf.setError("Password is not correct");
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Account created",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Email or Password is invalid, please try again", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mAuth.signInWithEmailAndPassword(email, password);
                                    }
                                });
                                snackbar.show();
                            }
                        }
                    });
        });
    }
}
