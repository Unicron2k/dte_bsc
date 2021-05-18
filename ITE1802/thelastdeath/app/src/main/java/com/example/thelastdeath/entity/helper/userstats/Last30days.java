package com.example.thelastdeath.entity.helper.userstats;

public class Last30days {
    private long sessionCount;
    private long timeSpent;

    public Last30days() {
    }

    public Last30days(long sessionCount, long timeSpent) {
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
