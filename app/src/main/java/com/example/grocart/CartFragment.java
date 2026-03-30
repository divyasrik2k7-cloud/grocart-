package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.UpdatePriceListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private final ArrayList<Product> cartItems = new ArrayList<>();
    private TextView totalAmount, emptyCartMsg;
    private Button btnProceed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize Views
        recyclerView = v.findViewById(R.id.cartRecyclerView);
        totalAmount = v.findViewById(R.id.totalAmount);
        emptyCartMsg = v.findViewById(R.id.emptyCartMsg);
        btnProceed = v.findViewById(R.id.btnProceed);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize adapter once
        adapter = new CartAdapter(getContext(), cartItems, this);
        recyclerView.setAdapter(adapter);

        updateCartUI();

        btnProceed.setOnClickListener(view -> {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("total", CartManager.getTotalAmount());
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onPriceUpdated() {
        updateCartUI();
    }

    public void updateCartUI() {
        // Update the list data without creating a new adapter
        ArrayList<Product> currentItems = CartManager.getItems();
        cartItems.clear();
        cartItems.addAll(currentItems);
        
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (cartItems.isEmpty()) {
            emptyCartMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            totalAmount.setText("₹0");
        } else {
            emptyCartMsg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            totalAmount.setText("₹" + CartManager.getTotalAmount());
        }
    }
}
