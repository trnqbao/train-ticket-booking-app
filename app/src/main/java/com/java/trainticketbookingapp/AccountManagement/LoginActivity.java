package com.java.trainticketbookingapp.AccountManagement;



import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.java.trainticketbookingapp.Animation.LoadingActivity;
import com.java.trainticketbookingapp.Animation.LoginLoadingActivity;
import com.java.trainticketbookingapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private TextView tvSignUp, tvForget;
    FirebaseAuth mAuth;

    private ProgressDialog loadingDialog;
    public static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    private LoadingActivity loadingActivity;

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
        loadingActivity = new LoadingActivity(loadingDialog);


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
                if (s.length() > 0) {
                    ivEye.setVisibility(View.VISIBLE);
                } else {
                    ivEye.setVisibility(View.GONE);
                    ivEye.setSelected(false);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        ivEye.setOnClickListener(v -> {
            if (!ivEye.isSelected()) {
                ivEye.setSelected(true);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
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


        SharedPreferences loginPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean remember = loginPreferences.getBoolean("remember", false);
        if (remember) {
            etEmail.setText(loginPreferences.getString(PREF_EMAIL, ""));
            etPassword.setText(loginPreferences.getString(PREF_PASSWORD, ""));
            rememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
           String email, password;
           email = String.valueOf(etEmail.getText());
           password = String.valueOf(etPassword.getText());

            if (TextUtils.isEmpty(email)) {
                etEmail.setError(getString(R.string.email_alert));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError(getString(R.string.pwd_alert));
                return;
            }

            boolean remember1 = rememberMe.isChecked();
            SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
            if (remember1) {
                loginPrefsEditor.putBoolean("remember", true);
                loginPrefsEditor.putString(PREF_EMAIL, email);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {

//                                    loadingActivity.showLoadingDialog(this, getString(R.string.loading));
//                                    Handler handler = new Handler();
//                                    handler.postDelayed(() -> {
//
//                                        loadingActivity.dismissLoadingDialog();
//                                        Intent intent = new Intent(this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }, 2000);
                                    Intent intent = new Intent(this, LoginLoadingActivity.class);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.verify_email), Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                }
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(getString(R.string.retry), v1 -> mAuth.signInWithEmailAndPassword(email, password));
                            snackbar.show();
                        }
                    });
       });
        
    }



}