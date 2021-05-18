package com.example.thelastdeath.entity;

import java.io.Serializable;

public class UserProgramSession implements Serializable {
    private long id;
    private String rid;
    private String api_key;
    private String date;
    private int time_spent;
    private long user_program_id;
    private String extra_json_data;
    private String description;

    public UserProgramSession() {
    }

    public UserProgramSession(long id, String rid, String api_key, String date, int time_spent, long user_program_id, String extra_json_data, String description) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.date = date;
        this.time_spent = time_spent;
        this.user_program_id = user_program_id;
        this.extra_json_data = extra_json_data;
        this.description = description;
    }

    public UserProgramSession(String date, int time_spent, long user_program_id, String extra_json_data, String description) {
        this.date = date;
        this.time_spent = time_spent;
        this.user_program_id = user_program_id;
        this.extra_json_data = extra_json_data;
        this.description = description;
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
    public String getDate() {
        return date;
    }
    public int getTime_spent() {
        return time_spent;
    }
    public long getUser_program_id() {
        return user_program_id;
    }
    public String getExtra_json_data() {
        return extra_json_data;
    }
    public String getDescription() {
        return description;
    }
}
