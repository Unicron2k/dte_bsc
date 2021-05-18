package com.shadyshrimp.quizztime;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.shadyshrimp.quizztime.jsonlogic.Question;
import com.shadyshrimp.quizztime.jsonlogic.QuestionAdapter;
import com.shadyshrimp.quizztime.jsonlogic.QuestionData;
import com.shadyshrimp.quizztime.jsonlogic.QuestionViewModel;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private static ViewPager2 vp2ViewPager;
    private FragmentStateAdapter fsaQuestionAdapter;
    private ProgressBar pbLoadingIndicator;
    private QuestionViewModel questionViewModel;
    private boolean mDownloading = false;
    protected static int[] resultMultipleChoice;
    protected static boolean[] correctAnswers;

    public static int[] getResultMultipleChoice() {
        return resultMultipleChoice;
    }

    public static void setResultMultipleChoice(int result, int index) {
        QuizActivity.resultMultipleChoice[index] = result;
    }

    public static boolean[] getCorrectAnswers() {
        return correctAnswers;
    }

    public static void setCorrectAnswers(boolean result, int index) {
        QuizActivity.correctAnswers[index] = result;
    }

    public static ViewPager2 getVp2ViewPager() {
        return vp2ViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right,  R.anim.no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar mainToolbar = findViewById(R.id.quiz_toolbar);
        setSupportActionBar(mainToolbar);

        // Add and enable back-arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pbLoadingIndicator = findViewById(R.id.loading_indicator);
        pbLoadingIndicator.setVisibility(View.INVISIBLE);
        vp2ViewPager = findViewById(R.id.pager);
        Bundle extras = getIntent().getExtras();
        boolean noStoredQuestions = true;
        if(extras != null){
            String flag = extras.getString("flag");
            assert flag != null;
            if(flag.equals("continue")){
                noStoredQuestions = false;
            }
        }

        if(noStoredQuestions){
            vp2ViewPager.post(this::download);
            //Disables user-swipe
            vp2ViewPager.setUserInputEnabled(false);
        } else {
            vp2ViewPager.post(this::readQuestions);
            //Load questions
            //resultMultipleChoice = new int[Number of Questions];
            //resultTrueFalse = new boolean[number of Questions];
        }
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        this.subscribe();

    }

    private void subscribe() {
        final Observer<QuestionData> questionDataObserver = questionData -> {
            if (questionData != null && questionData.getAllQuestions() != null) {
                ArrayList<Question> allQuestionsList = questionData.getAllQuestions();
                fsaQuestionAdapter = new QuestionAdapter(this, allQuestionsList);
                resultMultipleChoice = new int[allQuestionsList.size()];
                correctAnswers = new boolean[allQuestionsList.size()];
                vp2ViewPager.setAdapter(fsaQuestionAdapter);

                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        questionViewModel.getMQuestionData().observe(this, questionDataObserver);

        final Observer<String> errorMessageObserver = errorMessage -> {
            // TODO: Add *better* error-message handling code
            Log.e("QuizTime", errorMessage);
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mDownloading = false;
        };
        questionViewModel.getMErrorMessage().observe(this, errorMessageObserver);
    }
    public void readQuestions(){
        if(questionViewModel != null){
            questionViewModel.questionsFromFile(getApplicationContext());
        }
    }
    public void download() {
        if (questionViewModel != null && !mDownloading) {
            pbLoadingIndicator.setVisibility(View.VISIBLE);
            mDownloading = true;
            questionViewModel.startDownload(getApplicationContext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // TODO: save current state of Quiz, then return to MainActivity
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_to_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (vp2ViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
            //finish();
            overridePendingTransition(R.anim.no_animation, R.anim.slide_out_to_right);
        } else {
            vp2ViewPager.setCurrentItem(vp2ViewPager.getCurrentItem() - 1);
        }
    }
}
