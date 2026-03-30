package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";
    TextView totalBill;
    Button confirmBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_order);

        totalBill = findViewById(R.id.totalBill);
        confirmBtn = findViewById(R.id.confirmBtn);
        backBtn = findViewById(R.id.backBtn);

        int total = getIntent().getIntExtra("total", 0);
        totalBill.setText("Total Bill: ₹" + total);

        backBtn.setOnClickListener(v -> finish());

        confirmBtn.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getUid();
            if (uid == null) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Prepare Order Data
            String orderId = "#ORD" + System.currentTimeMillis() % 100000;
            String timestamp = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

            HashMap<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", orderId);
            orderMap.put("timestamp", timestamp);
            orderMap.put("totalAmount", String.valueOf(total));
            orderMap.put("products", CartManager.getItems());

            // 2. Save to Firebase: Orders / uid / orderKey
            DatabaseReference ordersRef = FirebaseDatabase.getInstance(DB_URL).getReference("Orders").child(uid);
            ordersRef.push().setValue(orderMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // 3. Clear Cart and Go to Success
                    CartManager.clearCart();
                    Intent i = new Intent(this, SuccessActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Order failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
