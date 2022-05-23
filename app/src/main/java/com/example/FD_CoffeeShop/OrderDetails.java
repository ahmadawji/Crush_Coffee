package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {
    Button addBasket;
    Customer c;
    CheckBox cream,caramel, vanilla;
    float priceOfItem;
    ImageView imageItem;
    int quantity=1, sugarQuantity=1;
    int index, addOnsCount;
    Order o;
    ProgressBar progOrdDet;
    RadioGroup size, sugar;
    String priceOfProduct;
    TextView value, price;

    // variable for shared preferences.
    public static final String SHARED_PREFS="FD_prefs";
    public static final String BASKETID="basket_ID";
    SharedPreferences sharedpreferences;
    //To manage sessions
    String basketIDSess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        basketIDSess= sharedpreferences.getString(BASKETID, null);

        Intent i = getIntent();
        index=i.getIntExtra("position",0);
        c= (Customer) i.getExtras().getSerializable("customer");

        priceOfItem=quantity*Data_Category.category[index].getPrice();
        o=new Order(LocalDate.now(), false, quantity, priceOfItem, "small", "regular","",Data_Category.category[index] );


        imageItem =(ImageView) findViewById(R.id.ivImageItem);
        value=(TextView) findViewById(R.id.tvValue);
        price=(TextView) findViewById(R.id.tvPrice);
        size=(RadioGroup) findViewById(R.id.rgSize);
        sugar=(RadioGroup) findViewById(R.id.rgSugar);
        cream=(CheckBox) findViewById(R.id.cbCream);
        caramel=(CheckBox) findViewById(R.id.cbCaramel);
        vanilla=(CheckBox) findViewById(R.id.cbVanilla);
        addBasket=(Button) findViewById(R.id.btAddBasket);
        progOrdDet= (ProgressBar) findViewById(R.id.pbProg2);

        imageItem.setImageResource(Data_Category.category[index].getImage());

        priceOfProduct=String.valueOf(Data_Category.category[index].getPrice());

        priceOfProduct="$"+priceOfProduct;
        price.setText(priceOfProduct);

        cream.setOnClickListener(changePrice1);
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
                o.setSugar("regular");
            }else if(sugarAmmountValue.compareTo("double")==0){
                o.setSugar("double");
            }else{
                o.setSugar("triple");
            }

            String addOns="";
            ArrayList<String> addOnsList= new ArrayList<String>();
            if(cream.isChecked())
                addOnsList.add("Cream");
            if(caramel.isChecked())
                addOnsList.add("Caramel");
            if(vanilla.isChecked())
                addOnsList.add("Vanilla");

            //code to make addons grammarly correct
            for(int i=0; i<addOnsList.size(); i++){
                if(addOnsList.size()>1) {
                    if(i!= addOnsList.size() - 1)
                    addOns += addOnsList.get(i) + ", ";
                    else
                        addOns += "and " + addOnsList.get(i);
                }else
                    addOns+=addOnsList.get(i);

            }


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
        Toast.makeText(OrderDetails.this, "Basket id: "+basketIDSess, Toast.LENGTH_LONG).show();


        String catID=String.valueOf(o.getCategoryId());
        String orderID= basketIDSess;
        String quantity=String.valueOf(o.getQuantity());
        String price = String.valueOf((int)o.getPrice());
        String description="";
        if (o.getQuantity()>1 && !o.getAddOns().isEmpty()){
            description=String.format("%d %s %s sugar %ss added with %s.",o.getQuantity(), o.getSize(), o.getSugar(), Data_Category.category[index].getName(), o.getAddOns());
        }else if(o.getQuantity()==1 && !o.getAddOns().isEmpty()){
            description=String.format("%d %s %s sugar %s added with %s.",o.getQuantity(), o.getSize(), o.getSugar(), Data_Category.category[index].getName(), o.getAddOns());
        }else{
            description=String.format("%d %s %s sugar %s.",o.getQuantity(), o.getSize(), o.getSugar(), Data_Category.category[index].getName());
        }

        System.out.println(String.format("catID: %s orderID: %s quantity: %s price: %s description: %s", catID, orderID, quantity, price, description));

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("category_id",catID);
            object.put("order_id",orderID);
            object.put("quantity",quantity);
            object.put("price",price);
            object.put("description",description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Coffee_API_URLs.ADDITEM;

        progOrdDet.setVisibility(View.VISIBLE);
        addBasket.setEnabled(false);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                Toast.makeText(OrderDetails.this, response.getString("success"), Toast.LENGTH_LONG).show();
                progOrdDet.setVisibility(View.INVISIBLE);
                addBasket.setEnabled(true);

                }catch (Exception e){
                    Toast.makeText(OrderDetails.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetails.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                progOrdDet.setVisibility(View.INVISIBLE);
                addBasket.setEnabled(true);
            }
        });

        queue.add(request);
    }

    View.OnClickListener changePrice1= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cream.isChecked()){
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
        //Our if condition here will prevent the priceOfItem to decrease under the product's original price and will prevent the price to turn negative on decrement
        if(priceOfItem>=Data_Category.category[index].getPrice()*2+addOnsCount) {
            /*
            Example of error:
             if addOns were 3
            * 4 >= 2*2+3
            * 4 >= 7
            * if I turned off 3 checkboxes and I clicked on the decrement button the value will turn negative
            * If clicked on the decrement 4=4-2=2
            * and if I turned off the checkboxes
            * 2=2-3=-1 (Negative value which is invalid)
            *
            * */
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