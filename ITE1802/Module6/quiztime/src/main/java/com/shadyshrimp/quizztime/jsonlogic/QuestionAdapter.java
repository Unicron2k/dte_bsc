package com.shadyshrimp.quizztime.jsonlogic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.shadyshrimp.quizztime.QuizQuestionsFragment;

import java.util.ArrayList;


public class QuestionAdapter extends FragmentStateAdapter {
    private static int NUM_QUESTIONS;
    private ArrayList<Question> allQuestions;

    public QuestionAdapter(FragmentActivity fa, ArrayList<Question> allQuestions){
        super(fa);
        this.allQuestions = allQuestions;
        QuestionAdapter.NUM_QUESTIONS = this.allQuestions.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new QuizQuestionsFragment(allQuestions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return NUM_QUESTIONS;
    }
}