package com.shadyshrimp.quizztime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.shadyshrimp.quizztime.jsonlogic.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class QuizQuestionsFragment extends Fragment {
    private static final String TAG = "Quiz";
    private Question question;
    private ArrayList<String> quizOptions;
    private int position;
    private int numQuestions = QuizActivity.getCorrectAnswers().length;

    public QuizQuestionsFragment(Question question, int position) {
        this.question = question;
        quizOptions = new ArrayList<>();
        this.position = position;
    }

    private void processQuestion(boolean correctAnswer, int buttonID){
        if (correctAnswer){ QuizActivity.setCorrectAnswers(true, position); }
        if(!question.getType().equals("boolean")){ QuizActivity.setResultMultipleChoice(buttonID,position); }

        QuizActivity.getVp2ViewPager().setCurrentItem(position+1);
    }


    public void finishQuiz(View v){
        Log.d("QuizTime", "pos: " + position + ", numQuestions: " + numQuestions + ", Status: All questions answered");
        Intent resultIntent = new Intent(getContext(), ResultActivity.class);
        resultIntent.putExtra("correctAnswers", QuizActivity.getCorrectAnswers());
        resultIntent.putExtra("resultMultipleChoice", QuizActivity.getResultMultipleChoice());
        startActivity(resultIntent);
        Objects.requireNonNull(getActivity()).finish();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.quiz_screen_slide_page, container, false);
        TextView tvQuestion = rootView.findViewById(R.id.question);
        RadioButton rbAnswer1 = rootView.findViewById(R.id.radioButton1);
        RadioButton rbAnswer2 = rootView.findViewById(R.id.radioButton2);
        RadioButton rbAnswer3 = rootView.findViewById(R.id.radioButton3);
        RadioButton rbAnswer4 = rootView.findViewById(R.id.radioButton4);

        rbAnswer1.setOnClickListener((view)->{
            boolean correctAnswer = rbAnswer1.getText().toString().equals(question.getCorrect_answer());
            processQuestion(correctAnswer,1);
        });
        rbAnswer2.setOnClickListener((view)->{
            boolean correctAnswer = rbAnswer2.getText().toString().equals(question.getCorrect_answer());
            processQuestion(correctAnswer,2);
        });
        rbAnswer3.setOnClickListener((view)->{
            boolean correctAnswer = rbAnswer3.getText().toString().equals(question.getCorrect_answer());
            processQuestion(correctAnswer,3);
        });
        rbAnswer4.setOnClickListener((view)->{
            boolean correctAnswer = rbAnswer4.getText().toString().equals(question.getCorrect_answer());
            processQuestion(correctAnswer,4);
        });

        if(position+1==numQuestions){
            Button finish = rootView.findViewById(R.id.btnFinish);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(this::finishQuiz);
        }

        quizOptions.add(question.getCorrect_answer());
        quizOptions.addAll(question.getIncorrect_answer());

        // Randomize answer order
        Collections.shuffle(quizOptions);

        if (question.getType().equals("boolean")) {
            // Boolean question
            Log.d(TAG, "onCreateView: " + question.getType());

            rbAnswer1.setText("True");
            rbAnswer2.setText("False");

            // Set other buttons to invisible when question is boolean
            rbAnswer3.setVisibility(View.INVISIBLE);
            rbAnswer4.setVisibility(View.INVISIBLE);
        } else {
            // Multiple choice question
            Log.d(TAG, "onCreateView: " + question.getType());
            Log.d(TAG, "onCreateView: correct answer - " + question.getCorrect_answer());
            Log.d(TAG, "onCreateView: incorrect answer - " + question.getIncorrect_answer());

            rbAnswer1.setText(quizOptions.get(0));
            rbAnswer2.setText(quizOptions.get(1));
            rbAnswer3.setText(quizOptions.get(2));
            rbAnswer4.setText(quizOptions.get(3));
        }
        tvQuestion.setText((position+1) + ". " + question.getQuestion());

        return rootView;
    }
}