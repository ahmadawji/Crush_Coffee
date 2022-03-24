package com.example.FD_CoffeeShop;

public class Category {
    private int id;
    private String name;
    private int image;
    private float price;

    public Category(){}

    public Category(int id, String name, int image, float price) {
        this.id = id;
        this.name = name;
        this.image=image;
        this.price =price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
