package com.example.thelastdeath.entity;

import java.io.Serializable;

public class AppProgramType implements Serializable {
    private long id;
    private String rid;
    private String api_key;
    private String description;
    private String icon;
    private String back_color;

    public AppProgramType() {
    }

    public AppProgramType(long id, String rid, String api_key, String description, String icon, String back_color) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.description = description;
        this.icon = icon;
        this.back_color = back_color;
    }

    public AppProgramType(String description, String icon, String back_color) {
        this.description = description;
        this.icon = icon;
        this.back_color = back_color;
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
    public String getDescription() {
        return description;
    }
    public String getIcon() {
        return icon;
    }
    public String getBack_color() {
        return back_color;
    }
}