package com.example.thelastdeath.entity.helper.userstats;

public class Last7Days {
    private long sessionCount;
    private long timeSpent;

    public Last7Days() {
    }

    public Last7Days(long sessionCount, long timeSpent) {
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
