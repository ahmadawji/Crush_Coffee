package com.example.FD_CoffeeShop;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private int id;
    private LocalDate order_date;
    private Boolean paid;
    private int quantity;
    private float price;
    private String size;
    private String sugar;
    private String addOns;
    private Category category;
    private Customer customer;
    private LocalTime orderTime;

    public Order(int id, int quantity, Category category ){
        this.id=id;
        this.quantity=quantity;
        this.category= category;
    }

    public Order(LocalDate order_date, Boolean paid, int quantity, float price, String size, String sugar, String addOns, Category category) {
        this.order_date = order_date;
        this.paid = paid;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.sugar = sugar;
        this.addOns = addOns;
        this.category = category;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }


    public LocalTime getOrderTime() {
        return LocalTime.now();
    }

    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    public int getCategoryId(){
       return this.category.getId();
    }

    public String getUserName(){
        return Customer.USERNAME;
    }

    @Override
    public String toString() {
        if (quantity>1)
        return  quantity +" "+this.category.getName()+"s";
        return  quantity +" "+this.category.getName();
    }
}
