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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    // ⚠️ Check this URL matches exactly what is in your Firebase Console Realtime Database tab
    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";

    EditText etName, etEmail, etPassword;
    Button btnRegister;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.registerName);
        etEmail = findViewById(R.id.registerEmail);
        etPassword = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Name is required");
                return;
            }
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }

            // 1. Create user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = auth.getUid();
                    if (uid != null) {
                        // 2. Sync user data to "users" collection in Realtime Database
                        syncUserToDatabase(uid, name, email, password);
                    }
                } else {
                    String error = task.getException() != null ? task.getException().getMessage() : "Auth failed";
                    Log.e(TAG, "Registration failed: " + error);
                    Toast.makeText(this, "Auth Error: " + error, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void syncUserToDatabase(String uid, String name, String email, String password) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("users").child(uid);

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("password", password);

        dbRef.setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User data saved successfully for: " + uid);
                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Database error";
                Log.e(TAG, "Database Write Failed: " + error);
                Toast.makeText(RegisterActivity.this, "Database Sync Failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
