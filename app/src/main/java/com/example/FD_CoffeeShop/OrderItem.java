package com.example.FD_CoffeeShop;

public class OrderItem {
    private float price;
    private int image;
    private String description;

    public OrderItem(float price, int image, String description) {
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
