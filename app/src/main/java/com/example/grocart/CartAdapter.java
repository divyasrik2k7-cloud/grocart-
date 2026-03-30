package com.example.grocart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Product> list;
    private final UpdatePriceListener listener;

    // Interface to communicate price updates
    public interface UpdatePriceListener {
        void onPriceUpdated();
    }

    public CartAdapter(Context context, ArrayList<Product> list, UpdatePriceListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, plusBtn, minusBtn;
        TextView name, price, quantity;

        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.cartImage);
            name = v.findViewById(R.id.cartName);
            price = v.findViewById(R.id.cartPrice);
            quantity = v.findViewById(R.id.cartQuantity);
            plusBtn = v.findViewById(R.id.plusBtn);
            minusBtn = v.findViewById(R.id.minusBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Product p = list.get(position);

        h.name.setText(p.getName());
        h.price.setText("₹" + (p.getPrice() * p.getQuantity()));
        h.quantity.setText(String.valueOf(p.getQuantity()));

        Glide.with(context)
                .load(p.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(h.image);

        h.plusBtn.setOnClickListener(v -> {
            CartManager.addItem(p);
            notifyDataSetChanged();
            if (listener != null) listener.onPriceUpdated();
        });

        h.minusBtn.setOnClickListener(v -> {
            CartManager.removeItem(p);
            // Refresh the whole list because items might be removed
            list.clear();
            list.addAll(CartManager.getItems());
            notifyDataSetChanged();
            if (listener != null) listener.onPriceUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
