package com.example.crush_coffee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Order {
    private int id;
    private LocalDate order_date;
    private Boolean paid;
    private int quantity;
    private float price;
    private String size;
    private int sugar;
    private String addOns;
    private Category category;
    private Customer customer;
    private LocalTime orderTime;

    public Order(LocalDate order_date, Boolean paid, int quantity, float price, String size, int sugar, String addOns, Category category, Customer customer, LocalTime orderTime) {
        this.order_date = order_date;
        this.paid = paid;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.sugar = sugar;
        this.addOns = addOns;
        this.category = category;
        this.customer = customer;
        this.orderTime = orderTime;
    }

}
