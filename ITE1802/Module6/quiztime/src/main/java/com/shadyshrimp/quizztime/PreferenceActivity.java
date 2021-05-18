package com.shadyshrimp.quizztime;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class PreferenceActivity extends AppCompatActivity {
    private static final String TAG = "PreferenceActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar mainToolbar = findViewById(R.id.preference_toolbar);
        setSupportActionBar(mainToolbar);


        // Add and enable back-arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.str_settings));
        }

        PreferenceFragment preferenceFragment = new PreferenceFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.settings_fragment, preferenceFragment).commit();



        Button button = findViewById(R.id.bt_url_toast);
        button.setOnClickListener((View v) -> {
            Toast toast = Toast.makeText(getApplicationContext(), QuizURLGenerator.generate(this), Toast.LENGTH_SHORT);
            toast.show();
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_to_bottom);
    }
}