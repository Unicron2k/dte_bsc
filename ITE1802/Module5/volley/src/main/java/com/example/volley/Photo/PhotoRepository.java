package com.example.volley.Photo;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volley.MySingletonQueue;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotoRepository {

    private MutableLiveData<PhotoData> photoData = new MutableLiveData<>(new PhotoData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "https://jsonplaceholder.typicode.com/photos/";
    private RequestQueue queue;
    private StringRequest stringRequest;

    public PhotoRepository() {
        //
    }

    public MutableLiveData<PhotoData> getPhotoData() {
        return photoData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void downloadPhoto(Context context, long albumId){
        mUrlString = "https://jsonplaceholder.typicode.com/photos?albumId=" + albumId;
        downloadPhoto(context);
    }
    public void downloadPhoto(Context context) {

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
                            ArrayList<Photo> tmpList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject photoJson = response.getJSONObject(i);
                                Photo photo = gson.fromJson(photoJson.toString(), Photo.class);
                                tmpList.add(photo);
                            }
                            PhotoData tmpPhotoData = new PhotoData(tmpList);
                            photoData.postValue(tmpPhotoData);
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }
}
