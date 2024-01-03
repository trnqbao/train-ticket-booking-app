package com.java.trainticketbookingapp.AccountManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etPasswordCf, etPhone;
    private Button btnSignUp;
    private TextView tvLogin;
    private Snackbar snackbar;
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
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPasswordCf = findViewById(R.id.et_password_cf);
        etPhone = findViewById(R.id.et_phone);

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(view -> {
            String name, phone, email, password, passwordCf;
            name = String.valueOf(etName.getText());
            phone = String.valueOf(etPhone.getText());
            email = String.valueOf(etEmail.getText());
            password = String.valueOf(etPassword.getText());
            passwordCf = String.valueOf(etPasswordCf.getText());


            if (TextUtils.isEmpty(name)) {
                etName.setError(getString(R.string.error_name));
                return;
            }

            if (TextUtils.isEmpty(email)) {
                etEmail.setError(getString(R.string.error_email));
                return;
            }

            if (TextUtils.isEmpty(phone)) {
                etPhone.setError(getString(R.string.error_phone));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError(getString(R.string.error_pwd));
                return;
            }

            if (!password.equals(passwordCf)) {
                etPasswordCf.setError(getString(R.string.error_cf_pwd));
                return;
            }

            registerUserWithEmailVerification(name, phone, email, password);

        });
    }
    private void registerUserWithEmailVerification(String name, String phone, String email, String password) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser) {
                            // The email is not used yet
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                UserAccount userAccount = new UserAccount(name, email, phone);
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");

                                                reference.child(user.getUid()).setValue(userAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            if (user != null) {
                                                                user.sendEmailVerification()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
//                                                                                    snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.success_verify), Snackbar.LENGTH_SHORT);
//                                                                                    snackbar.show();
                                                                                    Toast.makeText(RegisterActivity.this, getString(R.string.success_verify), Toast.LENGTH_SHORT).show();
                                                                                    mAuth.signOut(); // Sign out the user
                                                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                } else {
//                                                                                    snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.fail_verify), Snackbar.LENGTH_SHORT);
//                                                                                    snackbar.show();
                                                                                    Toast.makeText(RegisterActivity.this, getString(R.string.fail_verify), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        } else {
                                                            Toast.makeText(RegisterActivity.this, getString(R.string.fail_register), Toast.LENGTH_SHORT).show();
//                                                            snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.fail_register), Snackbar.LENGTH_SHORT);
//                                                            snackbar.show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(RegisterActivity.this, getString(R.string.existed_email), Toast.LENGTH_SHORT).show();
//                                                snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.existed_email), Snackbar.LENGTH_SHORT);
//                                                snackbar.show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, getString(R.string.fail_register), Toast.LENGTH_SHORT).show();
//                            snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.fail_register), Snackbar.LENGTH_SHORT);
//                            snackbar.show();
                        }
                    }
                });
    }


}
