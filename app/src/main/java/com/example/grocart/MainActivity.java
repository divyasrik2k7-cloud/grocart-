package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    LinearLayout veg, fruits, meat, dairy, snacks, home, cooking;
    BottomNavigationView bottomNav;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        veg = findViewById(R.id.veg);
        fruits = findViewById(R.id.fruits);
        meat = findViewById(R.id.meat);
        dairy = findViewById(R.id.dairy);
        snacks = findViewById(R.id.snacks);
        home = findViewById(R.id.home);
        cooking = findViewById(R.id.cooking);

        bottomNav = findViewById(R.id.bottomNav);
        searchEditText = findViewById(R.id.searchEditText);

        //  Category clicks
        veg.setOnClickListener(v -> open("vegetables"));
        fruits.setOnClickListener(v -> open("fruits"));
        meat.setOnClickListener(v -> open("meat"));
        dairy.setOnClickListener(v -> open("dairy"));
        snacks.setOnClickListener(v -> open("snacks"));
        home.setOnClickListener(v -> open("home"));
        cooking.setOnClickListener(v -> open("cooking"));

        // SEARCH BUTTON LOGIC
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    intent.putExtra("search_query", query);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter an item to search", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        //  Bottom Navigation Logic
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                // If using activities, stay here or recreate. 
                // If using fragments, load HomeFragment.
                return true;
            } else if (id == R.id.cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else if (id == R.id.orders) {
                startActivity(new Intent(MainActivity.this, OrdersActivity.class));
            } else if (id == R.id.profile) {
                // Since Profile is a Fragment, we could load it in a container or open as Activity.
                // For consistency with your structure (Cart/Orders are Activities), let's use OrdersActivity approach if exists.
                // But you have ProfileFragment, so let's load it if there is a container, otherwise show toast or create ProfileActivity.
                
                // If you want to use ProfileFragment inside MainActivity:
                // loadFragment(new ProfileFragment());
                
                // Alternatively, if you want a separate page like Cart:
                // startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                
                // Since we have the Fragment, I'll update MainActivity to support fragment swapping if a container is added, 
                // but for now, I'll provide a way to see it.
                Toast.makeText(this, "Opening Profile...", Toast.LENGTH_SHORT).show();
                // If you don't have a container, you might need a ProfileActivity. 
                // Let's create a simple ProfileActivity to host the fragment.
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
            return true;
        });
    }

    private void open(String category) {
        Intent i = new Intent(this, CategoryActivity.class);
        i.putExtra("category", category);
        startActivity(i);
    }
}
