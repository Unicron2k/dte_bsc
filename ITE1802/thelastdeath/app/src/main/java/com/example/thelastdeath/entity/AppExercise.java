package com.example.thelastdeath.entity;

import java.io.Serializable;

public class AppExercise implements Serializable {
    private long id;
    private String rid;
    private String api_key;
    private String name;
    private String description;
    private String icon;
    private String infobox_color;

    public AppExercise() {
    }

    public AppExercise(long id, String rid, String api_key, String name, String description, String icon, String infobox_color) {
        this.id = id;
        this.rid = rid;
        this.api_key = api_key;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.infobox_color = infobox_color;
    }

    public AppExercise(String name, String description, String icon, String infobox_color) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.infobox_color = infobox_color;
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
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getIcon() {
        return icon;
    }
    public String getInfobox_color() {
        return infobox_color;
    }
}