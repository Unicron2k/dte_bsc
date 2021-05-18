package com.example.thelastdeath.entity.helper.userstats;

public class CurrentYear {
    private long sessionCount;
    private long timeSpent;

    public CurrentYear() {
    }

    public CurrentYear(long sessionCount, long timeSpent) {
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
