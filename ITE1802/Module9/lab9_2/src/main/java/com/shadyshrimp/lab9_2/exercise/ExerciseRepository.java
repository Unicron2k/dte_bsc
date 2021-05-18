package com.shadyshrimp.lab9_2.exercise;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.shadyshrimp.lab9_2.SingletonQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExerciseRepository {


    private MutableLiveData<ExerciseData> programData = new MutableLiveData<>(new ExerciseData(""));
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private String mUrlString = "http://tusk.systems/trainingapp/v2/api.php/programs/?_expand_children=true";
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
        mUrlString="http://tusk.systems/trainingapp/v2/api.php/programs/"+programId+"?_expand_children=true";
        downloadExercises(context);
    }

    public void downloadExercises(Context context) {

        queue = SingletonQueue.getInstance(context).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                mUrlString,
                null,
                jsonObject -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Exercise> tmpList = new ArrayList<>();
                        JSONArray program_exercises = jsonObject.getJSONArray("exercises");
                        for (int i = 0; i < program_exercises.length(); i++) {
                            JSONObject programExerciseAsJson = program_exercises.getJSONObject(i);
                            JSONObject exerciseAsJsonObject = programExerciseAsJson.getJSONObject("exercise");
                            Exercise exercise = gson.fromJson(exerciseAsJsonObject.toString(), Exercise.class);
                            tmpList.add(exercise);
                        }
                    ExerciseData tmpExerciseData = new ExerciseData(tmpList);
                    programData.postValue(tmpExerciseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },error -> errorMessage.postValue(error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}