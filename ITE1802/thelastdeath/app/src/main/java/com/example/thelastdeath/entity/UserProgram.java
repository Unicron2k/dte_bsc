package com.example.thelastdeath.entity;

import java.io.Serializable;
import java.util.List;

public class UserProgram implements Serializable {
    private long id;
    private String rid;
    private String api_key;
    private long user_id;
    private long app_program_type_id;
    private String name;
    private String description;
    private int use_timing;

    private List<UserProgramExercise> user_program_exercises;
    private List<UserProgramSession> user_program_sessions;
    private AppProgramType app_program_type;

    public UserProgram() {
    }

    public UserProgram(long id, String rid, String api_key, long user_id, long app_program_type_id, String name, String description, int use_timing) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.user_id = user_id;
        this.app_program_type_id = app_program_type_id;
        this.name = name;
        this.description = description;
        this.use_timing = use_timing;
    }

    public UserProgram(long id, String rid, String api_key, long user_id, long app_program_type_id, String name, String description, int use_timing, List<UserProgramExercise> user_program_exercises, List<UserProgramSession> user_program_sessions, AppProgramType app_program_type) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.user_id = user_id;
        this.app_program_type_id = app_program_type_id;
        this.name = name;
        this.description = description;
        this.use_timing = use_timing;
        this.user_program_exercises = user_program_exercises;
        this.user_program_sessions = user_program_sessions;
        this.app_program_type = app_program_type;
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
    public long getUser_id() {
        return user_id;
    }
    public long getApp_program_type_id() {
        return app_program_type_id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getUse_timing() {
        return use_timing;
    }
    public List<UserProgramExercise> getUser_program_exercises() {
        return user_program_exercises;
    }
    public List<UserProgramSession> getUser_program_sessions() {
        return user_program_sessions;
    }
    public AppProgramType getApp_program_type() {
        return app_program_type;
    }
}