package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Handler h=new Handler();//for the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Customer.loggedIn) {
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);//go to login activity
                }else{
                    Intent i = new Intent(MainActivity.this, loginActivity.class);
                    startActivity(i);//go to login activity
                }
            }
        },2000);//delay time
    }
}