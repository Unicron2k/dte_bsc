package com.shadyshrimp.karakter1.Program;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgramViewModel extends ViewModel {
    private ProgramRepository mRepository;
    private MutableLiveData<ProgramData> mProgramData;
    private MutableLiveData<String> mErrorMessage;

    public ProgramViewModel() {
        mRepository = new ProgramRepository();
        mProgramData = mRepository.getProgramData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<ProgramData> getProgramData() {
        return mProgramData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.downloadPrograms(context);
    }
}
