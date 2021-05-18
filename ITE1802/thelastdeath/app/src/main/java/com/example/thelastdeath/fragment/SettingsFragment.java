package com.example.thelastdeath.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.preference.*;
import com.example.thelastdeath.R;
import com.example.thelastdeath.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static com.example.thelastdeath.utils.Utils.setAppTheme;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    Preference pFeedback;
    Preference pDeleteData;
    Preference pAbout;
    Preference.OnPreferenceClickListener preferenceClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Use this to fetch a preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext());
        String name = sharedPreferences.getString(getString(R.string.pref_key_dark_mode), "");
        // Show the selected preference
        //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
        */

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey);

        pFeedback = findPreference(getString(R.string.pref_key_feedback));
        pDeleteData = findPreference(getString(R.string.pref_key_delete_data));
        pAbout = findPreference(getString(R.string.pref_key_about));

        preferenceClickListener = preference -> {
            if(preference.getKey().equals(getString(R.string.pref_key_feedback))){
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.feedbackFragment, null, Utils.getAnimation());
            } else if (preference.getKey().equals(getString(R.string.pref_key_delete_data))) {
               if (((ActivityManager) Objects.requireNonNull(Objects.requireNonNull(this.getContext()).getSystemService(Context.ACTIVITY_SERVICE))).clearApplicationUserData()) {
                    Log.i("INFO: ","Data has been 'Deleted'! *laughs in Google*");
                } else {
                   Log.i("INFO: ","Could not delete data!");
                }
            } else if(preference.getKey().equals(getString(R.string.pref_key_about))){
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.aboutFragment, null, Utils.getAnimation());
            }
            //We don't want to update anything now, so we return false
            return false;
        };

        pFeedback.setOnPreferenceClickListener(preferenceClickListener);
        pDeleteData.setOnPreferenceClickListener(preferenceClickListener);
        pAbout.setOnPreferenceClickListener(preferenceClickListener);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Change app-theme
        if(key.equals(getString(R.string.pref_key_dark_mode))){
            setAppTheme(sharedPreferences, Objects.requireNonNull(getContext()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
