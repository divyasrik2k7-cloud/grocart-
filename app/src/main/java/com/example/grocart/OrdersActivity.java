package com.example.grocart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";
    RecyclerView recyclerView;
    OrdersAdapter adapter;
    ArrayList<Order> orderList;
    LinearLayout emptyOrdersLayout;
    Button btnGoShopping;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_orders);

        recyclerView = findViewById(R.id.ordersRecyclerView);
        emptyOrdersLayout = findViewById(R.id.emptyOrdersLayout);
        btnGoShopping = findViewById(R.id.btnGoShopping);
        backBtn = findViewById(R.id.backBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(orderList);
        recyclerView.setAdapter(adapter);

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        btnGoShopping.setOnClickListener(view -> finish());

        fetchOrdersFromFirebase();
    }

    private void fetchOrdersFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        // Ensure collection name is "Orders" (Matches OrderActivity)
        DatabaseReference ref = FirebaseDatabase.getInstance(DB_URL).getReference("Orders").child(uid);
        
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        String id = data.child("orderId").getValue(String.class);
                        String date = data.child("timestamp").getValue(String.class);
                        // Using String.valueOf to be safe with different data types in DB
                        Object totalObj = data.child("totalAmount").getValue();
                        int total = 0;
                        if (totalObj != null) {
                            total = Integer.parseInt(totalObj.toString());
                        }

                        Order order = new Order(id, null, total, date);
                        orderList.add(0, order); // Newest first
                    } catch (Exception e) {
                        Log.e("OrdersActivity", "Error parsing order: " + data.getKey(), e);
                    }
                }
                
                if (orderList.isEmpty()) {
                    emptyOrdersLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyOrdersLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
