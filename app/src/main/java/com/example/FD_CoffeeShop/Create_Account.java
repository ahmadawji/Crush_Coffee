package com.example.FD_CoffeeShop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Create_Account extends AppCompatActivity {


    // two buttons
    Button bCancel, bProceed;

    // four text fields
    EditText etFirstName, etLastName, etEmail, etPassword, etRepass;

    // one boolean variable to check whether all the text fields
    // are filled by the user, properly or not.
    boolean isAllFieldsChecked = false;

    //for date picker
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    //variables to be sent for api
    String fname, lname, dob="", email, password;

    //progress bar
    public ProgressBar prog1 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        // register buttons with their proper IDs.
        bProceed = findViewById(R.id.btProceedButton);
        bCancel = findViewById(R.id.btCancelButton);

        //progress bar
        prog1 = findViewById(R.id.prog1);

        // register all the EditText fields with their IDs.
        etFirstName = findViewById(R.id.etFirstname);
        etLastName = findViewById(R.id.etLastname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRepass= findViewById(R.id.etRePassword);

        //For the date picker
        dateView = (TextView) findViewById(R.id.tvDateView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        // handle the PROCEED button
        bProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store the returned value of the dedicated function which checks
                // whether the entered data is valid or if any fields are left blank.
                isAllFieldsChecked = CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {
                    fname=etFirstName.getText().toString();
                    lname=etLastName.getText().toString();
                    email=etEmail.getText().toString();
                    password=etPassword.getText().toString();
                    createUser();
                }
            }
        });

        // if user presses the cancel button then close the
        // application or the particular activity.
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Account.this.finish();
                startActivity(new Intent(Create_Account.this, loginActivity.class));
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        dob= new StringBuilder().append(year).append("-").append(month).append("-").append(day).toString();
        Toast.makeText(this, "dob: "+dob, Toast.LENGTH_LONG).show();

    }


    // function which checks all the text fields
    // are filled or not by the user.
    // when user clicks on the PROCEED button
    // this function is triggered.
    private boolean CheckAllFields() {
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (etFirstName.length() == 0) {
            etFirstName.setError("This field is required");
            return false;
        }

        if (etLastName.length() == 0) {
            etLastName.setError("This field is required");
            return false;
        }

        if (etEmail.length() == 0) {
            etEmail.setError("Email is required");
            return false;
        }else if(!etEmail.getText().toString().trim().matches(emailPattern)){
            etEmail.setError("Invalid email address!");
            return false;
        }


        if(etPassword.getText().toString().compareTo(etRepass.getText().toString())==1){
            etRepass.setError("Passwords must be identical!");
            return false;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("Password is required");
            return false;
        } else if (etPassword.length() < 8) {
            etPassword.setError("Password must be minimum 8 characters");
            return false;
        }

        if (dob.isEmpty()){
            Toast.makeText(this, "Please add your date of birth!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // after all validation return true.
        return true;
    }

    public void createUser(){
        final RequestQueue queue = Volley.newRequestQueue(Create_Account.this);
        String url = Coffee_API_URLs.ADDUSER;

        prog1.setVisibility(View.VISIBLE);
        bProceed.setEnabled(false);
        //Toast.makeText(this, "fname: "+fname, Toast.LENGTH_SHORT).show();

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("fname",fname);
            object.put("lname",lname);
            object.put("dob",dob);
            object.put("email",email);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Create_Account.this, response.toString(), Toast.LENGTH_LONG).show();
                prog1.setVisibility(View.INVISIBLE);
                bProceed.setEnabled(true);
                try{
                    if (response.toString().compareTo("{\"success\":\"user created successfuly\"}") == 0){
                        Toast.makeText(Create_Account.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Create_Account.this, Create_Account_Success.class);
                        Toast.makeText(Create_Account.this, "Hello", Toast.LENGTH_LONG).show();
                        startActivity(i);

                    }else{
                        Toast.makeText(Create_Account.this, "Email is already taken!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex){
                    Toast.makeText(Create_Account.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Create_Account.this,"Fail: "+ error.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Fail: "+ error.toString());

                prog1.setVisibility(View.INVISIBLE);
                bProceed.setEnabled(true);
            }
        });

        queue.add(request);
    }


}