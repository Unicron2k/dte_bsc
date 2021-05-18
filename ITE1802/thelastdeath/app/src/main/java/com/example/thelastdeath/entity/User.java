package com.example.thelastdeath.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private long id;
    private String firebase_id;
    private String api_key;
    private String phone;
    private String email;
    private String name;
    private int birth_year;
    private List<UserProgram> user_programs;

    public User() {
    }

    public User(long id, String firebase_id, String api_key, String phone, String email, String name, int birth_year) {
        this.id = id;
        this.firebase_id = firebase_id;
        this.api_key = api_key;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.birth_year = birth_year;
    }


     public User(long id, String firebase_id, String api_key, String phone, String email, String name, int birth_year, List<UserProgram> user_programs) {
     this.id = id;
     this.firebase_id = firebase_id;
     this.api_key = api_key;
     this.phone = phone;
     this.email = email;
     this.name = name;
     this.birth_year = birth_year;
     this.user_programs = user_programs;
     }


    public User(String phone, String email, String name, int birth_year) {
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.birth_year = birth_year;
    }


    /* Getters */
    public long getId() {
        return id;
    }
    public String getFirebase_id() {
        return firebase_id;
    }
    public String getApi_key() {
        return api_key;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public int getBirth_year() {
        return birth_year;
    }
    public List<UserProgram> getUser_programs() {
        return user_programs;
    }


    @NonNull
    public String toString() {
        return
                "id=" + id +
                "| firebase_id=" + firebase_id +
                "| api_key=" + api_key +
                "| phone=" + phone +
                "| birth_year=" + birth_year;
    }
}