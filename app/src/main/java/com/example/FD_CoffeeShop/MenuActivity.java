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
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvCat;
    TextView tvCusName;
    Customer c;
    DrawerLayout drawerLayout;

    //For Session management
    public static final String SHARED_PREFS="FD_prefs";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedpreferences=getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        //Assign variable
        drawerLayout=findViewById(R.id.drawer_layout);
        try {
            tvCusName = (TextView) findViewById(R.id.tvCustomerName);
            tvCusName.setText(Customer.CUSTOMERNAME);

        }catch(NullPointerException e){
            Log.d("error:",e.toString());
        }
        lvCat=(ListView) findViewById(R.id.lvCategory);
        ProductAdapter pa = new ProductAdapter(this,R.layout.product_adapter_view, Data_Category.category);
        //ArrayAdapter a = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, Data_Category.category);
        lvCat.setAdapter(pa);
        lvCat.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent i1 = new Intent(this, OrderDetails.class);
        i1.putExtra("position",i);
        i1.putExtra("customer", c);
        startActivity(i1);
    }

    public void goToMenuActivity(View view){
        recreate();
    }

    public void goToBasket(View view){
        redirectActivity(this, Basket.class);
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
        // calling method to edit values in shared prefs.
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // below line will clear
        // the data in shared prefs.
        editor.clear();

        // below line will apply empty
        // data to shared prefs.
        editor.apply();
        //Close app
        logout(this);

    }

    public void logout(Activity activity) {
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
                // starting mainactivity after
                // clearing values in shared preferences.
                Intent i = new Intent(MenuActivity.this, loginActivity.class);
                startActivity(i);
                finish();
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