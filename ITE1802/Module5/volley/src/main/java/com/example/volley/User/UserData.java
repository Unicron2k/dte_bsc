package com.example.volley.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    private List<User> allUsers = new ArrayList<>();

    public UserData(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public UserData(String allAlbumsAsJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        this.allUsers = gson.fromJson(allAlbumsAsJson, type);
    }

    public UserData(JSONObject jsonObject) {

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M.d.yy hh:mm a");
        gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        this.allUsers = gson.fromJson(jsonObject.toString(), type);
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public void addUser(User user) {
        allUsers.add(user);
    }

    @Override
    public String toString() {

        if (allUsers != null) {
            StringBuilder stringBuffer = new StringBuilder();
            for ( User user : allUsers) {
                stringBuffer.append(user.toString());
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } else {
            return "...";
        }
    }
}
