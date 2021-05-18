package com.example.volley.Album;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volley.MySingletonQueue;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumRepository {

    private MutableLiveData<AlbumData> albumData = new MutableLiveData<>(new AlbumData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "https://jsonplaceholder.typicode.com/albums/";
    private RequestQueue queue;
    private StringRequest stringRequest;

    public AlbumRepository() {
        //
    }

    public MutableLiveData<AlbumData> getAlbumData() {
        return albumData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void downloadAlbum(Context context, long userId){
        mUrlString = "https://jsonplaceholder.typicode.com/albums?userId=" + userId;
        downloadAlbum(context);
    }

    public void downloadAlbum(Context context) {

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
                            ArrayList<Album> tmpList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject albumJson = response.getJSONObject(i);
                                Album album = gson.fromJson(albumJson.toString(), Album.class);
                                tmpList.add(album);
                            }
                            AlbumData tmpAlbumData = new AlbumData(tmpList);
                            albumData.postValue(tmpAlbumData);
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
