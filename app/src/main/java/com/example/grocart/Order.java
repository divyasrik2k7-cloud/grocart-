package com.example.grocart;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private ArrayList<Product> items;
    private int totalAmount;
    private String date;

    // Required for Firebase
    public Order() {}

    public Order(String orderId, ArrayList<Product> items, int totalAmount, String date) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public String getOrderId() { return orderId; }
    public ArrayList<Product> getItems() { return items; }
    public int getTotalAmount() { return totalAmount; }
    public String getDate() { return date; }
}
