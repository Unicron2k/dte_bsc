package com.example.volleytest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

// SE
// https://developer.android.com/topic/libraries/architecture/livedata?authuser=1
// og
// https://medium.com/@ogieben/basics-of-viewmodel-and-livedata-4550a34c19f3
public class MainActivity extends AppCompatActivity {

    private boolean mDownloading = false;
    private ProgressBar progressWork;
    private AlbumsViewModel albumsViewModel;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressWork = findViewById(R.id.progressWork);

        // Gjør at TextView-en blir scrollbar, vertikalt (se layout):
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(new ScrollingMovementMethod());

        // Bruker ViewModelProvider i stedet for ViewModelProviders som er deprecated.
        albumsViewModel = new ViewModelProvider(this).get(AlbumsViewModel.class);
        this.subscribe();
    }

    private void subscribe() {
        final Observer<AlbumData> photoDataObserver = new Observer<AlbumData>() {
            @Override
            public void onChanged(AlbumData albumData) {
                TextView tvResult = findViewById(R.id.tvResult);
                tvResult.setBackgroundColor(Color.GREEN);
                tvResult.setText(albumData.toString());
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        albumsViewModel.getPhotoData().observe(this, photoDataObserver);

        final Observer<String> errorMessageObserver = new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                TextView tvResult = findViewById(R.id.tvResult);
                tvResult.setBackgroundColor(Color.RED);
                tvResult.setText(errorMessage);
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        albumsViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download(View view) {
        if (albumsViewModel != null && !mDownloading) {
            progressWork.setVisibility(View.VISIBLE);
            mDownloading = true;
            albumsViewModel.startDownload(getApplicationContext());
        }
    }

    public void clearText(View view) {
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setBackgroundColor(Color.LTGRAY);
        tvResult.setText("");
    }
}
