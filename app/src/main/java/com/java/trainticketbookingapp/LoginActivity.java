package com.java.trainticketbookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private TextView tvSignUp, tvForget;
    FirebaseAuth mAuth;
    public static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnLogin);
        rememberMe = findViewById(R.id.cb_remember);
        tvForget = findViewById(R.id.tv_forget_password);
        tvSignUp = findViewById(R.id.tvSignup);
        ImageView ivEye = findViewById(R.id.iv_eye);
        mAuth = FirebaseAuth.getInstance();
//        Log.e("TAG", "mAuth: " + mAuth.getCurrentUser().getEmail().toString() );
        //View and UnView password
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to implement
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to implement
            }

            @Override
            public void afterTextChanged(Editable s) {
                // If the EditText is not empty, show the eye icon
                if (s.length() > 0) {
                    ivEye.setVisibility(View.VISIBLE);
                } else {
                    // Otherwise, hide the eye icon and reset its state
                    ivEye.setVisibility(View.GONE);
                    ivEye.setSelected(false);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        ivEye.setOnClickListener(v -> {
            // If the eye icon is not selected, show the password and change the icon to open eye
            if (!ivEye.isSelected()) {
                ivEye.setSelected(true);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Otherwise, hide the password and change the icon to closed eye
                ivEye.setSelected(false);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        tvSignUp.setOnClickListener(v -> {
           Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
           startActivity(intent);
        });

        tvForget.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ConfirmEmail.class);
            startActivity(intent);
        });


        //Check Remember me is checked or not
        SharedPreferences loginPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean remember = loginPreferences.getBoolean("remember", false);
        if (remember) {
            etEmail.setText(loginPreferences.getString(PREF_EMAIL, ""));
            etPassword.setText(loginPreferences.getString(PREF_PASSWORD, ""));
            rememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
           String email, password;
           email = String.valueOf(etEmail.getText());
           password = String.valueOf(etPassword.getText());

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Please enter your password");
                return;
            }

           //Put email into a login
            boolean rememberm = rememberMe.isChecked();
            SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
            if (rememberm) {
                loginPrefsEditor.putBoolean("remember", true);
                loginPrefsEditor.putString(PREF_EMAIL, email);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }


            mAuth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                               startActivity(intent);
                               finish();
                           } else {
                               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Wrong Email or Password, please try again", Snackbar.LENGTH_INDEFINITE);
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