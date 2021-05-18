package com.shadyshrimp.karakter1;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.shadyshrimp.karakter1.Exercise.Exercise;
import com.shadyshrimp.karakter1.Exercise.ExerciseAdapter;
import com.shadyshrimp.karakter1.Exercise.ExerciseData;
import com.shadyshrimp.karakter1.Exercise.ExerciseViewModel;

import java.util.ArrayList;
import java.util.Objects;


public class ViewExerciseProgramActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private Menu menu;
    private ListView lvExerciseList;
    private ExerciseViewModel exerciseViewModel;
    private boolean mDownloading = false;
    private ProgressBar loadingIndicator;
    private long exerciseProgramId;
    private ExerciseAdapter exerciseAdapter;

    public static final String EXERCISE_NAME = "com.shadyshrimp.karakter1.EXERCISE_NAME";
    public static final String EXERCISE_DESCRIPTION = "com.shadyshrimp.karakter1.EXERCISE_DESCRIPTION";
    public static final String EXERCISE_ICON = "com.shadyshrimp.karakter1.EXERCISE_ICON";
    public static final String EXERCISE_INFOBOX_COLOR = "com.shadyshrimp.karakter1.EXERCISE_INFOBOX_COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_program);

        lvExerciseList = findViewById(R.id.lvExerciseList);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        TextView tvExerciseProgramName = findViewById(R.id.exerciseName);
        TextView tvExerciseProgramDescription = findViewById(R.id.exerciseDescription);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        exerciseProgramId = intent.getLongExtra(MainActivity.PROGRAM_ID, 0);
        String exerciseProgramName = intent.getStringExtra(MainActivity.PROGRAM_NAME);
        String exerciseProgramDescription = intent.getStringExtra(MainActivity.PROGRAM_DESCRIPTION);

        tvExerciseProgramName.setText(exerciseProgramName);
        tvExerciseProgramDescription.setText(exerciseProgramDescription);

        lvExerciseList.setOnItemClickListener((parent, view, position, id) -> {

            Intent viewExerciseDetailsIntent = new Intent(getApplicationContext(), ViewExerciseDetailsActivity.class);

            String exerciseDetailsName = Objects.requireNonNull(exerciseViewModel.getExerciseData().getValue()).getAllExercises().get(position).getName();
            String exerciseDetailsDescription = Objects.requireNonNull(exerciseViewModel.getExerciseData().getValue()).getAllExercises().get(position).getDescription();
            String exerciseDetailsIcon = Objects.requireNonNull(exerciseViewModel.getExerciseData().getValue()).getAllExercises().get(position).getIcon();
            String exerciseDetailsInfoboxColor = Objects.requireNonNull(exerciseViewModel.getExerciseData().getValue()).getAllExercises().get(position).getInfobox_color();

            viewExerciseDetailsIntent.putExtra(EXERCISE_NAME, exerciseDetailsName);
            viewExerciseDetailsIntent.putExtra(EXERCISE_DESCRIPTION, exerciseDetailsDescription);
            viewExerciseDetailsIntent.putExtra(EXERCISE_ICON, exerciseDetailsIcon);
            viewExerciseDetailsIntent.putExtra(EXERCISE_INFOBOX_COLOR, exerciseDetailsInfoboxColor);

            startActivity(viewExerciseDetailsIntent);
        });

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        this.subscribe();

        lvExerciseList.post(()-> download(exerciseProgramId));
    }

    private void subscribe() {
        final Observer<ExerciseData> ExerciseDataObserver = exerciseData -> {
            if (exerciseData != null && exerciseData.getAllExercises() != null) {

                ArrayList<Exercise> allExercisesList = (ArrayList<Exercise>) exerciseData.getAllExercises();
                exerciseAdapter = new ExerciseAdapter(getApplicationContext(), allExercisesList);

                lvExerciseList.setAdapter(exerciseAdapter);

                loadingIndicator.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };

        exerciseViewModel.getExerciseData().observe(this, ExerciseDataObserver);

        final Observer<String> errorMessageObserver = errorMessage -> {
            //TODO: Beautify this...
            Log.e(getString(R.string.str_error_tag), errorMessage);
            loadingIndicator.setVisibility(View.INVISIBLE);
            mDownloading = false;
        };
        exerciseViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download(long exerciseProgramId) {
        if (exerciseViewModel != null && !mDownloading) {
            loadingIndicator.setVisibility(View.VISIBLE);
            mDownloading = true;
            exerciseViewModel.startDownload(getApplicationContext(), exerciseProgramId);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.str_settings), Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_refresh:
                Toast.makeText(this, getString(R.string.str_refresh), Toast.LENGTH_SHORT).show();
                lvExerciseList.post(()-> download(exerciseProgramId));
                break;

            case R.id.action_about:
                Toast.makeText(this, getString(R.string.str_about_text), Toast.LENGTH_LONG).show();
                break;

            case R.id.action_quit:
                //Quitting. A bit dirty, but gets the job done...
                finishAffinity();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_to_right);
    }
}