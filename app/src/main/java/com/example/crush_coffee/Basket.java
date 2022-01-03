package com.example.crush_coffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Basket extends AppCompatActivity implements AdapterView.OnItemClickListener {
    DrawerLayout drawerLayout;
    ListView basketOrders;
    ArrayAdapter basketAdapter;
    final String username =Customer.USERNAME;
    private ArrayList<Order>ordersInBasket= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        drawerLayout= findViewById(R.id.drawer_layout);
        basketOrders= (ListView) findViewById(R.id.lvBasketOrders);

        basketAdapter= new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1, ordersInBasket);
        basketOrders.setAdapter(basketAdapter);

        basketOrders.setOnItemClickListener(this);
        getOrdersInBasket(username);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("position "+position);
    }

    public void getOrdersInBasket(String username){
            final RequestQueue queue = Volley.newRequestQueue(this);
            String URL= Coffee_API_URLs.ORDERSINBASKET+"?cun="+username;
            JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0;i < response.length();i++) {
                            JSONObject row = response.getJSONObject(i);
                            int orderId = row.getInt("id");
                            int quantity = row.getInt("quantity");
                            int catId=row.getInt("category_id");
                            ordersInBasket.add(new Order(orderId, quantity, Data_Category
                            .category[catId-1]));
                        }


                    } catch (Exception ex) {
                        Toast.makeText(Basket.this, "No records found", Toast.LENGTH_SHORT).show();
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

}