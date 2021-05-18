package com.shadyshrimp.lab9_2.exercise;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerciseViewModel extends ViewModel {
    private ExerciseRepository mRepository;
    private MutableLiveData<ExerciseData> mExerciseData;
    private MutableLiveData<String> mErrorMessage;

    public ExerciseViewModel() {
        mRepository = new ExerciseRepository();
        mExerciseData = mRepository.getExerciseData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<ExerciseData> getExerciseData() {
        return mExerciseData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context, long programId) {
        mRepository.downloadExercises(context, programId);
    }

    public void startDownload(Context context) {
        mRepository.downloadExercises(context);
    }
}
