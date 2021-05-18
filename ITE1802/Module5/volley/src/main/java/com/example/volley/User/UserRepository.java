package com.example.volley.User;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volley.MySingletonQueue;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserRepository {


    private MutableLiveData<UserData> userData = new MutableLiveData<>(new UserData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "https://jsonplaceholder.typicode.com/users/";
    private RequestQueue queue;
    private StringRequest stringRequest;

    public UserRepository() {
        //
    }

    public MutableLiveData<UserData> getUserData() {
        return userData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }


    public void downloadUsers(Context context) {
        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        // Laster ned en LISTE med JSON-objekter:
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrlString,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Gson gson = new Gson();
                            // Loop gjennom returnert JSON-array:
                            ArrayList<User> tmpList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userAsJson = response.getJSONObject(i);
                                User user = gson.fromJson(userAsJson.toString(), User.class);
                                tmpList.add(user);
                            }

                            UserData tmpUserData = new UserData(tmpList);
                            userData.postValue(tmpUserData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorMessage.postValue(error.getMessage());
                    }
                });
        queue.add(jsonArrayRequest);
    }
}
