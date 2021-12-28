package com.example.crush_coffee;

public class Category {
    private int id;
    private String name;
    private int image;

    public Category(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image=image;
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

    @Override
    public String toString() {
        return  name ;
    }
}
