package com.example.volleytest1livedata;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextViewModel extends ViewModel {
    private TextRepository mRepository;
    private MutableLiveData<TextData> mTextData;
    private MutableLiveData<String> mErrorMessage;

    public TextViewModel() {
        mRepository = new TextRepository();
        mTextData = mRepository.getTextData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<TextData> getTextData() {
        return mTextData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.download(context);
    }
}