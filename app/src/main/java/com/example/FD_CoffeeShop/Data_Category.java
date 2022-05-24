package com.example.FD_CoffeeShop;

import java.util.HashMap;
import java.util.Map;

public class Data_Category {

    final static Category[] category={
            new Category(1, "Espresso", R.drawable.espresso, 2.0F),
            new Category(2, "Macchiato", R.drawable.macchiato, 4.0F),
            new Category(3, "Mocha", R.drawable.mocha, 5.0F),
            new Category(4, "Latte", R.drawable.latte,6.0F),
            new Category(5, "Cappucino", R.drawable.cuppucino,6.0F)
    };

    static final Map<String, Category> categoryImagesMap = new HashMap<>();
    static {
        categoryImagesMap.put("1", category[0]);
        categoryImagesMap.put("2", category[1]);
        categoryImagesMap.put("3", category[2]);
        categoryImagesMap.put("4", category[3]);
        categoryImagesMap.put("5", category[4]);

    }


}
