package com.example.thelastdeath.entity.helper.userstats;

public class CurrentWeek {
    private long sessionCount;
    private long timeSpent;

    public CurrentWeek() {
    }

    public CurrentWeek(long sessionCount, long timeSpent) {
        this.sessionCount = sessionCount;
        this.timeSpent = timeSpent;
    }

    /* Getters */
    public long getSessionCount() {
        return sessionCount;
    }
    public long getTimeSpent() {
        return timeSpent;
    }
}
