package com.shadyshrimp.quizztime;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class QuizURLGenerator {
    public static String generate(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://opentdb.com/api.php?amount=");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String questions = sharedPreferences.getString("questions", "10");
        sb.append(questions);

        if (sharedPreferences.contains("category") && !sharedPreferences.getString("category", "").equals("ANY")) {
            sb.append("&category=");
            sb.append(sharedPreferences.getString("category", ""));
        }

        if (sharedPreferences.contains("difficulty") && !sharedPreferences.getString("difficulty", "").equals("ANY")) {
            sb.append("&difficulty=");
            sb.append(sharedPreferences.getString("difficulty", ""));
        }

        if (sharedPreferences.contains("type") && !sharedPreferences.getString("type", "").equals("ANY")) {
            sb.append("&type=");
            sb.append(sharedPreferences.getString("type", ""));
        }

        return sb.toString();
    }
}