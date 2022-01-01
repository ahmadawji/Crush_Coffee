package com.example.crush_coffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Basket extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        drawerLayout= findViewById(R.id.drawer_layout);
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