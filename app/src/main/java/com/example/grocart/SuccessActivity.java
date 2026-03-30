package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Button btnBackHome = findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(v -> {
            // Clear the cart after successful order
            CartManager.clearCart();
            
            // Go back to MainActivity and clear activity stack
            Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
