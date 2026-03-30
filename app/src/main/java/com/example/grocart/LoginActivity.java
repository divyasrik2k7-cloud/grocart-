package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText etEmail, etPassword;
    Button btnLogin, btnGoRegister;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ensure Firebase is initialized
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    Log.e(TAG, "Login failed: " + error);
                    Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_LONG).show();
                }
            });
        });

        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
