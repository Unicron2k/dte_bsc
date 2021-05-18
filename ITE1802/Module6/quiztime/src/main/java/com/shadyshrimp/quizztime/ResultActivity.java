package com.shadyshrimp.quizztime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ResultActivity extends AppCompatActivity {
    private boolean[] correctAnswers = {false};
    private int[] resultMultipleChoice = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        // Add and enable back-arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent resultIntent = getIntent();
        correctAnswers = resultIntent.getBooleanArrayExtra("correctAnswers");
        resultMultipleChoice = resultIntent.getIntArrayExtra("resultMultipleChoice");

        int numCorrect=0;
        for (boolean correctAnswer : correctAnswers) {
            if (correctAnswer) numCorrect++;
        }

        TextView textView = findViewById(R.id.textView);
        textView.setText("You had " + numCorrect + " of " + correctAnswers.length + " correct answers.");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
