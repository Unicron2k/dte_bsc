package com.shadyshrimp.karakter1.Program;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shadyshrimp.karakter1.SingletonQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgramRepository {


    private MutableLiveData<ProgramData> programData = new MutableLiveData<>(new ProgramData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "http://tusk.systems/trainingapp/v0/api.php/programs";
    private RequestQueue queue;

    public ProgramRepository() {
        //
    }

    public MutableLiveData<ProgramData> getProgramData() {
        return programData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void downloadPrograms(Context context) {

        queue = SingletonQueue.getInstance(context).getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrlString,
                null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Program> tmpList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject programJson = response.getJSONObject(i);
                            Program program = gson.fromJson(programJson.toString(), Program.class);
                            tmpList.add(program);
                        }
                        ProgramData tmpProgramData = new ProgramData(tmpList);
                        programData.postValue(tmpProgramData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> errorMessage.postValue(error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }
}