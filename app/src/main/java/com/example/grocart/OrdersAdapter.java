package com.example.grocart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private final ArrayList<Order> orderList;

    public OrdersAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderDate.setText(order.getDate());
        holder.orderTotal.setText("Total: ₹" + order.getTotalAmount());
        holder.orderStatus.setText("Status: Placed (COD)");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
