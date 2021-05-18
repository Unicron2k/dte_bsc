package com.example.thelastdeath.entity.helper;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

/**
 * TODO LIST:
 *  - Check:
 *  - Fix:
 *      -
 */
public class MyJsonArrayRequest extends JsonArrayRequest {
    private int httpStatusCode = -1;

    public MyJsonArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

//    public MyJsonArrayRequest(String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
//        super(url, jsonRequest, listener, errorListener);
//    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        httpStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
