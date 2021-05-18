package com.example.volley;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.volley.Photo.Photo;
import com.example.volley.Photo.PhotoAdapter;
import com.example.volley.Photo.PhotoData;
import com.example.volley.Photo.PhotoViewModel;

import java.util.ArrayList;

public class ViewAlbumPhotosActivity extends AppCompatActivity {

    private boolean mDownloading = false;
    private ProgressBar progressWork;
    private PhotoViewModel photoViewModel;
    private ListView lvPhotoResultList;
    private long albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        albumId = getIntent().getLongExtra("albumId", 1);

        progressWork = findViewById(R.id.progressWork);

        lvPhotoResultList = findViewById(R.id.lvPhotoResultList);

        lvPhotoResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewPhotoIntent = new Intent(getApplicationContext(), ViewPhotoActivity.class);
                String photoURL = photoViewModel.getPhotoData().getValue().getAllPhotos().get(position).getUrl();

                viewPhotoIntent.putExtra("photoURL", photoURL);
                startActivity(viewPhotoIntent);

            }
        });

        lvPhotoResultList.post(this::download);

        // Bruker ViewModelProvider i stedet for ViewModelProviders som er deprecated.
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        this.subscribe();
    }

    private void subscribe() {
        final Observer<PhotoData> photoDataObserver = new Observer<PhotoData>() {
            @Override
            public void onChanged(PhotoData photoData) {
                if (photoData != null && photoData.getAllPhotos() != null) {

                    ArrayList<Photo> alPhotoList = (ArrayList<Photo>)photoData.getAllPhotos();
                    PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(), alPhotoList);

                    lvPhotoResultList.setAdapter(photoAdapter);

                    progressWork.setVisibility(View.INVISIBLE);
                    mDownloading = false;
                }
            }
        };
        photoViewModel.getPhotoData().observe(this, photoDataObserver);

        final Observer<String> errorMessageObserver = new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                TextView tvError = new TextView(getApplicationContext());
                tvError.setText(errorMessage);
                //lvPhotoResultList.addView(tvError);
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        photoViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download() {
        if (photoViewModel != null && !mDownloading) {
            progressWork.setVisibility(View.VISIBLE);
            mDownloading = true;
            photoViewModel.startDownload(getApplicationContext(), albumId);
        }
    }
}