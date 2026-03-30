package com.example.grocart;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Product> list;

    public ProductAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;
        Button addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("₹" + product.getPrice());

        holder.addBtn.setOnClickListener(v -> {
            CartManager.addItem(product);
            Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });

        // Click on image to show full preview
        holder.image.setOnClickListener(v -> showProductPreview(product));

        // Enhanced Image Loading with Glide
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(context)
                    .load(product.getImage())
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void showProductPreview(Product product) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_product_preview, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        ImageView previewImage = dialogView.findViewById(R.id.previewImage);
        TextView previewName = dialogView.findViewById(R.id.previewName);
        TextView previewPrice = dialogView.findViewById(R.id.previewPrice);
        Button closeBtn = dialogView.findViewById(R.id.closeBtn);

        previewName.setText(product.getName());
        previewPrice.setText("₹" + product.getPrice());

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(context)
                    .load(product.getImage())
                    .fitCenter()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(previewImage);
        }

        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
