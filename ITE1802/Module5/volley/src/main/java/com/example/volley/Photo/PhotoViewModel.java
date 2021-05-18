package com.example.volley.Photo;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhotoViewModel extends ViewModel {

    private PhotoRepository mRepository;
    private MutableLiveData<PhotoData> mPhotoData;
    private MutableLiveData<String> mErrorMessage;

    public PhotoViewModel() {
        mRepository = new PhotoRepository();
        mPhotoData = mRepository.getPhotoData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<PhotoData> getPhotoData() {
        return mPhotoData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.downloadPhoto(context);
    }

    public void startDownload(Context context, long albumId) {
        mRepository.downloadPhoto(context, albumId);
    }
}
