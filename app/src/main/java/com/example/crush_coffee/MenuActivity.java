package com.example.crush_coffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView lvCat;
TextView tvCusName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        Customer c= (Customer) i.getExtras().getSerializable("customer");
        tvCusName =(TextView) findViewById(R.id.tvCustomerName);
        tvCusName.setText(c.getName());
        Customer.loggedIn=true;

        lvCat=(ListView) findViewById(R.id.lvCategory);
        ArrayAdapter a = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, Data_Category.category);
        lvCat.setAdapter(a);
        lvCat.setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent i1 = new Intent(this, OrderDetails.class);
        i1.putExtra("position",i);
        startActivity(i1);
    }




}