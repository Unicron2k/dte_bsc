package com.shadyshrimp.karakter1.Exercise;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shadyshrimp.karakter1.SingletonQueue;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExerciseRepository {


    private MutableLiveData<ExerciseData> programData = new MutableLiveData<>(new ExerciseData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "http://tusk.systems/trainingapp/v0/api.php/program_exercises?program_id=";
    private RequestQueue queue;

    public ExerciseRepository() {
        //
    }

    public MutableLiveData<ExerciseData> getExerciseData() {
        return programData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void downloadExercises(Context context, long programId){
        mUrlString="http://tusk.systems/trainingapp/v0/api.php/program_exercises?program_id="+programId;
        downloadExercises(context);
    }

    public void downloadExercises(Context context) {

        queue = SingletonQueue.getInstance(context).getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrlString,
                null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Exercise> tmpList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject exerciseJson = response.getJSONObject(i);
                            Exercise exercise = gson.fromJson(exerciseJson.toString(), Exercise.class);
                            tmpList.add(exercise);
                        }
                        ExerciseData tmpExerciseData = new ExerciseData(tmpList);
                        programData.postValue(tmpExerciseData);
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