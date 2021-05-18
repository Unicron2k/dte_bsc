package com.shadyshrimp.quizztime;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        Button btnStartQuiz = findViewById(R.id.button1);
        Button btnResumeQuiz = findViewById(R.id.button2);
        Button btnSettings = findViewById(R.id.button3);

        btnStartQuiz.setOnClickListener(v -> {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            startActivity(quizIntent);
        });

        btnResumeQuiz.setOnClickListener(v -> {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            String flagString = "continue";
            quizIntent.putExtra("flag", flagString);
            startActivity(quizIntent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent quizIntent = new Intent(this, PreferenceActivity.class);
            startActivity(quizIntent);
        });

    }
}
