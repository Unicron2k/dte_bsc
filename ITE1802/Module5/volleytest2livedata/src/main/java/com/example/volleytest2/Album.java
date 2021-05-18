package com.example.volleytest2;

public class Album {
    private long id=-1;
    private long userId=-1;
    private String title="";

    public Album(long id, long userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return id + " " + title;
    }
}
