package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Basket extends AppCompatActivity implements AdapterView.OnItemClickListener {
    OrderItemAdapter basketAdapter;
    Button generateQR;
    DrawerLayout drawerLayout;
    ListView basketItems;
    final String username =Customer.USERNAME;
    private ArrayList<OrderItem>itemsInBasket= new ArrayList<>();

    //For Session management
    public static final String SHARED_PREFS="FD_prefs";
    public static final String BASKETID="basket_ID";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    //To manage sessions
    String basketIDSess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        basketIDSess= sharedpreferences.getString(BASKETID, null);

        drawerLayout= findViewById(R.id.drawer_layout);
        basketItems= (ListView) findViewById(R.id.lvBasketItems);
        generateQR = findViewById(R.id.btGenerateQR);

        basketAdapter= new OrderItemAdapter(this, R.layout.items_adapter_view, itemsInBasket);
        basketItems.setAdapter(basketAdapter);

        basketItems.setOnItemClickListener(this);
        generateQR.setOnClickListener(showQr);
        getOrdersInBasket(basketIDSess);

    }

    //click listener for generateQR button
    View.OnClickListener showQr= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Basket.this, QRcodeActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("position "+position);
    }

//    public void getOrdersInBasket(String basketId){
//            final RequestQueue queue = Volley.newRequestQueue(this);
//            //String URL= Coffee_API_URLs.ORDERSINBASKET+"?cun="+username;
////            System.out.println("Username: "+username);
//            System.out.println("URL: "+Coffee_API_URLs.ORDERSINBASKET+basketId);
//            String URL =Coffee_API_URLs.ORDERSINBASKET+basketId;
//            JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//                    try {
//                        for (int i = 0;i < response.length();i++) {
//                            JSONObject row = response.getJSONObject(i);
//                            int orderId = row.getInt("id");
//                            int quantity = row.getInt("quantity");
//                            int catId=row.getInt("category_id");
//                           // itemsInBasket.add(new OrderItem(orderId, quantity, Data_Category.category[catId-1]));
//                        }
//
//
//                    } catch (Exception ex) {
//                        Toast.makeText(Basket.this, "No records found", Toast.LENGTH_SHORT).show();
//                    }
//
//                    basketAdapter.notifyDataSetChanged();
//                }
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(Basket.this, "At fill: "+error.toString(), Toast.LENGTH_LONG).show();
//                    Log.d("error", error.toString());
//                }
//            });
//
//            queue.add(request);
//
//    }


    public void getOrdersInBasket(String basketId){
        final RequestQueue queue = Volley.newRequestQueue(this);
        //String URL= Coffee_API_URLs.ORDERSINBASKET+"?cun="+username;
        //System.out.println("Username: "+username);
        System.out.println("URL: "+Coffee_API_URLs.ORDERSINBASKET+basketId);
        String URL =Coffee_API_URLs.ORDERSINBASKET+basketId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray itemsArray = response.getJSONArray("items");

                    for (int i = 0;i < itemsArray.length();i++) {
                            JSONObject row = itemsArray.getJSONObject(i);
                            float price = (float) row.getInt("price");
                            //System.out.println("price: "+price);
                            String description = row.getString("description");
                            int imageId=Data_Category.categoryImagesMap.get(String.valueOf(row.getInt("category_id"))).getImage();
                            itemsInBasket.add(new OrderItem(price, imageId, description));
                    }

                }catch (Exception e){
                    Toast.makeText(Basket.this, "error: "+e, Toast.LENGTH_LONG).show();
                }

                basketAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Basket.this, "At fill: "+error.toString(), Toast.LENGTH_LONG).show();
                Log.d("error", error.toString());
            }
        });

        queue.add(request);

    }








    public void goToMenuActivity(View view){
        redirectActivity(this, MenuActivity.class);
    }

    public void goToBasket(View view){
        //Recreate activity
        recreate();
        //Redirect activity to about us
        MenuActivity.redirectActivity(this, Basket.class);
    }

    public void clickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open the drawer layout
        drawerLayout.openDrawer(GravityCompat.START);

    }

    public   static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer Layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        // Initialize intent
        Intent intent=new Intent(activity,aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public void clickLogout(View view){
        //Close app
        logout(this);

    }

    public static void logout(Activity activity) {
        //Initialize alert dialog
        //used to display the dialog message with OK and Cancel buttons
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        //Set title for AlertDialog
        builder.setTitle("Logout");
        //Set message for AlertDialog
        builder.setMessage("Are you sure you want to logout?");
        //Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                activity.finishAffinity();
                //Exit app
                System.exit(0);


            }
        });
        //Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //Show  dialog
        builder.show();

    }

    public void clearBasket(View view) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = Coffee_API_URLs.DELETEFROMBASKET+basketIDSess;

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Basket.this, response, Toast.LENGTH_LONG).show();
                System.out.println("delete: "+response);
                if(response.compareTo("{\"message\":\"items deleted successfuly\"}")==0){
                    itemsInBasket.clear();
                    basketAdapter.notifyDataSetChanged();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Basket.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Fail: "+ error.toString());

            }
        });

        queue.add(request);
    }


}