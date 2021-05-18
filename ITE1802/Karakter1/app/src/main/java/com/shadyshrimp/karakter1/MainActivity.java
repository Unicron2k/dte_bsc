package com.shadyshrimp.karakter1;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.shadyshrimp.karakter1.Program.Program;
import com.shadyshrimp.karakter1.Program.ProgramAdapter;
import com.shadyshrimp.karakter1.Program.ProgramData;
import com.shadyshrimp.karakter1.Program.ProgramViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private Menu menu;
    private ListView lvProgramList;
    private ProgramViewModel programViewModel;
    private boolean mDownloading = false;
    private ProgressBar loadingIndicator;
    private ProgramAdapter programAdapter;

    public static final String PROGRAM_ID = "com.shadyshrimp.karakter1.PROGRAM_ID";
    public static final String PROGRAM_NAME = "com.shadyshrimp.karakter1.PROGRAM_NAME";
    public static final String PROGRAM_DESCRIPTION = "com.shadyshrimp.karakter1.PROGRAM_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = findViewById(R.id.loadingIndicator);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        lvProgramList = findViewById(R.id.lvProgramList);
        lvProgramList.setOnItemClickListener((parent, view, position, id) -> {
            Intent viewExerciseProgramIntent = new Intent(getApplicationContext(), ViewExerciseProgramActivity.class);

            long exerciseProgramId = Objects.requireNonNull(programViewModel.getProgramData().getValue()).getAllPrograms().get(position).getId();
            String exerciseProgramName = Objects.requireNonNull(programViewModel.getProgramData().getValue()).getAllPrograms().get(position).getName();
            String exerciseProgramDescription = Objects.requireNonNull(programViewModel.getProgramData().getValue()).getAllPrograms().get(position).getDescription();

            viewExerciseProgramIntent.putExtra(PROGRAM_ID, exerciseProgramId);
            viewExerciseProgramIntent.putExtra(PROGRAM_NAME, exerciseProgramName);
            viewExerciseProgramIntent.putExtra(PROGRAM_DESCRIPTION, exerciseProgramDescription);

            startActivity(viewExerciseProgramIntent);
        });

        programViewModel = new ViewModelProvider(this).get(ProgramViewModel.class);
        this.subscribe();

        lvProgramList.post(this::download);
    }

    private void subscribe() {
        final Observer<ProgramData> ProgramDataObserver = programData -> {
            if (programData != null && programData.getAllPrograms() != null) {

                ArrayList<Program> alProgramsList = (ArrayList<Program>) programData.getAllPrograms();
                programAdapter = new ProgramAdapter(getApplicationContext(), alProgramsList);

                lvProgramList.setAdapter(programAdapter);

                loadingIndicator.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };

        programViewModel.getProgramData().observe(this, ProgramDataObserver);

        final Observer<String> errorMessageObserver = errorMessage -> {
            //TODO: Beautify this...
            Log.e(getString(R.string.str_error_tag), errorMessage);
            loadingIndicator.setVisibility(View.INVISIBLE);
            mDownloading = false;
        };
        programViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download() {
        if (programViewModel != null && !mDownloading) {
            loadingIndicator.setVisibility(View.VISIBLE);
            mDownloading = true;
            programViewModel.startDownload(getApplicationContext());
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
                lvProgramList.post(()-> download());
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
}
