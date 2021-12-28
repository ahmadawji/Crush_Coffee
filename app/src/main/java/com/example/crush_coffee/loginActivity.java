package com.example.crush_coffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.text.SimpleDateFormat;

public class loginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    public ProgressBar prog1 ;
    Intent menuIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);//activebar
        //getSupportActionBar().setIcon(R.drawable.ic_baseline_shop_24);
        setContentView(R.layout.activity_login2);
        username=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);
        login=findViewById(R.id.bt_login);
        prog1 = findViewById(R.id.prog1);
        menuIntent= new Intent(loginActivity.this, MenuActivity.class);
        if(Customer.loggedIn) {
            startActivity(menuIntent);//go to login activity
        }

    }

    public void login(View view) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = Coffee_API_URLs.LOGIN;

        prog1.setVisibility(View.VISIBLE);
        login.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(loginActivity.this, response, Toast.LENGTH_LONG).show();
                prog1.setVisibility(View.INVISIBLE);
                login.setEnabled(true);
                System.out.println(response.trim()=="error");
                System.out.println(response.trim().compareTo("error"));
                if(response.trim().compareTo("error")!=0){
                   // Intent i= new Intent(loginActivity.this, MenuActivity.class);

                    //if there is no errors it will takes us to the getCustomer method which retrieve all the customer's info and will redirect us to the 'MenuActivity'
                    getCustomer(username.getText().toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginActivity.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                prog1.setVisibility(View.INVISIBLE);
                login.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",username.getText().toString());
                params.put("password", password.getText().toString());
                //params.put("key", "cuBubcDE");
                return params;
            }
        };

        queue.add(request);
    }

    public void getCustomer(String username){
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = Coffee_API_URLs.GETCUSTOMER + "?username=" + username;
        Customer c = new Customer();
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject row = response.getJSONObject(0);
                    String username = row.getString("username");
                    String name = row.getString("name");
                    String fname = row.getString("fname");
                    LocalDate dob = LocalDate.parse(row.getString("date_of_birth"));
                    String email = row.getString("email");
                    c.setUsername(username);
                    c.setName(name);
                    c.setFname(fname);
                    c.setDateOfBirth(dob);
                    c.setEmail(email);
                    Toast.makeText(loginActivity.this, c.toString(), Toast.LENGTH_LONG).show();
                    System.out.println("Hellloooo " +c.toString());
                    menuIntent.putExtra("customer",c);
                    startActivity(menuIntent);

                } catch (Exception ex) {
                    Toast.makeText(loginActivity.this, "No records found", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }




}//loginActivity