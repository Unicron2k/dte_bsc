package com.example.thelastdeath.entity.helper;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class VolleyErrorParser {
    public static ApiError parse(VolleyError error) {

        if (error == null || error.networkResponse == null) {
            return null;
        }

        ApiError apiError = new ApiError();

        String jsonBody;
        final String statusCode = String.valueOf(error.networkResponse.statusCode);
        //get response body and parse with appropriate encoding
        try {
            jsonBody = new String(error.networkResponse.data,"UTF-8");
            JSONObject bodyObject = new JSONObject(jsonBody);
            String message = bodyObject.getString("message");
            apiError.setCode(Integer.parseInt(statusCode));
            apiError.setMessage(message);

            return apiError;
        } catch (JSONException e) {
            return null;
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
