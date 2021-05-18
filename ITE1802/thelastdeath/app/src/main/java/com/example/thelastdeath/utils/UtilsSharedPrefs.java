package com.example.thelastdeath.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

/**
 * TODO LIST:
 *   Add more values for preferences if needed as keys
 *  - Implement:
 *      - (DONE) Basic functionality
 *  - Fix:
 *      -
 */
public class UtilsSharedPrefs {
    private static final String SHARED_PREF_FILENAME = "_SHARED_PREFERENCE_FILE";
    private static final String KEY_BOOLEAN_EXAMPLE = "KEY_BOOLEAN_EXAMPLE";
    private static final String KEY_STRING = "KEY_STRING";

    /**
     * Sets exampleBoolean in SharedPreference-file
     */
    public static void setExampleBoolean(boolean boolValue, Context context) {
        getSharedPreferences(context).edit().putBoolean(KEY_BOOLEAN_EXAMPLE, boolValue).apply();
    }

    /**
     * Returns exampleBoolean from SharedPreference-file
     */
    public static boolean getExampleBoolean(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_BOOLEAN_EXAMPLE, false);
    }

    /**
     * Sets exampleString in SharedPreference-file
     */
    public static void setExampleString(Context context, String exampleString) {
        getSharedPreferences(context).edit().putString(KEY_STRING, exampleString).apply();
    }

    /**
     * Returns exampleString from SharedPreference-file
     */
    public static String getExampleString(Context context) {
        return getSharedPreferences(context).getString(KEY_STRING, "");
    }









    /**
     * Returns SharedPreference for current package
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Objects.requireNonNull(context.getClass().getPackage()).getName() + SHARED_PREF_FILENAME, Context.MODE_PRIVATE);
    }
}
