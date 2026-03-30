package com.example.grocart;

import android.os.Bundle;
import android.content.*;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    TextView name, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        name.setText(getIntent().getStringExtra("name"));
        price.setText(getIntent().getStringExtra("price"));
        Button addToCart = findViewById(R.id.button);

        addToCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
    }
}