package com.example.FD_CoffeeShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Handler h=new Handler();//for the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting token for firebase messaging as stated in documentation: 'https://firebase.google.com/docs/cloud-messaging/android/client?authuser=3'
        //firebase of app: https://console.firebase.google.com/u/3/project/faster-dequeue-83e3a/notification/compose
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println("Your device registration token is: "+token);
                        Toast.makeText(MainActivity.this,"Your device registration token is: "+ token, Toast.LENGTH_SHORT).show();
                    }
                });

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, loginActivity.class);
                startActivity(i);//go to login activity
            }
        },2000);//delay time
    }
}