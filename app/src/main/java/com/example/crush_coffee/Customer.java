package com.example.crush_coffee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Customer implements Serializable {
    private String username;
    private String name;
    private String fname;
    private String password;
    private LocalDate dateOfBirth;
    private String email;

    //global variables
    public static Boolean loggedIn=false;
    public static String USERNAME="";
    public static String CUSTOMERNAME="";


    public Customer() { }

    public Customer(String username, String name, String fname, String password, LocalDate dateOfBirth, String email) {
        this.username = username;
        this.name = name;
        this.fname = fname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                '}';
    }
}
