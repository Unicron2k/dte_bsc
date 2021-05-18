package com.example.volleytest1livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
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
    private TextViewModel textViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressWork = findViewById(R.id.progressWork);

        // Bruker ViewModelProvider i stedet for ViewModelProviders som er deprecated.
        textViewModel = new ViewModelProvider(this).get(TextViewModel.class);
        this.subscribe();
    }

    private void subscribe() {
        final Observer<TextData> textDataObserver = new Observer<TextData>() {
            @Override
            public void onChanged(TextData albumData) {
                TextView tvResult = findViewById(R.id.tvResult);
                tvResult.setBackgroundColor(Color.GREEN);
                tvResult.setText(albumData.toString());
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        textViewModel.getTextData().observe(this, textDataObserver);

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
        textViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download(View view) {
        if (textViewModel != null && !mDownloading) {
            progressWork.setVisibility(View.VISIBLE);
            mDownloading = true;
            textViewModel.startDownload(getApplicationContext());
        }
    }

    public void clearText(View view) {
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setBackgroundColor(Color.LTGRAY);
        tvResult.setText("");
    }
}
