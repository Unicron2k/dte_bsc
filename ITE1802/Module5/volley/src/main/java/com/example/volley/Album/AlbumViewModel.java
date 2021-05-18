package com.example.volley.Album;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlbumViewModel extends ViewModel {
    private AlbumRepository mRepository;
    private MutableLiveData<AlbumData> mAlbumData;
    private MutableLiveData<String> mErrorMessage;

    public AlbumViewModel() {
        mRepository = new AlbumRepository();
        mAlbumData = mRepository.getAlbumData();
        mErrorMessage = mRepository.getErrorMessage();
    }

    public LiveData<AlbumData> getAlbumData() {
        return mAlbumData;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void startDownload(Context context) {
        mRepository.downloadAlbum(context);
    }

    public void startDownload(Context context, long albumID) {
        mRepository.downloadAlbum(context, albumID);
    }
}
