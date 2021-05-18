package com.example.thelastdeath.entity;

import java.io.Serializable;
import java.util.List;

public class UserProgramExercise implements Serializable {
    private long id;
    private String rid;
    private String api_key;
    private long user_program_id;
    private long app_exercise_id;
    private AppExercise app_exercise;


    public UserProgramExercise() {
    }

    public UserProgramExercise(long id, String rid, String api_key, long user_program_id, long app_exercise_id, AppExercise app_exercise) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.user_program_id = user_program_id;
        this.app_exercise_id = app_exercise_id;
        this.app_exercise = app_exercise;
    }

    /* Getters */
    public long getId() {
        return id;
    }
    public String getRid() {
        return rid;
    }
    public String getApi_key() {
        return api_key;
    }
    public long getUser_program_id() {
        return user_program_id;
    }
    public long getApp_exercise_id() {
        return app_exercise_id;
    }
    public AppExercise getApp_exercise() {
        return app_exercise;
    }
}