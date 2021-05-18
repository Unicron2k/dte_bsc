package com.example.thelastdeath.entity.helper;

import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.AppProgramType;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.UserProgram;
import com.example.thelastdeath.entity.UserProgramExercise;
import com.example.thelastdeath.entity.UserProgramSession;
import com.example.thelastdeath.entity.UserStats;

import java.util.List;

public class ApiResponse {
    private boolean result;
    private String message;
    private int httpStatusCode;
    private User user;
    private UserStats userStats;
    private List<AppProgramType> allAppProgramTypes;
    private AppProgramType appProgramType;
    private List<AppExercise> allAppAxercises;
    private AppExercise appExercise;
    private UserProgram userProgram;
    private UserProgramSession userProgramSession;
    private UserProgramExercise userProgramExercise;
    private IconFileNames iconFileNames;

    public ApiResponse() {
        result=true;
        message="";
        user = null;
        httpStatusCode = -1;
    }

    /**
     * /user/
     */
    public ApiResponse(boolean result, String message, User user, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.user = user;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * /user_stats/
     */
    public ApiResponse(boolean result, String message, UserStats userStats, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.userStats = userStats;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * all /app_program_types/
     */
    public ApiResponse(boolean result, String message, List<AppProgramType> allAppProgramTypes, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.allAppProgramTypes = allAppProgramTypes;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * specific rid /app_program_types/
     */
    public ApiResponse(boolean result, String message, AppProgramType appProgramType, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.appProgramType = appProgramType;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * all /app_exercises/
     *      added junkparam to avoid overloading issues due to JVM erasure'
     */
    public ApiResponse(boolean result, String message, List<AppExercise> appExercises, int httpStatusCode, String junkParam) {
        this.result = result;
        this.message = message;
        this.allAppAxercises = appExercises;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * specific rid /app_program_types/
     */
    public ApiResponse(boolean result, String message, AppExercise appExercise, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.appExercise = appExercise;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponse(boolean result, String message, UserProgram userProgram, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.userProgram = userProgram;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponse(boolean result, String message, UserProgramSession userProgramSession, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.userProgramSession = userProgramSession;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponse(boolean result, String message, UserProgramExercise userProgramExercise, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.userProgramExercise = userProgramExercise;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponse(boolean result, String message, IconFileNames iconFileNames, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.iconFileNames = iconFileNames;
        this.httpStatusCode = httpStatusCode;
    }


    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserStats getUserStats() {
        return userStats;
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }

    public List<AppProgramType> getAllAppProgramTypes() {
        return allAppProgramTypes;
    }

    public AppProgramType getAppProgramType() {
        return appProgramType;
    }

    public List<AppExercise> getAllAppAxercises() {
        return allAppAxercises;
    }

    public AppExercise getAppExercise() {
        return appExercise;
    }

    public UserProgram getUserProgram() {
        return userProgram;
    }

    public UserProgramSession getUserProgramSession() {
        return userProgramSession;
    }

    public UserProgramExercise getUserProgramExercise() {
        return userProgramExercise;
    }

    public IconFileNames getIconFileNames() {
        return iconFileNames;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
