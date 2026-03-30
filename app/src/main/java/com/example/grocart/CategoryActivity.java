package com.example.grocart;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";
    // 🔥 Database URL - Added specifically to prevent crashes if google-services.json is missing it
    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";

    RecyclerView recyclerView;
    ArrayList<Product> list;
    ProductAdapter adapter;
    TextView title;
    ImageView backBtn;
    DatabaseReference ref;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);
        title = findViewById(R.id.title);
        backBtn = findViewById(R.id.backBtn);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        list = new ArrayList<>();
        adapter = new ProductAdapter(this, list);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> finish());

        String searchQuery = getIntent().getStringExtra("search_query");
        String category = getIntent().getStringExtra("category");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            title.setText("Results for: " + searchQuery);
            performSearch(searchQuery.toLowerCase().trim());
        } else if (category != null && !category.trim().isEmpty()) {
            String displayTitle = category.substring(0, 1).toUpperCase() + category.substring(1);
            title.setText(displayTitle);
            loadCategoryData(category.toLowerCase().trim());
        } else {
            Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadCategoryData(String category) {
        if (ref != null && listener != null) ref.removeEventListener(listener);

        ref = FirebaseDatabase.getInstance(DB_URL).getReference("products").child(category);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        Product p = data.getValue(Product.class);
                        if (p != null) {
                            p.setId(category + "_" + data.getKey());
                            list.add(p);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing product", e);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isFinishing()) Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        ref.addValueEventListener(listener);
    }

    private void performSearch(String query) {
        if (ref != null && listener != null) ref.removeEventListener(listener);

        ref = FirebaseDatabase.getInstance(DB_URL).getReference("products");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryName = categorySnapshot.getKey();
                    for (DataSnapshot productSnapshot : categorySnapshot.getChildren()) {
                        try {
                            Product p = productSnapshot.getValue(Product.class);
                            if (p != null && p.getName() != null) {
                                if (p.getName().toLowerCase().contains(query)) {
                                    p.setId(categoryName + "_" + productSnapshot.getKey());
                                    list.add(p);
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Search error", e);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isFinishing()) Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        ref.addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ref != null && listener != null) ref.removeEventListener(listener);
    }
}
