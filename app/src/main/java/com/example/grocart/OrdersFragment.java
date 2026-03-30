package com.example.grocart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private final ArrayList<Order> orderList = new ArrayList<>();
    private LinearLayout emptyOrdersLayout;
    private Button btnGoShopping;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = v.findViewById(R.id.ordersRecyclerView);
        emptyOrdersLayout = v.findViewById(R.id.emptyOrdersLayout);
        btnGoShopping = v.findViewById(R.id.btnGoShopping);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrdersAdapter(orderList);
        recyclerView.setAdapter(adapter);

        btnGoShopping.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNav.setSelectedItemId(R.id.home);
            }
        });

        fetchOrdersFromFirebase();

        return v;
    }

    private void fetchOrdersFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance(DB_URL).getReference("Orders").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    // Mapping Firebase data to Order object
                    try {
                        String id = data.child("orderId").getValue(String.class);
                        String date = data.child("timestamp").getValue(String.class);
                        String totalStr = data.child("totalAmount").getValue(String.class);
                        int total = totalStr != null ? Integer.parseInt(totalStr) : 0;

                        Order order = new Order(id, null, total, date);
                        orderList.add(0, order); // Newest orders first
                    } catch (Exception e) {
                        Log.e("OrdersFragment", "Error parsing order", e);
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
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
