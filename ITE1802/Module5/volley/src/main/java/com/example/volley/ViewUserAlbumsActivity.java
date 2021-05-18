package com.example.volley;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.volley.Album.AlbumViewModel;
import com.example.volley.Album.Album;
import com.example.volley.Album.AlbumAdapter;
import com.example.volley.Album.AlbumData;

import java.util.ArrayList;

public class ViewUserAlbumsActivity extends AppCompatActivity {

    private boolean mDownloading = false;
    private ProgressBar progressWork;
    private AlbumViewModel albumViewModel;
    private ListView lvAlbumResultList;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_albums);

        userId = getIntent().getLongExtra("userId", 1);

        progressWork = findViewById(R.id.progressWork);

        lvAlbumResultList = findViewById(R.id.lvAlbumResultList);

        lvAlbumResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewAlbumPhotosIntent = new Intent(getApplicationContext(), ViewAlbumPhotosActivity.class);
                long albumId = albumViewModel.getAlbumData().getValue().getAllAlbums().get(position).getId();

                viewAlbumPhotosIntent.putExtra("albumId", albumId);
                startActivity(viewAlbumPhotosIntent);

            }
        });

        lvAlbumResultList.post(new Runnable() {
            @Override
            public void run() {
                download();
            }
        });

        // Bruker ViewModelProvider i stedet for ViewModelProviders som er deprecated.
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        this.subscribe();
    }

    private void subscribe() {
        final Observer<AlbumData> albumDataObserver = new Observer<AlbumData>() {
            @Override
            public void onChanged(AlbumData albumData) {
                if (albumData != null && albumData.getAllAlbums() != null) {

                    ArrayList<Album> alAlbumList = (ArrayList<Album>)albumData.getAllAlbums();
                    AlbumAdapter albumAdapter = new AlbumAdapter(getApplicationContext(), alAlbumList);

                    lvAlbumResultList.setAdapter(albumAdapter);

                    progressWork.setVisibility(View.INVISIBLE);
                    mDownloading = false;
                }
            }
        };
        albumViewModel.getAlbumData().observe(this, albumDataObserver);

        final Observer<String> errorMessageObserver = new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                TextView tvError = new TextView(getApplicationContext());
                tvError.setText(errorMessage);
                //lvResultList.addView(tvError);
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        albumViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download() {
        if (albumViewModel != null && !mDownloading) {
            progressWork.setVisibility(View.VISIBLE);
            mDownloading = true;
            albumViewModel.startDownload(getApplicationContext(), userId);
        }
    }
}