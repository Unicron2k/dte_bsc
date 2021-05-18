package com.example.thelastdeath.entity.helper;

import androidx.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * MyJsonObjectRequest arver fra JsonObjectRequest slik at vi kan overstyre parseNetworkResponse() for å kunne hente ut HTTP responsekode.
 * Se bruk i DataRepository
 */
public class MyJsonObjectRequest extends JsonObjectRequest {
    private int httpStatusCode = -1;

    public MyJsonObjectRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public MyJsonObjectRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        httpStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
