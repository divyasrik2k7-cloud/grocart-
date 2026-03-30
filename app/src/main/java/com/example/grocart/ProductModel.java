package com.example.grocart;

public class ProductModel {

    String name, price, image;

    public ProductModel() {} // required for Firebase

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
}