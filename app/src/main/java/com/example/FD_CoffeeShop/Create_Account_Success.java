package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Create_Account_Success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_success);
    }

    public void goLogin(View view) {
        startActivity(new Intent(this, loginActivity.class));
    }
}