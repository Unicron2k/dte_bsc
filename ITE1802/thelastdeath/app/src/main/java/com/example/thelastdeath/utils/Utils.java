package com.example.thelastdeath.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;

import java.util.ArrayList;

public class Utils {
    private static final String DEBUG_LOGGER_TAG = "Testing";

    /**
     * Displays a Toast on given Activity
     */
    public static void displayToast(Activity activity, String output) {
        Toast.makeText(activity, output, Toast.LENGTH_SHORT).show();
        appendLogger("Toast_shown->" + output);
    }


    /**
     * Set clipboard on device
     */
    public static void setToClipboard(Activity activity, String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Returns False if any EditText' empty
     */
    public static boolean isEditTextFieldsNotEmpty(EditText[] editTextArray, Activity activity) {
        for (EditText editText : editTextArray)
            if (editText.getText().toString().equals("")) {
                displayToast(activity, activity.getResources().getString(R.string.warning_edittext_empty));
                return false;
            }
        return true;
    }

    /**
     * Empty EditText fields
     */
    public static void emptyEditTextFields(EditText[] editTextArray) {
        for (EditText editText : editTextArray)
            editText.setText("");
    }

    /**
     * Sets visibility for Button[]
     */
    public static void setButtonVisibility(Button[] buttonArray, boolean visible) {
        for (Button button : buttonArray) {
            button.setClickable(visible);
            button.setAlpha(visible ? 1 : .5f);
        }
    }

    /**
     * Sets visibility for EditText[]
     */
    public static void setEditTextVisibility(EditText[] editTextArray, boolean visible) {
        for (EditText editText : editTextArray) {
            editText.setEnabled(visible);
            editText.setAlpha(visible ? 1 : .5f);
        }
    }

    /**
     * Sets adapter on ArrayList<String> to ListView with simple_list_item_1
     */
    public static void setAdapterToListView(Activity activity, ArrayList<String> arrayList, ListView listView) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    /**
     * Return default animations as navOptionsBuilder.build()
     */
    public static NavOptions getAnimation() {
        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder();
        navOptionsBuilder.setEnterAnim(R.anim.slide_in_right);
        navOptionsBuilder.setExitAnim(R.anim.slide_out_left);
        navOptionsBuilder.setPopEnterAnim(R.anim.slide_in_left);
        navOptionsBuilder.setPopExitAnim(R.anim.slide_out_right);
        return navOptionsBuilder.build();
    }

    public static String convertFileNameIcon8(String fileName) {
        if (fileName != null) {
            fileName = fileName.replaceAll("icons8-", "");
            fileName = fileName.replaceAll("_", " ");
        }
        return fileName;
    }

    /**
     * Append to Logcat with static Tag
     */
    public static void appendLogger(String msg) {
        Log.d(Utils.DEBUG_LOGGER_TAG, msg);
    }

    public static void setAppTheme(SharedPreferences sharedPreferences, Context context) {
        // Check which mode to set
        String pref = sharedPreferences.getString(context.getString(R.string.pref_key_dark_mode), "");
        String[] prefDarkModeValues = context.getResources().getStringArray(R.array.pref_dark_mode_values);
        if (pref.equals(prefDarkModeValues[0])) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (pref.equals(prefDarkModeValues[1])) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (pref.equals(prefDarkModeValues[2])) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (pref.equals(prefDarkModeValues[3])) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
        }
    }
}