package com.example.grocart;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CartManager {

    private static final String DB_URL = "https://grocart-a0cab-default-rtdb.firebaseio.com/";
    private static final ArrayList<Product> cartList = new ArrayList<>();

    public static void addItem(Product product) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        DatabaseReference cartRef = FirebaseDatabase.getInstance(DB_URL).getReference("Cart").child(uid);

        for (Product p : cartList) {
            if (p.getId().equals(product.getId())) {
                int newQty = p.getQuantity() + 1;
                p.setQuantity(newQty);
                cartRef.child(p.getId()).child("quantity").setValue(String.valueOf(newQty));
                return;
            }
        }

        product.setQuantity(1);
        cartList.add(product);

        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", product.getId());
        map.put("quantity", "1");
        cartRef.child(product.getId()).setValue(map);
    }

    public static void removeItem(Product product) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;
        DatabaseReference cartRef = FirebaseDatabase.getInstance(DB_URL).getReference("Cart").child(uid);

        for (int i = 0; i < cartList.size(); i++) {
            Product p = cartList.get(i);
            if (p.getId().equals(product.getId())) {
                if (p.getQuantity() > 1) {
                    int newQty = p.getQuantity() - 1;
                    p.setQuantity(newQty);
                    cartRef.child(p.getId()).child("quantity").setValue(String.valueOf(newQty));
                } else {
                    cartList.remove(i);
                    cartRef.child(p.getId()).removeValue();
                }
                return;
            }
        }
    }

    public static ArrayList<Product> getItems() {
        return new ArrayList<>(cartList);
    }

    public static int getTotalAmount() {
        int total = 0;
        for (Product p : cartList) {
            total += p.getPrice() * p.getQuantity();
        }
        return total;
    }

    public static void clearCart() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            FirebaseDatabase.getInstance(DB_URL).getReference("Cart").child(uid).removeValue();
        }
        cartList.clear();
    }
}
