package com.shadyshrimp.quizztime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.shadyshrimp.quizztime.jsonlogic.Category;
import com.shadyshrimp.quizztime.jsonlogic.CategoryData;
import com.shadyshrimp.quizztime.jsonlogic.QuestionViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class PreferenceFragment extends PreferenceFragmentCompat {
    private Context mContext;
    private ListPreference listPreference;
    private ArrayList<String> entriesAL;
    private ArrayList<String> entryValuesAL;
    private ProgressBar progressBar;
    private Activity activity;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
        activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        activity = null;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preference_screen, rootKey);
        listPreference = findPreference("category");
        progressBar = activity.findViewById(R.id.loading_indicator_preference);
        if (listPreference != null) {
            entriesAL = new ArrayList<>();
            entriesAL.add("Any category");
            entryValuesAL = new ArrayList<>();
            entryValuesAL.add("ANY");
            QuestionViewModel questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
            if (mContext != null) {
                questionViewModel.categoriesFromFile(mContext);
                if (questionViewModel.getMRepository().getCategoryArray().size() == 0) {
                    if(progressBar != null){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    questionViewModel.downloadCategories(mContext);
                    final Observer<CategoryData> categoryDataObserver = categoryData -> {
                        if(categoryData != null && categoryData.getAllCategories() != null){
                            try {
                                ArrayList<Category> categoryArray = categoryData.getAllCategories();
                                //iterates for the size of the ArrayList and adds each value to their respective arrays from the category array
                                for (int i = 0; i < categoryArray.size(); i++) {
                                    entriesAL.add(categoryArray.get(i).getName());
                                    entryValuesAL.add(categoryArray.get(i).getId());
                                }
                                CharSequence[] entries = entriesAL.toArray(new CharSequence[0]);
                                listPreference.setEntries(entries);
                                CharSequence[] entryValues = entryValuesAL.toArray(new CharSequence[0]);
                                listPreference.setEntryValues(entryValues);
                                if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    questionViewModel.getMCategoryData().observe(this, categoryDataObserver);
                } else   {
                    ArrayList<Category> categoryArray = questionViewModel.getMRepository().getCategoryArray();
                    //iterates for the size of the ArrayList and adds each value to their respective arrays from the category array
                    for (int i = 0; i < categoryArray.size(); i++) {
                        entriesAL.add(categoryArray.get(i).getName());
                        entryValuesAL.add(categoryArray.get(i).getId());
                    }
                    CharSequence[] entries = entriesAL.toArray(new CharSequence[0]);
                    listPreference.setEntries(entries);
                    CharSequence[] entryValues = entryValuesAL.toArray(new CharSequence[0]);
                    listPreference.setEntryValues(entryValues);
                }
            }

        }
    }
}