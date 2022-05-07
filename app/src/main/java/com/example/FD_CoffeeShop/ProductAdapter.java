package com.example.FD_CoffeeShop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Category> {

    private Context context;
    int myResource;


    public ProductAdapter(@NonNull Context context, int resource, Category[] objects) {
        super(context, resource, objects);
        this.context=context;
        myResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(myResource, null,true);
        TextView productCategory= rowView.findViewById(R.id.tvProduct);
        ImageView productImage= rowView.findViewById(R.id.ivProduct);

        productCategory.setText(Data_Category.category[position].getName());
        productImage.setImageResource(Data_Category.category[position].getImage());

        return rowView;
    }
}
