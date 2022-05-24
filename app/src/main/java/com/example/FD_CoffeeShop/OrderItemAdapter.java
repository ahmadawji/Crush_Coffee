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

import java.util.ArrayList;

public class OrderItemAdapter extends ArrayAdapter {

    private Context context;
    int myResource;
    private ArrayList<OrderItem> item;

    public OrderItemAdapter(@NonNull Context context, int resource, ArrayList<OrderItem> item) {
        super(context, resource, item);
        this.context=context;
        myResource=resource;
        this.item=item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(myResource, null,true);
        TextView productDescription= rowView.findViewById(R.id.tvProduct);
        TextView productPrice= rowView.findViewById(R.id.tvPrice);
        ImageView productImage= rowView.findViewById(R.id.ivProduct);


        productDescription.setText(item.get(position).getDescription());
        productImage.setImageResource(item.get(position).getImage());
        productPrice.setText("$"+String.valueOf(item.get(position).getPrice()));

        return rowView;
    }

}
