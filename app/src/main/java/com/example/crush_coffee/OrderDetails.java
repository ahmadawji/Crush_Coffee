package com.example.crush_coffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {
    Button addBasket, decrement;
    Customer c;
    CheckBox cocoa,caramel, vanilla;
    float priceOfItem;
    ImageView imageItem;
    int quantity=1, sugarQuantity=1;
    int index, addOnsCount;
    Order o;
    ProgressBar progOrdDet;
    RadioGroup size, sugar;
    String priceOfProduct;
    TextView value, price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent i = getIntent();
        index=i.getIntExtra("position",0);
        c= (Customer) i.getExtras().getSerializable("customer");

        priceOfItem=quantity*Data_Category.category[index].getPrice();
        o=new Order(LocalDate.now(), false, quantity, priceOfItem, "small", 1,"",Data_Category.category[index], c );


        imageItem =(ImageView) findViewById(R.id.ivImageItem);
        value=(TextView) findViewById(R.id.tvValue);
        price=(TextView) findViewById(R.id.tvPrice);
        size=(RadioGroup) findViewById(R.id.rgSize);
        sugar=(RadioGroup) findViewById(R.id.rgSugar);
        cocoa=(CheckBox) findViewById(R.id.cbCocoa);
        caramel=(CheckBox) findViewById(R.id.cbCaramel);
        vanilla=(CheckBox) findViewById(R.id.cbVanilla);
        addBasket=(Button) findViewById(R.id.btAddBasket);
        decrement=(Button) findViewById(R.id.btDecrement);
        progOrdDet= (ProgressBar) findViewById(R.id.pbProg2);

        imageItem.setImageResource(Data_Category.category[index].getImage());

        priceOfProduct=String.valueOf(Data_Category.category[index].getPrice());

        priceOfProduct="$"+priceOfProduct;
        price.setText(priceOfProduct);

        cocoa.setOnClickListener(changePrice1);
        caramel.setOnClickListener(changePrice2);
        vanilla.setOnClickListener(changePrice3);

        addBasket.setOnClickListener(sendBasket);

    }

    View.OnClickListener sendBasket = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        try{
            o.setQuantity(quantity);

            o.setPrice(priceOfItem);

            //Get checked radio button using its related RadioGroup
            RadioButton sizeCoffee= (RadioButton) findViewById(size.getCheckedRadioButtonId());
            RadioButton sugarAmmount= (RadioButton) findViewById(sugar.getCheckedRadioButtonId());
            String sizeCoffeeValue= sizeCoffee.getText().toString();
            String sugarAmmountValue= sugarAmmount.getText().toString();
            if(sizeCoffeeValue.compareTo("small")==0){
                o.setSize("small");
            }else if(sizeCoffeeValue.compareTo("medium")==0){
                o.setSize("medium");
            }else{
                o.setSize("large");
            }
            if(sugarAmmountValue.compareTo("regular")==0){
                o.setSugar(1);
            }else if(sugarAmmountValue.compareTo("double")==0){
                o.setSugar(2);
            }else{
                o.setSugar(3);
            }

            String addOns="";
            if(cocoa.isChecked())
                addOns+=addOns+"1.Cocoa ";
            if(caramel.isChecked())
                addOns+="2.Caramel ";
            if(vanilla.isChecked())
                addOns+="3.Vanilla";
            o.setAddOns(addOns);

        }   catch(Exception e){
            Toast.makeText(OrderDetails.this, e.toString(), Toast.LENGTH_LONG).show();
        }

            System.out.println(o.toString());
            System.out.println(Data_Category.category[index].getPrice());
            sendBasketServer(o);
        }
    };

    public void sendBasketServer(Order o){

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = Coffee_API_URLs.ADDORDER;

        progOrdDet.setVisibility(View.VISIBLE);
        addBasket.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(OrderDetails.this, response, Toast.LENGTH_LONG).show();
                progOrdDet.setVisibility(View.INVISIBLE);
                addBasket.setEnabled(true);
                System.out.println(response.trim()=="error");
                System.out.println(response.trim().compareTo("error"));
                if(response.trim().compareTo("error")!=0){
                     Intent i= new Intent(OrderDetails.this, MenuActivity.class);
                     startActivity(i);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetails.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                progOrdDet.setVisibility(View.INVISIBLE);
                addBasket.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderDate",o.getOrder_date().toString());
                params.put("quantity",String.valueOf(o.getQuantity()));
                params.put("price",String.valueOf(o.getPrice()));
                params.put("size", o.getSize());
                params.put("sugar",String.valueOf(o.getSugar()));
                params.put("addOns",o.getAddOns());
                params.put("categoryId",String.valueOf(o.getCategoryId()));
                params.put("customerUsername",o.getUserName());
                params.put("orderTime",o.getOrderTime().toString());

                //params.put("key", "cuBubcDE");
                return params;
            }
        };

        queue.add(request);
    }

    View.OnClickListener changePrice1= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cocoa.isChecked()){
                addOnsCount++;
                priceOfItem+=1;
            }else{
                addOnsCount--;
                priceOfItem-=1;
            }
            priceOfProduct=String.valueOf(priceOfItem);
            priceOfProduct="$"+priceOfProduct;
            price.setText(priceOfProduct);
        }
    };

    View.OnClickListener changePrice2= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(caramel.isChecked()){
                addOnsCount++;
                priceOfItem+=1;
            }else{
                addOnsCount--;
                priceOfItem-=1;
            }
            priceOfProduct=String.valueOf(priceOfItem);
            priceOfProduct="$"+priceOfProduct;
            price.setText(priceOfProduct);
        }
    };

    View.OnClickListener changePrice3= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(vanilla.isChecked()){
                addOnsCount++;
                priceOfItem+=1;
            }else{
                addOnsCount--;
                priceOfItem-=1;
            }

            priceOfProduct=String.valueOf(priceOfItem);
            priceOfProduct="$"+priceOfProduct;
            price.setText(priceOfProduct);
        }
    };

    public void decrement(View view){
        System.out.println("price: "+priceOfItem);
        if(quantity<=1)
            quantity=1;
        else
            quantity--;
            value.setText(""+quantity);
        if(priceOfItem>=Data_Category.category[index].getPrice()*2+addOnsCount) {
            priceOfItem -= Data_Category.category[index].getPrice();
            priceOfProduct = String.valueOf(priceOfItem);
            priceOfProduct = "$" + priceOfProduct;
            price.setText(priceOfProduct);
        }

    }

    public void increment(View view) {
        quantity++;
        value.setText(""+quantity);
        priceOfItem+=Data_Category.category[index].getPrice();
        priceOfProduct=String.valueOf(priceOfItem);
        priceOfProduct="$"+priceOfProduct;
        price.setText(priceOfProduct);
    }
}