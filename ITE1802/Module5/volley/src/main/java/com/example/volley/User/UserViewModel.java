package com.example.volley.User;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private UserRepository mRepository;
    private MutableLiveData<UserData> mUserData;
    private MutableLiveData<String> mErrorMessage;

    public UserViewModel() {
        mRepository = new UserRepository();
        mUserData = mRepository.getUserData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<UserData> getUserData() {
        return mUserData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.downloadUsers(context);
    }
}
