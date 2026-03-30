package com.example.grocart;

public class Product {

    private String id;
    private String name;
    private int price;
    private String image;
    private int quantity;

    // Firebase needs an empty constructor
    public Product() {
    }

    public Product(String id, String name, int price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = 1;
    }

    public String getId() { return id; }

    // Added the missing setId method
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public int getPrice() { return price; }

    public String getImage() { return image; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return name + " - ₹" + price;
    }
}
