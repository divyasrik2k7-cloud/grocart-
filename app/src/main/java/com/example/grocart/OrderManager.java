package com.example.grocart;

import java.util.ArrayList;

public class OrderManager {
    private static final ArrayList<Order> orderList = new ArrayList<>();

    public static void addOrder(Order order) {
        orderList.add(0, order); // Add to top of list
    }

    public static ArrayList<Order> getOrders() {
        return new ArrayList<>(orderList);
    }
}
