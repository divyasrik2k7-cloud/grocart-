package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.UpdatePriceListener {

    RecyclerView cartRecyclerView;
    TextView totalAmountTv;
    Button proceedBtn;
    ImageView backBtn;

    CartAdapter adapter;
    ArrayList<Product> cartItems;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTv = findViewById(R.id.totalAmountTv);
        proceedBtn = findViewById(R.id.proceedBtn);
        backBtn = findViewById(R.id.backBtn);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getItems();
        adapter = new CartAdapter(this, cartItems, this);
        cartRecyclerView.setAdapter(adapter);

        updateTotal();

        // ✅ Back button functionality
        backBtn.setOnClickListener(v -> finish());

        // ✅ Navigation to OrderActivity
        proceedBtn.setOnClickListener(v -> {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, OrderActivity.class);
                i.putExtra("total", CartManager.getTotalAmount());
                startActivity(i);
            }
        });
    }

    @Override
    public void onPriceUpdated() {
        updateTotal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCart();
    }

    private void refreshCart() {
        cartItems.clear();
        cartItems.addAll(CartManager.getItems());
        adapter.notifyDataSetChanged();
        updateTotal();
    }

    private void updateTotal() {
        totalAmountTv.setText("Total Amount = ₹" + CartManager.getTotalAmount());
    }
}
