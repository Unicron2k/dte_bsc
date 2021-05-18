package com.example.volleytest1livedata;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TextRepository {

    private MutableLiveData<TextData> textData = new MutableLiveData<>(new TextData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "http://kark.uit.no:8088/d3330log_backend/getTestMessage";
    private RequestQueue queue;
    private StringRequest stringRequest;
    private ImageRequest imageRequest;

    public TextRepository() {
        //
    }

    public MutableLiveData<TextData> getTextData() {
        return textData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void download(Context context) {
        try {
            queue = Volley.newRequestQueue(context);
            // GET String
            stringRequest = new StringRequest(Request.Method.GET, mUrlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        textData.postValue(new TextData(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorMessage.postValue(error.getMessage());
                    }
                });

            queue.add(stringRequest);
        } catch (Error error) {
            errorMessage = new MutableLiveData<>(error.getMessage());
        }
    }
}
