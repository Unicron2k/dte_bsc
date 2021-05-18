package com.shadyshrimp.lab9_2.exercise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExerciseData {
    private List<Exercise> allExercises;

    public ExerciseData(List<Exercise> allExercises) {
        this.allExercises = allExercises;
    }

    public ExerciseData(String allExercisesAsJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Exercise>>(){}.getType();
        this.allExercises = gson.fromJson(allExercisesAsJson, type);
    }

    public ExerciseData(JSONObject jsonObject) {

        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M.d.yy hh:mm a");
        gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Exercise>>(){}.getType();
        this.allExercises = gson.fromJson(jsonObject.toString(), type);
    }

    public List<Exercise> getAllExercises() {
        return allExercises;
    }

    public void setAllExercises(List<Exercise> allExercises) {
        this.allExercises = allExercises;
    }

    public void addExercise(Exercise exercise) {
        allExercises.add(exercise);
    }

    @Override
    public String toString() {

        if (allExercises!=null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Exercise exercise : allExercises) {
                stringBuffer.append(exercise.toString());
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } else {
            return "...";
        }
    }
}
