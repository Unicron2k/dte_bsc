package com.shadyshrimp.karakter1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.squareup.picasso.Picasso;

public class ViewExerciseDetailsActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private Menu menu;
    private ImageView ivIcon;
    private TextView tvExerciseName;
    private WebView wvExerciseDescription;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_details);



        ivIcon = findViewById(R.id.ivIcon);
        tvExerciseName = findViewById(R.id.tvExerciseName);
        wvExerciseDescription = findViewById(R.id.wvExerciseDescription);
        rootView = findViewById(android.R.id.content).getRootView();

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        String exerciseDetailsName = intent.getStringExtra(ViewExerciseProgramActivity.EXERCISE_NAME);
        String exerciseDetailsDescription = intent.getStringExtra(ViewExerciseProgramActivity.EXERCISE_DESCRIPTION);
        String exerciseDetailsIcon = intent.getStringExtra(ViewExerciseProgramActivity.EXERCISE_ICON);
        String exerciseDetailsInfoboxColor = intent.getStringExtra(ViewExerciseProgramActivity.EXERCISE_INFOBOX_COLOR);

        String iconUrl = "https://tusk.systems/trainingapp/icons/"+exerciseDetailsIcon;
        Picasso.get().load(iconUrl).placeholder(R.drawable.ic_hourglass_empty_black_24dp).into(ivIcon);

        rootView.setBackgroundColor(Color.parseColor(exerciseDetailsInfoboxColor));

        tvExerciseName.setText(exerciseDetailsName);

        assert exerciseDetailsDescription != null;
        String exerciseDetailsDescriptionHTML = "<hr>" + exerciseDetailsDescription.replace("\n",exerciseDetailsDescription.contains("<p>")?"":"</br>");
        wvExerciseDescription.setBackgroundColor(Color.parseColor(exerciseDetailsInfoboxColor));
        wvExerciseDescription.loadData(exerciseDetailsDescriptionHTML, "text/html", "UTF-8");

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_refresh).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.str_settings), Toast.LENGTH_SHORT).show();
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
