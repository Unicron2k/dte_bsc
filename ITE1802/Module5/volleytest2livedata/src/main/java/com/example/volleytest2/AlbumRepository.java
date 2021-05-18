package com.example.volleytest2;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


    public void download(Context context) {

        queue = Volley.newRequestQueue(context);

        // Laster ned en LISTE med JSON-objekter:
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrlString,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try{
                            Gson gson = new Gson();

                            // Loop gjennom returnert JSON-array:
                            ArrayList<Album> tmpList = new ArrayList<>();
                            for(int i=0; i < response.length(); i++){
                                JSONObject albumJson = response.getJSONObject(i);
                                Album album = gson.fromJson(albumJson.toString(), Album.class);
                                tmpList.add(album);
                            }

                            AlbumData tmpAlbumData = new AlbumData(tmpList);
                            albumData.postValue(tmpAlbumData);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        errorMessage.postValue(error.getMessage());
                    }
                })
                {
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
