package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login, createAcc;
    public ProgressBar prog1 ;
    Intent menuIntent ;

    //For Session management
    public static final String SHARED_PREFS="FD_prefs";
    public static final String USERNAME_KEY="username_key";
    public static final String PASSWORD_KEY = "password_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String userSess, passSess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // in shared prefs inside the string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
         userSess= sharedpreferences.getString(USERNAME_KEY, null);
         passSess= sharedpreferences.getString(PASSWORD_KEY, null);



        username=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);
        login=findViewById(R.id.btLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                    Toast.makeText(loginActivity.this, "Please check if username or password are empty.", Toast.LENGTH_LONG).show();
                else
                login(v);
            }
        });

        prog1 = findViewById(R.id.prog1);
        menuIntent= new Intent(loginActivity.this, MenuActivity.class);

        createAcc=findViewById(R.id.btCreateAccount);
        createAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, Create_Account.class));
            }
        });


    }

    //If the user is already logged in and his credentials are stored
    @Override
    protected void onStart() {
        super.onStart();
        if (userSess != null && passSess != null) {
            Intent i = new Intent(loginActivity.this, MenuActivity.class);
            startActivity(i);
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
                    //On success we add the username and password to be in our session
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    // below two lines will put values for
                    // email and password in shared preferences.
                    editor.putString(USERNAME_KEY, username.getText().toString());
                    editor.putString(PASSWORD_KEY, password.getText().toString());

                    // to save our data with key and value.
                    editor.apply();

                    //if there is no errors it will takes us to the getCustomer method which retrieve all the customer's info and will redirect us to the 'MenuActivity'
                    getCustomer(username.getText().toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginActivity.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Fail: "+ error.toString());

                prog1.setVisibility(View.INVISIBLE);
                login.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",username.getText().toString());
                params.put("password", password.getText().toString().trim());
                //params.put("key", "cuBubcDE");
                return params;
            }
        };

       // request.setRetryPolicy(new DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



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
                   // System.out.println("Hellloooo " +c.toString());
                   // menuIntent.putExtra("customer",c);
                    Customer.CUSTOMERNAME=c.getName();
                    Customer.USERNAME=c.getUsername();

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


    public void createAccount(View view) {
    }

}//loginActivity