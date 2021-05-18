package com.example.thelastdeath.entity;

import com.example.thelastdeath.entity.helper.userstats.CurrentMonth;
import com.example.thelastdeath.entity.helper.userstats.CurrentWeek;
import com.example.thelastdeath.entity.helper.userstats.CurrentYear;
import com.example.thelastdeath.entity.helper.userstats.Last30days;
import com.example.thelastdeath.entity.helper.userstats.Last7Days;

public class UserStats {
    private long id;
    private String firebase_id;
    private String api_key;
    private Last7Days last7Days;
    private CurrentWeek currentWeek;
    private CurrentMonth currentMonth;
    private Last30days last30days;
    private CurrentYear currentYear;

    public UserStats() {
    }

    public UserStats(long id, String firebase_id, String api_key, Last7Days last7Days, CurrentWeek currentWeek, CurrentMonth currentMonth, Last30days last30days, CurrentYear currentYear) {
        this.id = id;
        this.firebase_id = firebase_id;
        this.api_key = api_key;
        this.last7Days = last7Days;
        this.currentWeek = currentWeek;
        this.currentMonth = currentMonth;
        this.last30days = last30days;
        this.currentYear = currentYear;
    }

    /* Getters */
    public long getId() {
        return id;
    }
    public String getFirebase_id() {
        return firebase_id;
    }
    public String getApi_key() {
        return api_key;
    }
    public Last7Days getLast7Days() {
        return last7Days;
    }
    public CurrentWeek getCurrentWeek() {
        return currentWeek;
    }
    public CurrentMonth getCurrentMonth() {
        return currentMonth;
    }
    public Last30days getLast30days() {
        return last30days;
    }
    public CurrentYear getCurrentYear() {
        return currentYear;
    }
}