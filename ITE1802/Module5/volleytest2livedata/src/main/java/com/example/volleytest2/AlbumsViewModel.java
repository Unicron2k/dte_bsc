package com.example.volleytest2;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlbumsViewModel extends ViewModel {
    private AlbumRepository mRepository;
    private MutableLiveData<AlbumData> mAlbumData;
    private MutableLiveData<String> mErrorMessage;

    public AlbumsViewModel() {
        mRepository = new AlbumRepository();
        mAlbumData = mRepository.getAlbumData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<AlbumData> getPhotoData() {
        return mAlbumData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.download(context);
    }
}